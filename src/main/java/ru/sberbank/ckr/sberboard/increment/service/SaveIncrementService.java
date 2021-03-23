package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.repository.EspdMatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SaveIncrementService {
    private final EspdMatRepository espdMatRepository;

    public List<String> getUniqueDescriptions() {
        return espdMatRepository.findAllUniqueSubscribeId();
    }

    public String getActualPackageToProcess(String subscrId) {
        return espdMatRepository.getActualPackageToProcess(subscrId);
    }

    public List<String> getTableNamesToProcess() {
        List<String> packagesToProcess = new ArrayList<>();
        espdMatRepository.findAllUniqueSubscribeId()
                .stream()
                .map(espdMatRepository::getActualPackageToProcess)
                .forEach(pack -> {
                    if (pack != null) {
                        packagesToProcess.add(pack);}
                });
        /*
        Удалить из списка пакеты, у которых в табл increment_states есть запись
        с именем пакета, существующим в списке после п.2, если у него
        (Incrementation_state = "100" или = NULL) и дата workflow_end_dt в increment_states больше или равна
         */
        return packagesToProcess;
    }
}
