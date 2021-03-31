package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TableService {

    private final TransferDataBetweenSchemasService transferDataService;

    @Transactional
    void processAllTablesInPackage(List<EspdMatObj> espdMatObjs) {
        espdMatObjs.forEach(transferDataService::transferDataByEspdMatObj);
    }

}
