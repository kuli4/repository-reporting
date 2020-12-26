package pro.kuli4.repository.maprocessor.entities.cagent;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "COUNTER_AGENT_IDENTIFIERS")
public class CounterAgentIdentifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CounterAgentIdentifierTypes type;

    @Column(name = "VALUE")
    private String value;

    @ManyToOne
    @JoinColumn(name = "COUNTER_AGENT")
    @ToString.Exclude
    private CounterAgent counterAgent;
}
