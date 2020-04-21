package com.example.bitemporal;

import com.example.persistence.repository.BusinessHistoryHeadRepositoryResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Bootstraps a context for the tests
 */

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BusinessHistoryHeadRepositoryResolver.class)
public class BiTemporalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiTemporalDemoApplication.class, args);
    }

}
