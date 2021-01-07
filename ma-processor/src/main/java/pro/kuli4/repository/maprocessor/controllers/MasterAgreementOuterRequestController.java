package pro.kuli4.repository.maprocessor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pro.kuli4.repository.maprocessor.dto.beans.MasterAgreementOuterRequestDto;
import pro.kuli4.repository.maprocessor.dto.mappers.MasterAgreementOuterRequestMapper;
import pro.kuli4.repository.maprocessor.repositories.MasterAgreementRepository;

import java.util.Collection;

@Slf4j
@CrossOrigin
@RequestMapping("/ma/")
@RestController
public class MasterAgreementOuterRequestController {

    private final MasterAgreementOuterRequestMapper masterAgreementOuterRequestMapper;
    private final MasterAgreementRepository masterAgreementRepository;

    public MasterAgreementOuterRequestController(
            MasterAgreementOuterRequestMapper masterAgreementOuterRequestMapper,
            MasterAgreementRepository masterAgreementRepository) {
        this.masterAgreementOuterRequestMapper = masterAgreementOuterRequestMapper;
        this.masterAgreementRepository = masterAgreementRepository;
    }

    @GetMapping(value = "/")
    public Collection<MasterAgreementOuterRequestDto> getAllMasterAgreements() {
        return masterAgreementOuterRequestMapper.mapAll(masterAgreementRepository.findAll());
    }

    @GetMapping(value = "/{id}/")
    public MasterAgreementOuterRequestDto getMasterAgreement(@PathVariable String id) {
        log.debug("Id from request = {}", id);
        return masterAgreementOuterRequestMapper.map(masterAgreementRepository.findById(Long.valueOf(id)).orElse(null));
    }
}

