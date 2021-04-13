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
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatObjRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.OperationsOnTablesRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.event.TableProcessedEvent;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class TableService {

    private static final int PAGE_SIZE = Integer.parseInt(Utils.getJNDIValue("java:comp/env/increment/pagination/pageSize"));

    private final OperationsOnTablesRawDataDAO operationsOnTablesRawDataDAO;
    private final TransferDataService transferDataService;
    private final PrepareDataForTransferService prepareDataForTransferService;
    private final IncrementStateService incrementStateService;
    private final JdbcPostgresCtlLoadingDao jdbcPostgresCtlLoadingDao;
    private final EspdMatObjRawDataDAO espdMatObjRawDataDAO;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SbBrdServiceAuditService loggerAudit;


    @Transactional(propagation = Propagation.NESTED, transactionManager = "transactionManagerRawData")
    void processTable(EspdMatObj espdMatObj, EspdMat espdMat) {

        loggerAudit.send("Starting processing the table " + espdMatObj.getSrcRealTable(), SubTypeIdAuditEvent.F0.name());

        String tableName = espdMatObj.getSrcRealTable();
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
        incrementStateService.saveNewIncrementStates(incrementStateForCurrentTable);

        operationsOnTablesRawDataDAO.createTableIfNotExist(tableName);
        operationsOnTablesRawDataDAO.createColumnIncrPackRunIdIfNotExist(tableName);

        List<Column> preparedColumns = prepareDataForTransferService.getColumns(tableName);
        int currentPageNum;
        for (int i = 0; i <= prepareDataForTransferService.findPagesCount(tableName, PAGE_SIZE); i++) {
            currentPageNum = i;
            Page<Map<String, Object>> dataList =
                    prepareDataForTransferService.findPaginated(tableName, PageRequest.of(currentPageNum, PAGE_SIZE));
            prepareDataForTransferService.joinColumnsAndData(preparedColumns, dataList.getContent());
            transferDataService.upsert(tableName, preparedColumns);
        }

        espdMatObjRawDataDAO.save(espdMatObj);
        applicationEventPublisher.publishEvent(new TableProcessedEvent(incrementStateForCurrentTable));

        loggerAudit.send("Finishing processing the table " + espdMatObj.getSrcRealTable(), SubTypeIdAuditEvent.F0.name());

    }

}
