package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatObjRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.event.TableProcessedEvent;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TableService {

    private final TransferDataService transferDataService;
    private final PrepareDataForTransferService prepareDataForTransferService;
    private final IncrementStateService incrementStateService;
    private final JdbcPostgresCtlLoadingDao jdbcPostgresCtlLoadingDao;
    private final EspdMatObjRawDataDAO espdMatObjRawDataDAO;
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final Logger logger = LogManager.getLogger(TableService.class.getSimpleName());


    @Transactional(propagation = Propagation.NESTED, transactionManager = "transactionManagerRawData")
    void processTable(EspdMatObj espdMatObj, EspdMat espdMat) {
        //TODO Журналирование Начало процесса применения инкремента к таблице в рамках определенного пакета
        logger.info("Starting processing the table " + espdMatObj.getSrcRealTable());
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

        List<Column> preparedDataInColumns = prepareDataForTransferService.getDataTable(tableName);
        transferDataService.upsert(tableName, preparedDataInColumns);
        espdMatObjRawDataDAO.save(espdMatObj);
        applicationEventPublisher.publishEvent(new TableProcessedEvent(incrementStateForCurrentTable));
        logger.info("Finishing processing the table " + espdMatObj.getSrcRealTable());
        //TODO Журналирование Окончание процесса применения инкремента к таблице в рамках определенного пакета
    }

}
