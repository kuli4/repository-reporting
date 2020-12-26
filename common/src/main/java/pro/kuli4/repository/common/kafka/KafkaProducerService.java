package pro.kuli4.repository.common.kafka;

import pro.kuli4.repository.common.innermessage.InnerMessage;

public interface KafkaProducerService {
    void push(InnerMessage message);
}
