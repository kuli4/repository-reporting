package pro.kuli4.repository.maprocessor.dto.mappers;

import org.springframework.stereotype.Component;
import pro.kuli4.repository.maprocessor.dto.OuterRequestMapper;
import pro.kuli4.repository.maprocessor.dto.beans.MasterAgreementOuterRequestDto;
import pro.kuli4.repository.maprocessor.entities.MasterAgreement;


@Component
public class MasterAgreementOuterRequestMapper implements OuterRequestMapper<MasterAgreement, MasterAgreementOuterRequestDto> {

    private final CounterAgentOuterRequestMapper counterAgentOuterRequestMapper;
    private final NsdFormOuterRequestMapper nsdFormOuterRequestMapper;

    public MasterAgreementOuterRequestMapper (
            CounterAgentOuterRequestMapper counterAgentOuterRequestMapper,
            NsdFormOuterRequestMapper nsdFormOuterRequestMapper
    ) {
        this.counterAgentOuterRequestMapper = counterAgentOuterRequestMapper;
        this.nsdFormOuterRequestMapper = nsdFormOuterRequestMapper;
    }

    @Override
    public MasterAgreementOuterRequestDto map(MasterAgreement masterAgreement) {
        return MasterAgreementOuterRequestDto.builder()
                .id(masterAgreement.getId())
                .status(masterAgreement.getStatus())
                .counterAgent(counterAgentOuterRequestMapper.map(masterAgreement.getCounterAgent()))
                .documentId(masterAgreement.getDocumentId())
                .startDate(masterAgreement.getStartDate())
                .endDate(masterAgreement.getEndDate())
                .type(masterAgreement.getType().getValue())
                .version(masterAgreement.getVersion().getValue())
                .isFirstParty(masterAgreement.isFirstParty())
                .isMaUtiGenerator(masterAgreement.isMaUtiGenerator())
                .isDealUtiGenerator(masterAgreement.isDealUtiGenerator())
                .UTI(masterAgreement.getUTI())
                .confirmationMethod(masterAgreement.getConfirmationMethod())
                .forms(nsdFormOuterRequestMapper.mapAll(masterAgreement.getNsdForms()))
                .build();
    }

}
