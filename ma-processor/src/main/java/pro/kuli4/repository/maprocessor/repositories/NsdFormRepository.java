package pro.kuli4.repository.maprocessor.repositories;


import org.springframework.data.repository.CrudRepository;
import pro.kuli4.repository.maprocessor.entities.forms.NsdForm;

import java.util.Optional;

public interface NsdFormRepository extends CrudRepository<NsdForm, Long> {
    Optional<NsdForm> findFirstById(Long id);
}
