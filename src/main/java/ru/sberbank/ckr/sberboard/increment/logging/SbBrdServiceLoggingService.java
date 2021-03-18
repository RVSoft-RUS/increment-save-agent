package ru.sberbank.ckr.sberboard.increment.logging;

import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.config.kafka.KafkaEvent;
import ru.sberbank.ckr.sberboard.increment.config.kafka.KafkaSenderService;

@Service
public class SbBrdServiceLoggingService extends KafkaSenderService {

    protected KafkaEvent getEvent(String message, String subTypeId, String modeId) {
        return getEvent("Tech", message, subTypeId, modeId);
    }
}
