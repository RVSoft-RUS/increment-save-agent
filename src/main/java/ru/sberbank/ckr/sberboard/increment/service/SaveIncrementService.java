package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatObjRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SaveIncrementService {
    private final EspdMatRawDataIncrementDAO espdMatRawDataIncrementDAO;
    private final EspdMatObjRawDataIncrementDAO espdMatObjRawDataIncrementDAO;
    private final IncrementStateRepository incrementStatesRepository;
    private final SbBrdServiceLoggingService loggerTech;

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
                espdMatRawDataIncrementDAO.findAllUniqueSubscribeId()
                        .stream()
                        .map(espdMatRawDataIncrementDAO::findActualEspdMatToProcess)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        Map<EspdMat, List<EspdMatObj>> espdMatListMap = new HashMap<>();

        loggerTech.send("Found packages to process: "
                + packagesToProcess.size(), SubTypeIdLoggingEvent.INFO.name());

        for (EspdMat espdMat : packagesToProcess) {

            loggerTech.send("Check if the package '" + espdMat.getPackageSmd()
                    + "' has tables to load", SubTypeIdLoggingEvent.INFO.name());

            List<EspdMatObj> espdMatObjsList = getEspdMatObjsForCurrentActualEspdMat(espdMat);

            if (espdMatObjsList != null) {
                loggerTech.send("The package '" + espdMat.getPackageSmd() + "' has " + espdMatObjsList.size()
                        + " tables: " + espdMatObjsList.toString(), SubTypeIdLoggingEvent.INFO.name());

                espdMatListMap.put(espdMat, espdMatObjsList);
            }
        }
        return espdMatListMap;
    }

    public List<EspdMatObj> getEspdMatObjsForCurrentActualEspdMat(EspdMat espdMat) {

        loggerTech.send("Search incrementState for subscribe id: " + espdMat.getSubscrId(), SubTypeIdLoggingEvent.INFO.name());

        IncrementState incrementStates = incrementStatesRepository.findFirstBySubscrIdAndObjTypeOrderByStartDtDesc(
                espdMat.getSubscrId(),
                IncrementStateObjType.PACKAGE
        );
        if (incrementStates != null && (incrementStates.getIncrementationState()
                .equals(IncrementStateStatus.PACKAGE_PROCESSED_SUCCESSFULLY.status)
                        || incrementStates.getIncrementationState()
                .equals(IncrementStateStatus.PACKAGE_IN_PROCESS.status))
                && (incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt())
                || incrementStates.getWorkflowEndDt().isEqual(espdMat.getWorkflowEndDt()))) {

            loggerTech.send(LocalDateTime.now() + ": The package'" + incrementStates.getPackageSmd() + "' has already been processed with end_date " + incrementStates.getEndDt(), SubTypeIdLoggingEvent.INFO.name());
            return null;
        }    //?? ?????? ???? ???????????????????? ????????????, ???????? incrementStates = null
        loggerTech.send("The package '" + espdMat.getPackageSmd() + "' of subscribe id '" + espdMat.getSubscrId() + "' ready to process", SubTypeIdLoggingEvent.INFO.name());

        return espdMatObjRawDataIncrementDAO.findAllByPackageSmd(espdMat.getPackageSmd());
    }

    public EspdMat getEspdMatByPackageSmd(String packageSmd) {
        return espdMatRawDataIncrementDAO.findEspdMatToProcessByPackageSmd(packageSmd);
    }

    public List<EspdMatObj> getEspdMatObjsByEspdMat(EspdMat espdMat) {
        return getEspdMatObjsForCurrentActualEspdMat(espdMat);
    }

}
