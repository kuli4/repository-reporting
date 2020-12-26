package pro.kuli4.repository.maprocessor.dto.beans;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CounterAgentOuterRequestDto {
    private long id;
    private List<CounterAgentIdentifierOuterRequestDto> identifiers;
    private String name;
}
