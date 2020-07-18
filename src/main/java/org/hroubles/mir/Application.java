package org.hroubles.mir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.hroubles.mir.repository")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
