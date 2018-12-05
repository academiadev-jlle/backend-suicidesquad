package br.com.academiadev.suicidesquad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SuicidesquadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuicidesquadApplication.class, args);
    }
}
