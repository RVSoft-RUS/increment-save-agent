package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.audit.SbBrdServiceAuditService;
import ru.sberbank.ckr.sberboard.increment.audit.SubTypeIdAuditEvent;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresTablesInfoDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatObjRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.OperationsOnTablesRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.DataByTableNameRawDataIncrementDao;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.event.TableProcessedEvent;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TableService {

    private static final long MEMORY_AVAILABLE  = 1024 * 1024 * Integer.parseInt(Utils.getJNDIValue("java:comp/env/increment/memoryAvailable"));
    private static final int MAX_PAGE_SIZE  = 50_000;

    private final OperationsOnTablesRawDataDAO operationsOnTablesRawDataDAO;
    private final TransferDataService transferDataService;
    private final PrepareDataForTransferService prepareDataForTransferService;
    private final IncrementStateService incrementStateService;
    private final JdbcPostgresCtlLoadingDao jdbcPostgresCtlLoadingDao;
    private final EspdMatObjRawDataDAO espdMatObjRawDataDAO;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SbBrdServiceAuditService loggerAudit;
    private final SbBrdServiceLoggingService loggerTech;
    private final DataByTableNameRawDataIncrementDao dataByTableNameRawDataIncrementDao;
    private final JdbcPostgresTablesInfoDao jdbcPostgresTablesInfoDao;


    @Transactional(propagation = Propagation.NESTED, transactionManager = "transactionManagerRawData")
    void processTable(EspdMatObj espdMatObj, EspdMat espdMat, IncrementState incrementForCurrentPackage) {
        String tableName = espdMatObj.getSrcRealTable();
        loggerAudit.send("Starting processing the table " + tableName +
                " with size: " + jdbcPostgresTablesInfoDao.getTableSizeAsString(tableName) +
                " and " + jdbcPostgresTablesInfoDao.getColumnsCountFromTable(tableName) +
                " fields in the table.", SubTypeIdAuditEvent.F0.name());

        IncrementState incrementStateForCurrentTable = IncrementState.builder()
                .packageSmd(espdMatObj.getPackageSmd())
                .subscrId(espdMatObj.getSubscrId())
                .objType(IncrementStateObjType.TABLE)
                .objsInPack(1)
                .workflowEndDt(espdMat.getWorkflowEndDt())
                .startDt(LocalDateTime.now())
                .targetTable(tableName)
                .minCtlLoading(jdbcPostgresCtlLoadingDao.getMinCtlLoadingFromTable(tableName))
                .maxCtlLoading(jdbcPostgresCtlLoadingDao.getMaxCtlLoadingFromTable(tableName))
                .incrementationState(IncrementStateStatus.TABLE_IN_PROCESS.status)
                .build();
        applicationEventPublisher.publishEvent(new TableProcessedEvent(incrementStateForCurrentTable));
        incrementStateService.saveNewIncrementStates(incrementStateForCurrentTable);

        operationsOnTablesRawDataDAO.createTableIfNotExist(tableName);
        operationsOnTablesRawDataDAO.createColumnIncrPackRunIdIfNotExist(tableName);

        int pageSize = getPageSize(tableName);

        List<Column> preparedColumns = prepareDataForTransferService.getColumns(tableName);
        int currentPageNum;
        int countRows = dataByTableNameRawDataIncrementDao.getCountByTableName(tableName);
        int countPages = prepareDataForTransferService.findPagesCount(tableName, pageSize);
        loggerTech.send("Start transfer data to raw_data." + tableName +
                        ". Count rows in table: " + countRows +
                        ". Count pages: " + countPages + ". Page size: " + pageSize,
                SubTypeIdLoggingEvent.INFO.name());
        for (int i = 0; i < countPages; i++) {
            currentPageNum = i;
            Page<Map<String, Object>> dataList =
                    prepareDataForTransferService.findPaginated(tableName, PageRequest.of(currentPageNum, pageSize), incrementForCurrentPackage);
            prepareDataForTransferService.joinColumnsAndData(preparedColumns, dataList.getContent());
            String sqlQuery = transferDataService.queryBuild(preparedColumns, tableName);
            transferDataService.upsert(preparedColumns, sqlQuery);
        }
        espdMatObjRawDataDAO.save(espdMatObj);

        loggerAudit.send("Finishing processing the table " + espdMatObj.getSrcRealTable(), SubTypeIdAuditEvent.F0.name());

    }

    private int getPageSize(String tableName) {
        long tableSize = jdbcPostgresTablesInfoDao.getTableSize(tableName);
        long numberOfEntries = jdbcPostgresTablesInfoDao.getNumberOfEntries(tableName);
        int pageSize = (int) (numberOfEntries * MEMORY_AVAILABLE / tableSize);
        return Math.max(Math.min(pageSize, MAX_PAGE_SIZE), 1) ;
    }
}
