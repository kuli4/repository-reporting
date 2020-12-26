package pro.kuli4.repository.maprocessor.dto.beans;

import lombok.*;
import pro.kuli4.repository.common.taxonomy.MasterAgreementConfirmationMethods;
import pro.kuli4.repository.maprocessor.entities.MasterAgreementStatuses;

import java.time.LocalDate;
import java.util.Collection;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MasterAgreementOuterRequestDto {
    private long id;
    private MasterAgreementStatuses status;
    private CounterAgentOuterRequestDto counterAgent;
    private String documentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private String version;
    private Boolean isFirstParty;
    private Boolean isMaUtiGenerator;
    private Boolean isDealUtiGenerator;
    private String UTI;
    private MasterAgreementConfirmationMethods confirmationMethod;
    private Collection<NsdFormOuterRequestDto> forms;
}
