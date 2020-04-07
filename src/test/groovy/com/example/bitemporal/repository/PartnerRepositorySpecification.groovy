package com.example.bitemporal.repository

import com.example.bitemporal.repository.test.AddressRepository
import com.example.bitemporal.repository.test.PartnerRepository
import com.example.bitemporal.test.PartnerDbTestUtilities
import com.example.bitemporal.test.PartnerFactory
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.hamcrest.Matchers.notNullValue
import static org.hamcrest.Matchers.is

@SpringBootTest
@Testcontainers
@Slf4j
class PartnerRepositorySpecification extends Specification implements PartnerFactory, PartnerDbTestUtilities{

    @Autowired
    ApplicationContext context

    @Autowired
    PartnerRepository partnerRepository

    @Autowired
    AddressRepository addressRepository

    @Autowired
    JdbcTemplate jdbcTemplate

    @Shared
    PostgreSQLContainer container = new PostgreSQLContainer()

    def setup(){

    }

    def "Spring context should be up."() {
        expect:
        assert context, is (notNullValue())
        assert context.containsBean("biTemporalDemoApplication"), is(true)

    }

}
