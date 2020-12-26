package pro.kuli4.repository.maprocessor.dto.mappers;

import org.springframework.stereotype.Component;
import pro.kuli4.repository.maprocessor.dto.OuterRequestMapper;
import pro.kuli4.repository.maprocessor.dto.beans.CounterAgentIdentifierOuterRequestDto;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgentIdentifier;


@Component
public class CounterAgentIdentifierOuterRequestMapper implements OuterRequestMapper<CounterAgentIdentifier, CounterAgentIdentifierOuterRequestDto> {
    @Override
    public CounterAgentIdentifierOuterRequestDto map(CounterAgentIdentifier identifier) {
        return CounterAgentIdentifierOuterRequestDto.builder()
                .id(identifier.getId())
                .type(identifier.getType())
                .value(identifier.getValue())
                .build();
    }
}
