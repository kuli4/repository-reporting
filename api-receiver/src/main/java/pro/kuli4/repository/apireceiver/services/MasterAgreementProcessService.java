package pro.kuli4.repository.apireceiver.services;

import org.springframework.http.ResponseEntity;

public interface MasterAgreementProcessService {
    ResponseEntity<String> handle(String body) throws Exception;
}
