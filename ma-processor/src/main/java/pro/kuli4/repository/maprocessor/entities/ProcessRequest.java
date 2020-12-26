package pro.kuli4.repository.maprocessor.entities;

import lombok.*;
import org.hibernate.annotations.Type;
import pro.kuli4.repository.common.innermessage.MessageType;
import pro.kuli4.repository.common.innermessage.RepositorySystemComponents;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@Table(name = "PROCESS_REQUESTS")
public class ProcessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE_TYPE")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "MESSAGE_CHAIN_ID")
    private String messageChainId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ProcessRequestStatuses status;

    @Column(name = "FROM_C")
    @Enumerated(EnumType.STRING)
    private RepositorySystemComponents from;

    @Column(name = "PAYLOAD")
    @Type(type="text")
    private String payload;

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
