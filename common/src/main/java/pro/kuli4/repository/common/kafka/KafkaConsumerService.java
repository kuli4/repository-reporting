package pro.kuli4.repository.common.kafka;

import pro.kuli4.repository.common.functional.HandlerWithException;
import pro.kuli4.repository.common.innermessage.InnerMessage;

public interface KafkaConsumerService {
    void pullAndHandle(HandlerWithException<InnerMessage> handler);
}
