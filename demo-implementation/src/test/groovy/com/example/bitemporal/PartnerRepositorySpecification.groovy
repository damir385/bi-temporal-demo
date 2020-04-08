package com.example.bitemporal

import com.example.bitemporal.model.PartnerHead
import com.example.bitemporal.repository.AddressBusinessHistoryRepository
import com.example.bitemporal.repository.AddressBusinessHistoryStateRepository
import com.example.bitemporal.repository.PartnerBusinessHistoryRepository
import com.example.bitemporal.repository.PartnerBusinessHistoryStateRepository
import com.example.bitemporal.test.PartnerDbTestUtilities
import com.example.bitemporal.test.PartnerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue

@SpringBootTest
@Testcontainers
class PartnerRepositorySpecification extends Specification implements PartnerFactory, PartnerDbTestUtilities{

    @Autowired
    ApplicationContext context

    @Autowired
    PartnerBusinessHistoryRepository partnerBusinessHistoryRepository

    //@Autowired
    PartnerBusinessHistoryStateRepository businessHistoryStateRepository

    //@Autowired
    AddressBusinessHistoryRepository addressBusinessHistoryRepository

    //@Autowired
    AddressBusinessHistoryStateRepository addressBusinessHistoryStateRepository

    //@Autowired
    JdbcTemplate jdbcTemplate

    @Shared
    PostgreSQLContainer container = new PostgreSQLContainer()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")

    def setup(){

    }

    def "Spring context should be up."() {
        expect:
        assert context, is (notNullValue())
        assert context.containsBean("biTemporalDemoApplication"), is(true)

    }


    def "Partner history create"() {

        given: "A partner history"
        PartnerHead partnerHead = createPartnerHistory()

        when:
        PartnerHead result = partnerBusinessHistoryRepository.create(partnerHead)

        then:
        println(result)
        assert result, is (partnerHead)

    }

}
