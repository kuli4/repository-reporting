package pro.kuli4.repository.apireciever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.kuli4.repository.apireciever.services.SourceSystems;

@SpringBootApplication
public class ApiRecieverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiRecieverApplication.class, args);
    }
}
