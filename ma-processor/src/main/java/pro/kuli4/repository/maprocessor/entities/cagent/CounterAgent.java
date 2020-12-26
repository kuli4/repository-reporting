package pro.kuli4.repository.maprocessor.entities.cagent;

import lombok.*;
import pro.kuli4.repository.common.taxonomy.CounterAgentClassification;
import pro.kuli4.repository.common.taxonomy.CountryCodes;
import pro.kuli4.repository.common.taxonomy.OrganizationTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "COUNTER_AGENTS")
public class CounterAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToMany(mappedBy = "counterAgent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CounterAgentIdentifier> counterAgentIdentifiers;

    @Column(name = "NAME")
    private String name;

    @Column(name = "COUNTRY_CODE")
    @Enumerated(EnumType.STRING)
    private CountryCodes countryCode;

    @Column(name = "CLASSIFICATION")
    @Enumerated(EnumType.STRING)
    private CounterAgentClassification classification;

    @Column(name = "ORGANIZATION_TYPE")
    @Enumerated(EnumType.STRING)
    private OrganizationTypes organizationType;

    @Column(name = "IS_AFFILIATED")
    private boolean isAffiliated;

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
