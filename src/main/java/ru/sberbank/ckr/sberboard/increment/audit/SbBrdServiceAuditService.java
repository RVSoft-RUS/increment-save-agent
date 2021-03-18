package ru.sberbank.ckr.sberboard.increment.audit;

import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.config.kafka.KafkaEvent;
import ru.sberbank.ckr.sberboard.increment.config.kafka.KafkaSenderService;

@Service
public class SbBrdServiceAuditService extends KafkaSenderService {

    protected KafkaEvent getEvent(String message, String subTypeId, String modeId) {
        return getEvent("Audit", message, subTypeId, modeId);
    }
}
