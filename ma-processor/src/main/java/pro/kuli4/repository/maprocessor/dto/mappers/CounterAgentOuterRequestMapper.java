package pro.kuli4.repository.maprocessor.dto.mappers;

import org.springframework.stereotype.Component;
import pro.kuli4.repository.maprocessor.dto.OuterRequestMapper;
import pro.kuli4.repository.maprocessor.dto.beans.CounterAgentOuterRequestDto;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgent;

import java.util.stream.Collectors;

@Component
public class CounterAgentOuterRequestMapper implements OuterRequestMapper<CounterAgent, CounterAgentOuterRequestDto> {
    private final CounterAgentIdentifierOuterRequestMapper counterAgentIdentifierOuterRequestMapper;

    public CounterAgentOuterRequestMapper(
            CounterAgentIdentifierOuterRequestMapper counterAgentIdentifierOuterRequestMapper
    ) {
        this.counterAgentIdentifierOuterRequestMapper = counterAgentIdentifierOuterRequestMapper;
    }

    @Override
    public CounterAgentOuterRequestDto map(CounterAgent counterAgent) {
        return CounterAgentOuterRequestDto.builder()
                .id(counterAgent.getId())
                .identifiers(counterAgent.getCounterAgentIdentifiers().stream().map(counterAgentIdentifierOuterRequestMapper::map).collect(Collectors.toList()))
                .name(counterAgent.getName())
                .build();
    }
}
