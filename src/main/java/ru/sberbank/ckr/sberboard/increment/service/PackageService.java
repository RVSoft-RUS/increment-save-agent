package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
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

    @Transactional(transactionManager = "transactionManagerRawData")
    public void processPackage(EspdMat espdMat, List<EspdMatObj> espdMatObjs) {
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
    }

}
