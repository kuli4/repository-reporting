package pro.kuli4.repository.maprocessor.repositories;

import org.springframework.data.repository.CrudRepository;
import pro.kuli4.repository.maprocessor.entities.ProcessRequest;
import pro.kuli4.repository.maprocessor.entities.ProcessRequestStatuses;

import java.util.List;

public interface ProcessRequestRepository extends CrudRepository<ProcessRequest, Long> {
    List<ProcessRequest> findAllByStatusOrderByCreatedAt(ProcessRequestStatuses status);
}
