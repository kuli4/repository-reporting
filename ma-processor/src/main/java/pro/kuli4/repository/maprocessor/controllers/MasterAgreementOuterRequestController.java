package pro.kuli4.repository.maprocessor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.kuli4.repository.maprocessor.dto.beans.MasterAgreementOuterRequestDto;
import pro.kuli4.repository.maprocessor.dto.mappers.MasterAgreementOuterRequestMapper;
import pro.kuli4.repository.maprocessor.repositories.MasterAgreementRepository;

import java.util.Collection;

@Slf4j
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
    public ResponseEntity<Collection<MasterAgreementOuterRequestDto>> getAllMasterAgreements() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(
                masterAgreementOuterRequestMapper.mapAll(masterAgreementRepository.findAll()),
                headers,
                HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/")
    public MasterAgreementOuterRequestDto getMasterAgreement(@PathVariable String id) {
        log.debug("Id from request = {}", id);
        return masterAgreementOuterRequestMapper.map(masterAgreementRepository.findById(Long.valueOf(id)).orElse(null));
    }
}

