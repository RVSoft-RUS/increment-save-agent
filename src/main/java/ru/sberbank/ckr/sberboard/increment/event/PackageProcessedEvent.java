package ru.sberbank.ckr.sberboard.increment.event;

import lombok.Data;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;

@Data
public class PackageProcessedEvent {

    private final IncrementState incrementState;

    public PackageProcessedEvent(IncrementState incrementState) {
        this.incrementState = incrementState;
    }

}
