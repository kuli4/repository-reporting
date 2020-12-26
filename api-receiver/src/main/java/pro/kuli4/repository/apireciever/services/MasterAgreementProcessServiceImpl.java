package pro.kuli4.repository.apireciever.services;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pro.kuli4.repository.apireciever.controllers.ReceiverProcessException;
import pro.kuli4.repository.common.innermessage.InnerMessage;
import pro.kuli4.repository.common.innermessage.RepositorySystemComponents;
import pro.kuli4.repository.common.kafka.KafkaProducerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class MasterAgreementProcessServiceImpl implements MasterAgreementProcessService {

    private final KafkaProducerService kafkaProducerService;

    public MasterAgreementProcessServiceImpl(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public ResponseEntity<String> handle(String body) throws Exception {
        try {
            ObjectMapper mapper;
            JsonNode request;

            // Create Json tree
            try {
                mapper = new ObjectMapper();
                request = mapper.readTree(body);
            } catch (JsonProcessingException jpe) {
                throw new ReceiverProcessException(jpe);
            }

            // Parse "header" json object
            JsonNode requestHeader = request.get("header");
            if (requestHeader == null)
                throw new ReceiverProcessException("Expected \"header\" token in massage");
            log.debug("\"header\" json object from HTTP request: {}", requestHeader.toPrettyString());
            String sourceSystemStr = requestHeader.get("sourceSystem").asText();
            SourceSystems sourceSystem;
            try {
                sourceSystem = SourceSystems.valueOf(sourceSystemStr);
            } catch (IllegalArgumentException iae) {
                throw new ReceiverProcessException("Source system \"" + sourceSystemStr + "\" not found");
            }
            String messageId = requestHeader.get("messageId").asText();

            // Parse "body" json object
            JsonNode requestBody = request.get("body");
            if (requestBody == null) throw new ReceiverProcessException("Expected \"body\" token in massage");
            log.debug("\"body\" json object from HTTP request: {}", requestBody.toPrettyString());

            // Create message for ma-processor
            InnerMessage processMessage = InnerMessage
                    .newBuilder()
                    .setId(sourceSystem + messageId)
                    .setFrom(RepositorySystemComponents.API_COMPONENT)
                    .setTo(RepositorySystemComponents.MASTER_AGREEMENT_COMPONENT)
                    .setDatetime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .setPayload(ByteString.copyFrom(mapper.writeValueAsBytes(requestBody)))
                    .build();

            // Put message to kafka
            kafkaProducerService.push(processMessage);
        } catch (ReceiverProcessException mae) {
            mae.printStackTrace();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();

            if (mae.getCause() != null && mae.getCause() instanceof JsonProcessingException) {
                JsonProcessingException jpe = ((JsonProcessingException) mae.getCause());
                JsonLocation location = jpe.getLocation();
                jpe.clearLocation();
                response.put("errorReason", jpe.getMessage() + " at line: " + location.getLineNr() + ", column: " + location.getColumnNr());
            } else {
                response.put("errorReason", mae.getMessage());
            }
            return new ResponseEntity<>(mapper.writeValueAsString(response), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
