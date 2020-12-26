package pro.kuli4.repository.maprocessor.services.forms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.kuli4.repository.common.formats.*;
import pro.kuli4.repository.common.helpers.StringBuildHelper;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgentIdentifierTypes;
import pro.kuli4.repository.maprocessor.entities.forms.Cm10NsdForm;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MasterAgreementNsdFormGeneratorImpl implements MasterAgreementNsdFormGenerator {

    @Override
    public String generateCM010(Cm10NsdForm entityOfForm) {
        try {
            JAXBContext jc = JAXBContext.newInstance("pro.kuli4.repository.common.formats");

            ObjectFactory objectFactory = new ObjectFactory();

            // Create a wrapper tag
            NonpublicExecutionReport nonpublicExecutionReport = objectFactory.createNonpublicExecutionReport();
            nonpublicExecutionReport.setFpmlVersion("5-4");
            nonpublicExecutionReport.setActualBuild(new BigInteger("5"));

            // Create a message header
            nonpublicExecutionReport.setHeader(constructMessageHeader(objectFactory, entityOfForm));

            // Is it correction?
            nonpublicExecutionReport.setIsCorrection(false);

            // Set a correlation identifier
            CorrelationId correlationId = objectFactory.createCorrelationId();
            correlationId.setCorrelationIdScheme("http://repository.nsd.ru/coding-scheme/correlationId(nsdrus)");
            correlationId.setValue(entityOfForm.getCorrelationId());
            nonpublicExecutionReport.getCorrelationId().add(correlationId);

            // Set asOfDate
            DatatypeFactory datatypeFactory = DatatypeFactory.newDefaultInstance();
            String asOfDate = entityOfForm.getAsOfDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(asOfDate);
            IdentifiedDate identifiedDateAsOfDate = objectFactory.createIdentifiedDate();
            identifiedDateAsOfDate.setValue(xmlGregorianCalendar);
            nonpublicExecutionReport.setAsOfDate(identifiedDateAsOfDate);

            // Generate parties
            Map<String, Party> parties = new HashMap<>();
            // Trade repository party
            Party traderRepository = objectFactory.createParty();
            traderRepository.setId("TradeRepository");
            PartyId partyIdRPZR = objectFactory.createPartyId();
            partyIdRPZR.setValue(entityOfForm.getTradeRepository().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
            traderRepository.getPartyId().add(partyIdRPZR);
            PartyId partyIdLEI = objectFactory.createPartyId();
            partyIdLEI.setValue(StringBuildHelper.buildPartyId(CounterAgentIdentifierTypes.LEI.toString(), entityOfForm.getTradeRepository().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.LEI)).findFirst().orElseThrow().getValue()));
            traderRepository.getPartyId().add(partyIdLEI);
            PartyName partyName = objectFactory.createPartyName();
            partyName.setValue(entityOfForm.getTradeRepository().getName());
            traderRepository.setPartyName(partyName);

            parties.put("TradeRepository", traderRepository);
            nonpublicExecutionReport.getParty().add(traderRepository);

            // Uti generating party
            Party utiGeneratingParty = objectFactory.createParty();
            utiGeneratingParty.setId("UTIGeneratingParty");

            PartyId utiPartyIdRPZR = objectFactory.createPartyId();
            utiPartyIdRPZR.setValue(entityOfForm.getUtiGeneratingParty().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
            utiGeneratingParty.getPartyId().add(utiPartyIdRPZR);

            PartyId utiPartyIdLEI = objectFactory.createPartyId();
            utiPartyIdLEI.setValue(StringBuildHelper.buildPartyId(CounterAgentIdentifierTypes.LEI.toString(), entityOfForm.getUtiGeneratingParty().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.LEI)).findFirst().orElseThrow().getValue()));
            utiGeneratingParty.getPartyId().add(utiPartyIdLEI);

            PartyName utiPartyName = objectFactory.createPartyName();
            utiPartyName.setValue(entityOfForm.getUtiGeneratingParty().getName());
            utiGeneratingParty.setPartyName(utiPartyName);

            parties.put("UTIGeneratingParty", utiGeneratingParty);
            nonpublicExecutionReport.getParty().add(utiGeneratingParty);

            // Party 1
            Party party1 = objectFactory.createParty();
            party1.setId("Party1");

            PartyId party1IdRPZR = objectFactory.createPartyId(); // TODO: Check type of party1
            party1IdRPZR.setValue(entityOfForm.getParty1().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
            party1.getPartyId().add(party1IdRPZR);

            PartyId party1IdLEI = objectFactory.createPartyId(); // TODO: Check type of party1
            log.debug("================================");
            log.debug("Party1 identifiers: {}", entityOfForm.getParty1().getCounterAgentIdentifiers());
            log.debug("================================");
            party1IdLEI.setValue(StringBuildHelper.buildPartyId(CounterAgentIdentifierTypes.LEI.name(), entityOfForm.getParty1().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.LEI)).findFirst().orElseThrow().getValue()));
            party1.getPartyId().add(party1IdLEI);

            PartyName party1Name = objectFactory.createPartyName();
            party1Name.setValue(entityOfForm.getParty1().getName());
            party1.setPartyName(party1Name);

            IndustryClassification party1classification = objectFactory.createIndustryClassification();
            party1classification.setIndustryClassificationScheme("http://www.fpml.org/coding-scheme/external/north-american-industry-classification-system");
            party1classification.setValue(entityOfForm.getParty1().getClassification().name());
            party1.getClassification().add(party1classification);

            CountryCode party1CountryCode = objectFactory.createCountryCode();
            party1CountryCode.setCountryScheme("http://www.fpml.org/ext/iso3166");
            party1CountryCode.setValue(entityOfForm.getParty1().getCountryCode().name());
            party1.setCountry(party1CountryCode);

            OrganizationType party1organizationType = objectFactory.createOrganizationType();
            party1organizationType.setOrganizationTypeScheme("http://www.fpml.org/coding-scheme/organization-type");
            party1organizationType.setValue(entityOfForm.getParty1().getOrganizationType().name());
            party1.setOrganizationType(party1organizationType);

            parties.put("Party1", party1);
            nonpublicExecutionReport.getParty().add(party1);

            // Party 2
            Party party2 = objectFactory.createParty();
            party2.setId("Party2");

            PartyId party2IdRPZR = objectFactory.createPartyId(); // TODO: Check type of party1
            party2IdRPZR.setValue(entityOfForm.getParty2().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
            party2.getPartyId().add(party2IdRPZR);

            PartyId party2IdLEI = objectFactory.createPartyId(); // TODO: Check type of party1
            party2IdLEI.setValue(StringBuildHelper.buildPartyId(CounterAgentIdentifierTypes.LEI.name(), entityOfForm.getParty2().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.LEI)).findFirst().orElseThrow().getValue()));
            party2.getPartyId().add(party2IdLEI);

            PartyName party2Name = objectFactory.createPartyName();
            party2Name.setValue(entityOfForm.getParty2().getName());
            party2.setPartyName(party2Name);

            IndustryClassification party2classification = objectFactory.createIndustryClassification();
            party2classification.setIndustryClassificationScheme("http://www.fpml.org/coding-scheme/external/north-american-industry-classification-system");
            party2classification.setValue(entityOfForm.getParty2().getClassification().name());
            party2.getClassification().add(party2classification);

            CountryCode party2CountryCode = objectFactory.createCountryCode();
            party2CountryCode.setCountryScheme("http://www.fpml.org/ext/iso3166");
            party2CountryCode.setValue(entityOfForm.getParty2().getCountryCode().name());
            party2.setCountry(party2CountryCode);

            OrganizationType party2organizationType = objectFactory.createOrganizationType();
            party2organizationType.setOrganizationTypeScheme("http://www.fpml.org/coding-scheme/organization-type");
            party2organizationType.setValue(entityOfForm.getParty2().getOrganizationType().name());
            party2.setOrganizationType(party2organizationType);

            parties.put("Party2", party2);
            nonpublicExecutionReport.getParty().add(party2);

            // Sender party
            Party senderParty = objectFactory.createParty();
            senderParty.setId("Sender");

            PartyId senderPartyIdRPZR = objectFactory.createPartyId();
            senderPartyIdRPZR.setValue(entityOfForm.getSender().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
            senderParty.getPartyId().add(senderPartyIdRPZR);

            PartyId senderPartyIdLEI = objectFactory.createPartyId();
            senderPartyIdLEI.setValue(StringBuildHelper.buildPartyId(CounterAgentIdentifierTypes.LEI.toString(), entityOfForm.getSender().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.LEI)).findFirst().orElseThrow().getValue()));
            senderParty.getPartyId().add(senderPartyIdLEI);

            PartyName senderPartyName = objectFactory.createPartyName();
            senderPartyName.setValue(entityOfForm.getSender().getName());
            senderParty.setPartyName(senderPartyName);

            parties.put("Sender", senderParty);
            nonpublicExecutionReport.getParty().add(senderParty);

            // Generate trade
            nonpublicExecutionReport.setTrade(constructTrade(objectFactory, entityOfForm, parties));

            // Generate XML
            JAXBElement<NonpublicExecutionReport> nonpublicExecutionReportWrapper = objectFactory.createNonpublicExecutionReport(nonpublicExecutionReport);

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(nonpublicExecutionReportWrapper, stringWriter);

            return stringWriter.toString();

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private RequestMessageHeader constructMessageHeader(
            ObjectFactory objectFactory,
            Cm10NsdForm entityOfForm
    ) {
        RequestMessageHeader messageHeader = objectFactory.createRequestMessageHeader();

        // Add messageId
        MessageId messageId = objectFactory.createMessageId();
        messageId.setMessageIdScheme("http://repository.nsd.ru/coding-scheme/messageid(nsdrus)");
        messageId.setValue(entityOfForm.getMessageId());
        messageHeader.setMessageId(messageId);

        // Add sentBy
        MessageAddress messageAddressSentBy = objectFactory.createMessageAddress();
        messageAddressSentBy.setValue(entityOfForm.getSender().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
        messageHeader.setSentBy(messageAddressSentBy);

        // Add sentTo
        MessageAddress messageAddressSendTo = objectFactory.createMessageAddress();
        messageAddressSendTo.setValue(entityOfForm.getTradeRepository().getCounterAgentIdentifiers().stream().filter((id) -> id.getType().equals(CounterAgentIdentifierTypes.RPZR)).findFirst().orElseThrow().getValue());
        messageHeader.getSendTo().add(messageAddressSendTo);

        // Add creationTimestamp
        DatatypeFactory datatypeFactory = DatatypeFactory.newDefaultInstance();
        String nowDateTime = entityOfForm.getCreationTimestamp().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(nowDateTime);
        messageHeader.setCreationTimestamp(xmlGregorianCalendar);

        // Add implementationSpecification
        ImplementationSpecification implementationSpecification = objectFactory.createImplementationSpecification();
        ImplementationSpecificationVersion isv = objectFactory.createImplementationSpecificationVersion();
        isv.setValue("4.4");
        implementationSpecification.setVersion(isv);
        messageHeader.setImplementationSpecification(implementationSpecification);

        return messageHeader;
    }

    private Trade constructTrade(
            ObjectFactory objectFactory,
            Cm10NsdForm entityOfForm,
            Map<String, Party> parties
    ) {
        Trade trade = objectFactory.createTradeNsd();

        TradeHeader tradeHeader = objectFactory.createTradeHeader();

        // Create TradeRepository partyTradeIdentifier
        PartyTradeIdentifier tradeRepositoryPartyTradeIdentifier = objectFactory.createPartyTradeIdentifier();

        PartyReference tradeRepositoryPartyReference = objectFactory.createPartyReference();
        tradeRepositoryPartyReference.setHref(parties.get("TradeRepository"));
        tradeRepositoryPartyTradeIdentifier.setPartyReference(tradeRepositoryPartyReference);

        TradeId tradeRepositoryTradeId = objectFactory.createTradeId();
        tradeRepositoryTradeId.setValue("NONREF");
        tradeRepositoryPartyTradeIdentifier.getTradeIdOrVersionedTradeId().add(tradeRepositoryTradeId);

        tradeHeader.getPartyTradeIdentifier().add(tradeRepositoryPartyTradeIdentifier);

        // Create Party1 partyTradeIdentifier
        PartyTradeIdentifier Party1PartyTradeIdentifier = objectFactory.createPartyTradeIdentifier();

        PartyReference Party1PartyReference = objectFactory.createPartyReference();
        Party1PartyReference.setHref(parties.get("Party1"));
        Party1PartyTradeIdentifier.setPartyReference(Party1PartyReference);

        TradeId Party1TradeId = objectFactory.createTradeId();
        Party1TradeId.setValue(entityOfForm.getPartyTradeIdentifierFromParty1());
        Party1PartyTradeIdentifier.getTradeIdOrVersionedTradeId().add(Party1TradeId);

        tradeHeader.getPartyTradeIdentifier().add(Party1PartyTradeIdentifier);

        // Create Party2 partyTradeIdentifier
        PartyTradeIdentifier Party2PartyTradeIdentifier = objectFactory.createPartyTradeIdentifier();

        PartyReference Party2PartyReference = objectFactory.createPartyReference();
        Party2PartyReference.setHref(parties.get("Party2"));
        Party2PartyTradeIdentifier.setPartyReference(Party2PartyReference);

        TradeId Party2TradeId = objectFactory.createTradeId();
        Party2TradeId.setValue(entityOfForm.getPartyTradeIdentifierFromParty2());
        Party2PartyTradeIdentifier.getTradeIdOrVersionedTradeId().add(Party2TradeId);

        tradeHeader.getPartyTradeIdentifier().add(Party2PartyTradeIdentifier);

        // Create utiGeneratingParty partyTradeIdentifier
        PartyTradeIdentifier utiGeneratingPartyTradeIdentifier = objectFactory.createPartyTradeIdentifier();

        PartyReference utiGeneratingPartyReference = objectFactory.createPartyReference();
        utiGeneratingPartyReference.setHref(parties.get("UTIGeneratingParty"));
        utiGeneratingPartyTradeIdentifier.setPartyReference(utiGeneratingPartyReference);

        TradeId utiGeneratingPartyTradeId = objectFactory.createTradeId();
        utiGeneratingPartyTradeId.setValue(entityOfForm.getPartyTradeIdentifierFromUtiGeneratingParty());
        utiGeneratingPartyTradeIdentifier.getTradeIdOrVersionedTradeId().add(utiGeneratingPartyTradeId);

        tradeHeader.getPartyTradeIdentifier().add(utiGeneratingPartyTradeIdentifier);

        // Add tradeInformation block
        PartyTradeInformation partyTradeInformation = objectFactory.createPartyTradeInformation();

        PartyReference partyReference = objectFactory.createPartyReference();
        partyReference.setHref(parties.get("TradeRepository"));
        partyTradeInformation.setPartyReference(partyReference);

        ReportingRegime reportingRegime = objectFactory.createReportingRegime();
        ReportingRegimeName reportingRegimeName = objectFactory.createReportingRegimeName();
        reportingRegimeName.setReportingRegimeNameScheme("http://www.fpml.org/coding-scheme/reporting-regime");
        reportingRegimeName.setValue("RussianFederation");
        JAXBElement<ReportingRegimeName> reportingRegimeNameJAXBElement = objectFactory.createReportingRegimeName(reportingRegimeName);
        reportingRegime.getContent().add(reportingRegimeNameJAXBElement);
        partyTradeInformation.getReportingRegime().add(reportingRegime);

        tradeHeader.getPartyTradeInformation().add(partyTradeInformation);

        // Set tradeDate
        DatatypeFactory datatypeFactory = DatatypeFactory.newDefaultInstance();
        String tradeDate = entityOfForm.getTradeDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(tradeDate);
        IdentifiedDate identifiedDateTradeDate = objectFactory.createIdentifiedDate();
        identifiedDateTradeDate.setValue(xmlGregorianCalendar);
        tradeHeader.setTradeDate(identifiedDateTradeDate);

        // Set generated tradeHeader to trade
        trade.setTradeHeader(tradeHeader);

        // Create masterAgreementTerms
        MasterAgreementTerms masterAgreementTerms = objectFactory.createMasterAgreementTerms();

        MasterAgreementType masterAgreementType = objectFactory.createMasterAgreementType();
        masterAgreementType.setMasterAgreementTypeScheme("http://www.fpml.org/coding-scheme/master-agreement-type");
        masterAgreementType.setValue(entityOfForm.getMasterAgreementType().getValue());
        masterAgreementTerms.setMasterAgreementType(masterAgreementType);

        MasterAgreementVersion masterAgreementVersion = objectFactory.createMasterAgreementVersion();
        masterAgreementVersion.setMasterAgreementVersionScheme("http://www.fpml.org/coding-scheme/master-agreement-version");
        masterAgreementVersion.setValue(entityOfForm.getMasterAgreementVersion().getValue());
        masterAgreementTerms.setMasterAgreementVersion(masterAgreementVersion);

        masterAgreementTerms.setConfirmationMethod(MasterAgreementConfirmationEnum.valueOf(entityOfForm.getConfirmationMethod().name()));

        masterAgreementTerms.setPartiesAreAffiliated(YorN.valueOf(entityOfForm.getPartiesAreAffiliated()));

        trade.setProduct(objectFactory.createMasterAgreementTerms(masterAgreementTerms));

        return trade;
    }

}
