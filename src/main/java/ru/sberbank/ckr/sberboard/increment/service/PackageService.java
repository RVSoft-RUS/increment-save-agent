package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.event.PackageProcessedEvent;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PackageService {

    private final TableService tableService;
    private final EspdMatRawDataDAO espdMatRawDataDAO;
    private final IncrementStateService incrementStateService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final Logger logger = LogManager.getLogger(PackageService.class.getSimpleName());


    @Transactional(transactionManager = "transactionManagerRawData")
    public void processPackage(EspdMat espdMat, List<EspdMatObj> espdMatObjs) {
        //TODO Начало обработки пакета

        logger.info("Start processing the package " + espdMat.getPackageSmd());

        IncrementState incrementForCurrentPackage = IncrementState.builder()
                .startDt(LocalDateTime.now())
                .incrementationState(IncrementStateStatus.PACKAGE_IN_PROCESS.status)
                .build();
        incrementForCurrentPackage.setPackageSmd(espdMat.getPackageSmd());
        incrementForCurrentPackage.setSubscrId(espdMat.getSubscrId());
        incrementForCurrentPackage.setObjType(IncrementStateObjType.PACKAGE);
        incrementForCurrentPackage.setObjsInPack(espdMat.getObjs());
        incrementForCurrentPackage.setWorkflowEndDt(espdMat.getWorkflowEndDt());

        incrementStateService.saveNewIncrementStates(incrementForCurrentPackage);
        espdMatObjs.forEach(espdMatObj -> tableService.processTable(espdMatObj, espdMat));
        espdMatRawDataDAO.save(espdMat);
        applicationEventPublisher.publishEvent(new PackageProcessedEvent(incrementForCurrentPackage));
        //TODO Завершение обработки пакета
    }

}
