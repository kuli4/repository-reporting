package pro.kuli4.repository.maprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MasterAgreementProcessorApp {
    public static void main(String[] args) {
        SpringApplication.run(MasterAgreementProcessorApp.class, args);
    }
}
