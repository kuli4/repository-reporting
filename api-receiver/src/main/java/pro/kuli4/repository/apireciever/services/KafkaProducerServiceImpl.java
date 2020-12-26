package pro.kuli4.repository.apireciever.services;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.common.innermessage.InnerMessage;
import pro.kuli4.repository.common.kafka.KafkaProducerService;

import javax.annotation.PreDestroy;
import java.util.Properties;

@Component
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    public static final String TOPIC_NAME = "master-agreement-in";
    private final Producer<String, InnerMessage> producer;

    public KafkaProducerServiceImpl() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092"); // TODO: Move to application variables
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProtobufSerializer.class);
        properties.put(KafkaProtobufSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void push(InnerMessage message) {
        log.debug("Push message to {} topic: {}", TOPIC_NAME, message.toString()); // TODO: Adjust log flow
        ProducerRecord<String, InnerMessage> record = new ProducerRecord<>(TOPIC_NAME, null, message);
        producer.send(record);
        producer.flush();
    }

    @PreDestroy
    public void destroy() {
        producer.close();
    }

}
