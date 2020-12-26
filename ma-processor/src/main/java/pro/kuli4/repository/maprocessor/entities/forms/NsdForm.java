package pro.kuli4.repository.maprocessor.entities.forms;

import lombok.*;
import org.hibernate.annotations.Type;
import pro.kuli4.repository.maprocessor.entities.MasterAgreement;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgent;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "NSD_FORMS")
public class NsdForm {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE_ID")
    private String messageId;

    @Column(name = "SENT_BY")
    private String sentBy;

    @Column(name = "SEND_TO")
    private String sentTo;

    @Column(name = "CREATION_TIMESTAMP")
    private LocalDateTime creationTimestamp;

    @ManyToOne
    @JoinColumn(name = "MASTER_AGREEMENT")
    private MasterAgreement masterAgreement;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private NsdFormTypes type;

    @Column(name = "DIRECTION")
    @Enumerated(EnumType.STRING)
    private NsdFormDirections direction;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private NsdFormStatuses status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRADE_REPOSITORY")
    private CounterAgent tradeRepository;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_1")
    private CounterAgent party1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTY_2")
    private CounterAgent party2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UTI_GENERATING_PARTY")
    private CounterAgent utiGeneratingParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER")
    private CounterAgent sender;

    @Column(name = "FORM")
    @Type(type="text")
    private String form;

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
