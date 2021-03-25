package ru.sberbank.ckr.sberboard.increment.service;

import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;

@Service
public interface TransferDataBetweenSchemasService {

    void transferDataByEspdMatObj(EspdMatObj espdMatObj);

}
