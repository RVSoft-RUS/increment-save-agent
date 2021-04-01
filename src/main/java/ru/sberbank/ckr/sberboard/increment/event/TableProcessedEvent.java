package ru.sberbank.ckr.sberboard.increment.event;

import lombok.Data;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;

@Data
public class TableProcessedEvent {

    private final IncrementState incrementState;

    public TableProcessedEvent(IncrementState incrementState) {
        this.incrementState = incrementState;
    }
}
