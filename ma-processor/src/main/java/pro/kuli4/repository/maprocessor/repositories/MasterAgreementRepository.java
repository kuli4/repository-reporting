package pro.kuli4.repository.maprocessor.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pro.kuli4.repository.maprocessor.entities.MasterAgreement;

public interface MasterAgreementRepository
        extends PagingAndSortingRepository<MasterAgreement, Long> {

}
