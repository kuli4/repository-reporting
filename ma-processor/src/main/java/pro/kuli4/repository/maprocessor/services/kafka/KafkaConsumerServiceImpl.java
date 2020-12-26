package pro.kuli4.repository.maprocessor.services.kafka;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.common.functional.HandlerWithException;
import pro.kuli4.repository.common.innermessage.InnerMessage;
import pro.kuli4.repository.common.kafka.KafkaConsumerService;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    public static final String TOPIC_NAME = "master-agreement-in";
    private final Consumer<String, InnerMessage> consumer;

    public KafkaConsumerServiceImpl() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092"); // TODO: Move properties to application variables
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "ma-processor");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // TODO: Use transactions for "exactly-once"

        properties.put(KafkaProtobufDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
        properties.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE, InnerMessage.class.getName());

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(TOPIC_NAME));
    }

    @Override
    public void pullAndHandle(HandlerWithException<InnerMessage> handler) {
        ConsumerRecords<String, InnerMessage> records = consumer.poll(Duration.ofMillis(10));
        if (records.count() == 0) return;
        log.info("Received {} records from topic {}", records.count(), TOPIC_NAME);
        TopicPartition partition = records.partitions().stream().findFirst().orElseThrow();
        List<ConsumerRecord<String, InnerMessage>> partitionRecords = records.records(partition);
        long offset = partitionRecords.get(0).offset();
        try {
            for (ConsumerRecord<String, InnerMessage> record : partitionRecords) {
                handler.handle(record.value());
                offset++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset)));
        }
    }

    @PreDestroy
    public void destroy() {
        consumer.close();
    }
}
