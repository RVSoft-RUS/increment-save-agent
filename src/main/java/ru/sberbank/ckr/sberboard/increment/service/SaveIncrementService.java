package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatObjRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SaveIncrementService {
    private final EspdMatRawDataIncrementDAO espdMatDAO;
    private final EspdMatObjRawDataIncrementDAO espdMatObjDAO;
    private final IncrementStateRepository incrementStateRepository;
    private final String OBJ_TYPE_PACKAGE = "package";

    public List<String> getUniqueDescriptions() {
        return espdMatDAO.findAllUniqueSubscribeId();
    }

    public EspdMat getActualPackageToProcess(String subscrId) {
        return espdMatDAO.findActualEspdMatToProcess(subscrId);
    }

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
        espdMatDAO.findAllUniqueSubscribeId()
                .stream()
                .map(espdMatDAO::findActualEspdMatToProcess)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
//        Iterator<EspdMat> iterator = packagesToProcess.iterator();
//        while (iterator.hasNext()) {
//            EspdMat espdMat = iterator.next();
//            /////
//            IncrementStates incrementStates =
//                    incrementStatesRepository.findByPackageSmdAndObjTypeAndIncrPackRunId(
//                            espdMat.getPackageSmd(),
//                            OBJ_TYPE_PACKAGE,
//                            espdMat.getWorkflowRunId()
//                    );
//            if (incrementStates != null && incrementStates.getIncrementationState() != null && (
//                    incrementStates.getIncrementationState().equals("100")
//                            || incrementStates.getIncrementationState().equals("10"))
//                    && incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt()) ) {
//                iterator.remove();
//            }
//        }
        Map<EspdMat, List<EspdMatObj>> espdMatListMap = new HashMap<>();
        for (EspdMat espdMat: packagesToProcess) {
            espdMatListMap.put(espdMat, getEspdMatObjsForCurrentActualEspdMat(espdMat));
        }
        return espdMatListMap;
    }

    public List<EspdMatObj> getEspdMatObjsForCurrentActualEspdMat(EspdMat espdMat) {
        IncrementState incrementState = incrementStateRepository.findByPackageSmdAndObjTypeAndIncrPackRunId(
                espdMat.getPackageSmd(),
                OBJ_TYPE_PACKAGE,
                espdMat.getWorkflowRunId()
        );
        if (incrementState != null && (//todo Enum
                incrementState.getIncrementationState() == 100
                        || incrementState.getIncrementationState() == 10)
                && incrementState.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt()) ) {
            return null;
        }
        return espdMatObjDAO.findAllByPackageSmd(espdMat.getPackageSmd());
    }

}
