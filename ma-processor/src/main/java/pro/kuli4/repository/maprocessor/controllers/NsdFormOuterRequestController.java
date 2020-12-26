package pro.kuli4.repository.maprocessor.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.kuli4.repository.maprocessor.entities.forms.NsdForm;
import pro.kuli4.repository.maprocessor.repositories.NsdFormRepository;


@Controller
@RequestMapping("/ma/form/")
public class NsdFormOuterRequestController {

    private final NsdFormRepository nsdFormRepository;

    public NsdFormOuterRequestController (NsdFormRepository nsdFormRepository) {
        this.nsdFormRepository = nsdFormRepository;
    }

    @GetMapping(value = "/{id}/")
    public ResponseEntity<String> getNsdFormById(@PathVariable String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "text/xml;charset=UTF-8");
        NsdForm form = nsdFormRepository.findFirstById(Long.valueOf(id)).orElse(null);
        if (form == null) {
            return new ResponseEntity<>(
                    "Fail",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                form.getForm(),
                headers,
                HttpStatus.OK);
    }

}
