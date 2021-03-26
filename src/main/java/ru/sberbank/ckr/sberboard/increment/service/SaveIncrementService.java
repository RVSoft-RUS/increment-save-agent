package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;
import ru.sberbank.ckr.sberboard.increment.repository.EspdMatObjRepository;
import ru.sberbank.ckr.sberboard.increment.repository.EspdMatRepository;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStatesRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SaveIncrementService {
    private final EspdMatRepository espdMatRepository;
    private final EspdMatObjRepository espdMatObjRepository;
    private final IncrementStatesRepository incrementStatesRepository;
    private final String OBJ_TYPE_PACKAGE = "package";

    public List<String> getUniqueDescriptions() {
        return espdMatRepository.findAllUniqueSubscribeId();
    }

    public EspdMat getActualPackageToProcess(String subscrId) {
        return espdMatRepository.findActualEspdMatToProcess(subscrId);
    }

    public Map<EspdMat, List<EspdMatObj>> getEspdMatObjsForAllActualEspdMat() {
        List<EspdMat> packagesToProcess =
        espdMatRepository.findAllUniqueSubscribeId()
                .stream()
                .map(espdMatRepository::findActualEspdMatToProcess)
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
        IncrementStates incrementStates = incrementStatesRepository.findByPackageSmdAndObjTypeAndIncrPackRunId(
                espdMat.getPackageSmd(),
                OBJ_TYPE_PACKAGE,
                espdMat.getWorkflowRunId()
        );
        if (incrementStates != null && (//todo Enum
                incrementStates.getIncrementationState() == 100
                        || incrementStates.getIncrementationState() == 10)
                && incrementStates.getWorkflowEndDt().isAfter(espdMat.getWorkflowEndDt()) )
        {
            return null;
            }
        return espdMatObjRepository.findAllByPackageSmd(espdMat.getPackageSmd());
    }

}
