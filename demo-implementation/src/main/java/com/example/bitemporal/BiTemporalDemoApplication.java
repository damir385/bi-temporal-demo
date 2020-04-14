package com.example.bitemporal;

import com.example.persitence.repository.BusinessHistoryRepositoryResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Bootstraps a context for the tests
 */

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BusinessHistoryRepositoryResolver.class)
public class BiTemporalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiTemporalDemoApplication.class, args);
    }

}
