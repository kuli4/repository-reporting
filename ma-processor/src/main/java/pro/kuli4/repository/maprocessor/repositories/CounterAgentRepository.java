package pro.kuli4.repository.maprocessor.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgent;
import pro.kuli4.repository.maprocessor.entities.cagent.CounterAgentIdentifierTypes;

import java.util.List;
import java.util.Optional;

public interface CounterAgentRepository extends CrudRepository<CounterAgent, Long> {
    Optional<CounterAgent> findFirstByName(String name);

    @Query(nativeQuery = true,
            value = "SELECT * FROM COUNTER_AGENTS CA " +
                    "LEFT JOIN COUNTER_AGENT_IDENTIFIERS CAI ON CAI.COUNTER_AGENT = CA.ID " +
                    "WHERE CAI.TYPE = :#{#type.name()} " +
                    "AND CAI.VALUE = :value ")
    List<CounterAgent> findFirstByCounterAgentIdentifierTypeAndCounterAgentIdentifierValue(@Param("type") CounterAgentIdentifierTypes type, @Param("value") String value);
}
