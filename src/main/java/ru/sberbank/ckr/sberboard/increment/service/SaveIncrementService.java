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

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
                espdMatRawDataIncrementDAO.findAllUniqueSubscribeId()
                        .stream()
                        .map(espdMatRawDataIncrementDAO::findActualEspdMatToProcess)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        Map<EspdMat, List<EspdMatObj>> espdMatListMap = new HashMap<>();
        for (EspdMat espdMat : packagesToProcess) {
            List<EspdMatObj> espdMatObjsList = getEspdMatObjsForCurrentActualEspdMat(espdMat);
            if (espdMatObjsList != null) {
                espdMatListMap.put(espdMat, espdMatObjsList);
            }
        }
        return espdMatListMap;
    }

    public List<EspdMatObj> getEspdMatObjsForCurrentActualEspdMat(EspdMat espdMat) {
        IncrementState incrementStates = incrementStatesRepository.findBySubscrIdAndObjType(
                espdMat.getSubscrId(),
                IncrementStateObjType.PACKAGE
        );
        if (incrementStates != null && (
                incrementStates.getIncrementationState() == IncrementStateStatus.PACKAGE_PROCESSED_SUCCESSFULLY.status
                        || incrementStates.getIncrementationState() == IncrementStateStatus.PACKAGE_IN_PROCESS.status)
                && (incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt())
                || incrementStates.getWorkflowEndDt().isEqual(espdMat.getWorkflowEndDt()))) {
            return null;
        }
        return espdMatObjRawDataIncrementDAO.findAllByPackageSmd(espdMat.getPackageSmd());
    }

}
