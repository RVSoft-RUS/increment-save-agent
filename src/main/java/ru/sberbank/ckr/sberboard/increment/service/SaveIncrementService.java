package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatObjRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;

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

    private static final Logger logger = LogManager.getLogger(SaveIncrementService.class.getSimpleName());

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
                espdMatRawDataIncrementDAO.findAllUniqueSubscribeId()
                        .stream()
                        .map(espdMatRawDataIncrementDAO::findActualEspdMatToProcess)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        Map<EspdMat, List<EspdMatObj>> espdMatListMap = new HashMap<>();

        logger.info("Found packages to process: "+packagesToProcess.size());
        for (EspdMat espdMat : packagesToProcess) {

            logger.info("Check if the package '"+espdMat.getPackageSmd()+"' has tables to load");
            List<EspdMatObj> espdMatObjsList = getEspdMatObjsForCurrentActualEspdMat(espdMat);

             if (espdMatObjsList != null) {
                 logger.info("The package '"+espdMat.getPackageSmd()+"' has "+espdMatObjsList.size()+" tables: "+espdMatObjsList.toString());
                 espdMatListMap.put(espdMat, espdMatObjsList);
            }
        }
        return espdMatListMap;
    }

    public List<EspdMatObj> getEspdMatObjsForCurrentActualEspdMat(EspdMat espdMat) {
        logger.info("Search incrementState for subscribe id: "+espdMat.getSubscrId());
        IncrementState incrementStates = incrementStatesRepository.findBySubscrIdAndObjType(
                espdMat.getSubscrId(),
                IncrementStateObjType.PACKAGE
        );
        if (incrementStates != null && (
                incrementStates.getIncrementationState() == IncrementStateStatus.PACKAGE_PROCESSED_SUCCESSFULLY.status
                        || incrementStates.getIncrementationState() == IncrementStateStatus.PACKAGE_IN_PROCESS.status)
                && (incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt())
                || incrementStates.getWorkflowEndDt().isEqual(espdMat.getWorkflowEndDt()))) {

            logger.info("The package'"+incrementStates.getPackageSmd()+"' has already been processed");
            return null;
        }
        logger.info("The package '"+espdMat.getSubscrId()+"' ready to process");

        return espdMatObjRawDataIncrementDAO.findAllByPackageSmd(espdMat.getPackageSmd());
    }

}
