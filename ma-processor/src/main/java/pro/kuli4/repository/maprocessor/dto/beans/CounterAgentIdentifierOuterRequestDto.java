package pro.kuli4.repository.maprocessor.dto.beans;

import lombok.*;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgentIdentifierTypes;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CounterAgentIdentifierOuterRequestDto {
    private long id;
    private CounterAgentIdentifierTypes type;
    private String value;
}
