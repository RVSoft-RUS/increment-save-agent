package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatObjRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
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
    private final String OBJ_TYPE_PACKAGE = "package";

    public List<String> getUniqueDescriptions() {
        return espdMatRawDataIncrementDAO.findAllUniqueSubscribeId();
    }

    public EspdMat getActualPackageToProcess(String subscrId) {
        return espdMatRawDataIncrementDAO.findActualEspdMatToProcess(subscrId);
    }

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
        espdMatRawDataIncrementDAO.findAllUniqueSubscribeId()
                .stream()
                .map(espdMatRawDataIncrementDAO::findActualEspdMatToProcess)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<EspdMat, List<EspdMatObj>> espdMatListMap = new HashMap<>();
        for (EspdMat espdMat: packagesToProcess) {
            espdMatListMap.put(espdMat, getEspdMatObjsForCurrentActualEspdMat(espdMat));
        }
        return espdMatListMap;
    }

    public List<EspdMatObj> getEspdMatObjsForCurrentActualEspdMat(EspdMat espdMat) {
        IncrementState incrementStates = incrementStatesRepository.findByPackageSmdAndObjTypeAndIncrPackRunId(
                espdMat.getPackageSmd(),
                OBJ_TYPE_PACKAGE,
                espdMat.getWorkflowRunId()
        );
        if (incrementStates != null && (//todo Enum
                incrementStates.getIncrementationState() == 100
                        || incrementStates.getIncrementationState() == 10)
                && incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt()) ) {
            return null;
        }
        return espdMatObjRawDataIncrementDAO.findAllByPackageSmd(espdMat.getPackageSmd());
    }

}
