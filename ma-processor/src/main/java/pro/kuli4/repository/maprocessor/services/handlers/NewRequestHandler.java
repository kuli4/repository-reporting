package pro.kuli4.repository.maprocessor.services.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pro.kuli4.repository.common.helpers.StringBuildHelper;
import pro.kuli4.repository.common.taxonomy.MasterAgreementConfirmationMethods;
import pro.kuli4.repository.common.taxonomy.MasterAgreementTypes;
import pro.kuli4.repository.common.taxonomy.MasterAgreementVersions;
import pro.kuli4.repository.maprocessor.entities.MasterAgreement;
import pro.kuli4.repository.maprocessor.entities.MasterAgreementStatuses;
import pro.kuli4.repository.maprocessor.entities.ProcessRequest;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgent;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgentIdentifierTypes;
import pro.kuli4.repository.maprocessor.entities.forms.Cm10NsdForm;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormDirections;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormStatuses;
import pro.kuli4.repository.maprocessor.entities.forms.NsdFormTypes;
import pro.kuli4.repository.maprocessor.repositories.*;
import pro.kuli4.repository.maprocessor.services.forms.MasterAgreementNsdFormGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class NewRequestHandler {
    public final static String SIDE_NAME = "АКЦИОНЕРНОЕ ОБЩЕСТВО \"АЛЬФА-БАНК\"";
    public final static String TRADE_REPOSITORY_NAME = "Небанковская кредитная организация акционерное общество \"Национальный расчетный депозитарий\"";
    private final CounterAgent side;
    private final CounterAgent tradeRepository;
    private final ProcessRequestRepository processRequestRepository;
    private final CounterAgentRepository counterAgentRepository;
    private final MasterAgreementRepository masterAgreementRepository;
    private final MasterAgreementNsdFormGenerator masterAgreementNsdFormGenerator;
    private final Cm10NsdFormRepository cm10NsdFormRepository;

    public NewRequestHandler(
            ProcessRequestRepository processRequestRepository,
            CounterAgentRepository counterAgentRepository,
            MasterAgreementRepository masterAgreementRepository,
            Cm10NsdFormRepository cm10NsdFormRepository,
            MasterAgreementNsdFormGenerator masterAgreementNsdFormGenerator
    ) {
        this.processRequestRepository = processRequestRepository;
        this.counterAgentRepository = counterAgentRepository;
        this.masterAgreementRepository = masterAgreementRepository;
        this.masterAgreementNsdFormGenerator = masterAgreementNsdFormGenerator;
        this.cm10NsdFormRepository = cm10NsdFormRepository;
        this.side = counterAgentRepository.findFirstByName(SIDE_NAME).orElseThrow();
        log.debug("Side: {}", side);
        this.tradeRepository = counterAgentRepository.findFirstByName(TRADE_REPOSITORY_NAME).orElseThrow();
        log.debug("TradeRepository: {}", tradeRepository);
    }

    @Transactional
    public void handle(ProcessRequest processRequest) {
        ObjectMapper mapper = new ObjectMapper();
        processRequest = processRequestRepository.findById(processRequest.getId()).orElseThrow(() -> new RuntimeException("Oops!"));
        JsonNode message;
        try {
            message = mapper.readTree(processRequest.getPayload());
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
        JsonNode ma = message.get("ma");
        JsonNode ca = message.get("ca"); // TODO: Create ca-processor
        JsonNode tech = message.get("tech"); // TODO: Use sendNow

        // Find out a counter agent
        log.debug("Parameters for search counterAgentIdentifier: {} ::: {}", CounterAgentIdentifierTypes.valueOf(ca.get("idType").asText()),
                ca.get("id").asText());

        List<CounterAgent> counterAgents = counterAgentRepository.findFirstByCounterAgentIdentifierTypeAndCounterAgentIdentifierValue(
                CounterAgentIdentifierTypes.valueOf(ca.get("idType").asText()),
                ca.get("id").asText()
        );

        if (counterAgents.size() != 1) {
            throw new IllegalArgumentException("counterAgents.size(): " + counterAgents.size());
        }
        CounterAgent counterAgent = counterAgents.get(0);

        log.debug("CounterAgent: {}", counterAgent);

        // Create master agreement entity
        MasterAgreement masterAgreement = MasterAgreement.builder()
                .status(MasterAgreementStatuses.NEW)
                .documentId(ma.get("id").asText())
                .UTI(ma.get("uti").asText())
                .isFirstParty(ma.get("isFirstParty").asBoolean())
                .isDealUtiGenerator(ma.get("isDealUtiGenerator").asBoolean())
                .isMaUtiGenerator(ma.get("isMaUtiGenerator").asBoolean())
                .startDate(LocalDate.parse(ma.get("regDate").asText()))
                .type(MasterAgreementTypes.findByValue(ma.get("type").asText()))
                .version(MasterAgreementVersions.findByValue(ma.get("version").asText()))
                .confirmationMethod(MasterAgreementConfirmationMethods.valueOf(ma.get("confirmationMethod").asText()))
                .counterAgent(counterAgent)
                .build(); // TODO: Check to idempotency
        log.debug("MasterAgreement: {}", masterAgreement);
        masterAgreementRepository.save(masterAgreement);

        // Create CM10 NSD form for new master agreement
        Cm10NsdForm cm10NsdForm = new Cm10NsdForm();
        cm10NsdForm.setMasterAgreement(masterAgreement);
        cm10NsdForm.setType(NsdFormTypes.CM_010);
        cm10NsdForm.setDirection(NsdFormDirections.OUT);
        cm10NsdForm.setStatus(NsdFormStatuses.NEW);
        cm10NsdForm.setTradeRepository(tradeRepository);
        if (!masterAgreement.isFirstParty()) {
            cm10NsdForm.setParty1(counterAgent);
            cm10NsdForm.setParty2(side);
        } else {
            cm10NsdForm.setParty1(side);
            cm10NsdForm.setParty2(counterAgent);
        }
        if (masterAgreement.isMaUtiGenerator()) {
            cm10NsdForm.setUtiGeneratingParty(side);
        } else {
            cm10NsdForm.setUtiGeneratingParty(counterAgent);
        }
        cm10NsdForm.setSender(side);
        cm10NsdForm.setMessageId(StringBuildHelper.buildMessageId("MA", masterAgreement.getId()));
        cm10NsdForm.setSentBy(side.getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
        cm10NsdForm.setSentTo(tradeRepository.getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
        cm10NsdForm.setCreationTimestamp(LocalDateTime.now());
        cm10NsdForm.setCorrection(false);
        cm10NsdForm.setCorrelationId(StringBuildHelper.buildCorrelationId(
                side.getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue(),
                String.valueOf(LocalDate.now().getYear()),
                StringBuildHelper.buildMessageId("MA", masterAgreement.getId())));
        cm10NsdForm.setAsOfDate(LocalDate.now());
        cm10NsdForm.setPartyTradeIdentifierFromTradeRepository("NONREF");
        cm10NsdForm.setPartyTradeIdentifierFromParty1(masterAgreement.getDocumentId());
        cm10NsdForm.setPartyTradeIdentifierFromParty2(masterAgreement.getDocumentId());
        cm10NsdForm.setPartyTradeIdentifierFromUtiGeneratingParty(masterAgreement.getUTI());
        cm10NsdForm.setTradeDate(masterAgreement.getStartDate());
        cm10NsdForm.setMasterAgreementType(masterAgreement.getType());
        cm10NsdForm.setMasterAgreementVersion(masterAgreement.getVersion());
        cm10NsdForm.setConfirmationMethod(masterAgreement.getConfirmationMethod());
        cm10NsdForm.setPartiesAreAffiliated(counterAgent.isAffiliated() ? "Y" : "N");
        log.debug("Cm10NsdForm without xml: {}", cm10NsdForm);

        // Generate CM10 NSD form
        String form = masterAgreementNsdFormGenerator.generateCM010(cm10NsdForm);
        cm10NsdForm.setForm(form);
        log.debug("CM10: {}", form);
        cm10NsdFormRepository.save(cm10NsdForm);
    }
}
