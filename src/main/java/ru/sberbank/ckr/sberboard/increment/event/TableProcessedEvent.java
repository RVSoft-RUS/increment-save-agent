package ru.sberbank.ckr.sberboard.increment.event;

import lombok.Data;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;

@Data
public class TableProcessedEvent {

    private final EspdMatObj espdMatObj;

    public TableProcessedEvent(EspdMatObj espdMatObj) {
        this.espdMatObj = espdMatObj;
    }
}
