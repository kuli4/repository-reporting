package pro.kuli4.repository.maprocessor.entities;

import lombok.*;
import pro.kuli4.repository.common.taxonomy.MasterAgreementConfirmationMethods;
import pro.kuli4.repository.common.taxonomy.MasterAgreementTypes;
import pro.kuli4.repository.common.taxonomy.MasterAgreementVersions;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgent;
import pro.kuli4.repository.maprocessor.entities.forms.NsdForm;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "MASTER_AGREEMENTS")
public class MasterAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MasterAgreementStatuses status;

    @ManyToOne
    @JoinColumn(name = "COUNTER_AGENT_ID")
    private CounterAgent counterAgent;

    @Column(name = "DOCUMENT_ID")
    private String documentId;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "MASTER_AGREEMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private MasterAgreementTypes type;

    @Column(name = "MASTER_AGREEMENT_VERSION")
    @Enumerated(EnumType.STRING)
    private MasterAgreementVersions version;

    @Column(name = "IS_FIRST_PARTY")
    private boolean isFirstParty;

    @Column(name = "IS_MA_UTI_GENERATOR")
    private boolean isMaUtiGenerator;

    @Column(name = "IS_DEAL_UTI_GENERATOR")
    private boolean isDealUtiGenerator;

    @Column(name = "UTI")
    private String UTI;

    @Column(name = "CONFIRMATION_METHOD")
    @Enumerated(EnumType.STRING)
    private MasterAgreementConfirmationMethods confirmationMethod;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "masterAgreement", cascade = CascadeType.ALL) // #TODO: Limit cascade and decide about EAGER
    private Set<NsdForm> nsdForms;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}
