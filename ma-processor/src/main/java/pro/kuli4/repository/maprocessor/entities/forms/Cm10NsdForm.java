package pro.kuli4.repository.maprocessor.entities.forms;

import lombok.*;
import pro.kuli4.repository.common.taxonomy.MasterAgreementConfirmationMethods;
import pro.kuli4.repository.common.taxonomy.MasterAgreementTypes;
import pro.kuli4.repository.common.taxonomy.MasterAgreementVersions;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CM010_NSD_FORMS")
public class Cm10NsdForm extends NsdForm {

    @Column(name = "IS_CORRECTION")
    private boolean isCorrection;

    @Column(name = "CORRELATION_ID")
    private String correlationId;

    @Column(name = "AS_OF_DATE")
    private LocalDate asOfDate;

    @Column(name = "PARTY_TRADE_IDENTIFIER_TRADE_REPOSITORY")
    private String partyTradeIdentifierFromTradeRepository;

    @Column(name = "PARTY_TRADE_IDENTIFIER_PARTY_1")
    private String partyTradeIdentifierFromParty1;

    @Column(name = "PARTY_TRADE_IDENTIFIER_PARTY_2")
    private String partyTradeIdentifierFromParty2;

    @Column(name = "PARTY_TRADE_IDENTIFIER_UTI_GENERATING_PARTY")
    private String partyTradeIdentifierFromUtiGeneratingParty;

    @Column(name = "TRADE_DATE")
    private LocalDate tradeDate;

    @Column(name = "MASTER_AGREEMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private MasterAgreementTypes masterAgreementType;

    @Column(name = "MASTER_AGREEMENT_VERSION")
    @Enumerated(EnumType.STRING)
    private MasterAgreementVersions masterAgreementVersion;

    @Column(name = "CONFIRMATION_METHOD")
    @Enumerated(EnumType.STRING)
    private MasterAgreementConfirmationMethods confirmationMethod;

    @Column(name = "PARTIES_ARE_AFFILIATED")
    private String partiesAreAffiliated;

}
