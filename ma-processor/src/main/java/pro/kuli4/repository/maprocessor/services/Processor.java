package pro.kuli4.repository.maprocessor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.maprocessor.entities.*;
import pro.kuli4.repository.maprocessor.repositories.*;
import pro.kuli4.repository.maprocessor.services.handlers.NewRequestHandler;

import java.util.List;

@Component
@Slf4j
public class Processor { // TODO: Change a pattern to the "Lifecycle with handlers" one
    private final ProcessRequestRepository processRequestRepository;
    private final NewRequestHandler newRequestHandler;


    public Processor(
            ProcessRequestRepository processRequestRepository,
            NewRequestHandler newRequestHandler
    ) {
        this.processRequestRepository = processRequestRepository;
        this.newRequestHandler = newRequestHandler;
    }

    @Scheduled(fixedDelay = 10000)
    public void processAll() {
        List<ProcessRequest> processRequests = processRequestRepository.findAllByStatusOrderByCreatedAt(ProcessRequestStatuses.NEW);
        if (!processRequests.isEmpty()) {
            for (ProcessRequest processRequest : processRequests) {
                this.process(processRequest);
            }
        }
    }

    public void process(ProcessRequest processRequest) {
        try {
            switch (processRequest.getMessageType()) {
                case MA_NEW:
                    newRequestHandler.handle(processRequest);
                    break;
                case MA_NSD_RESPONSE:
                    break;
            }
            processRequest.setStatus(ProcessRequestStatuses.PROCESSED);
            processRequestRepository.save(processRequest);
        } catch (RuntimeException re) {
            re.printStackTrace();
            processRequest.setStatus(ProcessRequestStatuses.ERROR);
            processRequestRepository.save(processRequest);
        }
    }

}
