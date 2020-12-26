package pro.kuli4.repository.maprocessor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.common.innermessage.InnerMessage;
import pro.kuli4.repository.common.kafka.KafkaConsumerService;
import pro.kuli4.repository.maprocessor.entities.*;
import pro.kuli4.repository.maprocessor.repositories.ProcessRequestRepository;


@Component
@Slf4j
public class InnerRequestService {
    private final KafkaConsumerService kafkaConsumerService;
    private final ProcessRequestRepository processRequestRepository;

    public InnerRequestService(
            KafkaConsumerService kafkaConsumerService,
            ProcessRequestRepository processRequestRepository) {
        this.kafkaConsumerService = kafkaConsumerService;
        this.processRequestRepository = processRequestRepository;
    }

    public void handle(InnerMessage message) throws Exception {
            String payload = message.getPayload().toString("UTF-8");
            ProcessRequest processRequest = ProcessRequest.builder()
                    .messageType(message.getType())
                    .messageChainId(message.getMessageChainId())
                    .status(ProcessRequestStatuses.NEW)
                    .from(message.getFrom())
                    .payload(payload)
                    .build();
            processRequestRepository.save(processRequest);
    }

    @Scheduled(fixedDelay = 5000) // TODO: Move to application.yml
    public void getMessage() {
        kafkaConsumerService.pullAndHandle(this::handle);
    }
}
