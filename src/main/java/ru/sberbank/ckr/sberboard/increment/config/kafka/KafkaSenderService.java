package ru.sberbank.ckr.sberboard.increment.config.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public abstract class KafkaSenderService  {

    private static final Logger logger = LogManager.getLogger("KafkaSenderService");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaSettings kafkaSettings;

    @Transactional(value = "kafkaTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public void send(String message, String subTypeId) {
        String topicName = kafkaSettings.getTopicName();
        String modeId = kafkaSettings.getModeId();
        kafkaTemplate.send(topicName, getJsonString(getEvent(message, subTypeId, modeId)));
    }

    protected abstract KafkaEvent getEvent(String message, String subTypeId, String modeId);

    protected KafkaEvent getEvent(String typeId, String message, String subTypeId, String modeId){
        String appId = kafkaSettings.getAppId();
        KafkaEvent kafkaEvent = new KafkaEvent();
        kafkaEvent.setAppId(appId);
        kafkaEvent.setTimeStamp(new Date().getTime());
        kafkaEvent.setTypeId(typeId);
        kafkaEvent.setMessage(message);
        kafkaEvent.setSubTypeId(subTypeId);
        kafkaEvent.setModeId(modeId);
        return kafkaEvent;
    }

    protected String getJsonString(KafkaEvent kafkaEvent){
        ObjectMapper mapper = new ObjectMapper();
        String dtoAsString = null;
        try {
            dtoAsString = mapper.writer().writeValueAsString(kafkaEvent);
            logger.info(dtoAsString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dtoAsString;
    }
}
