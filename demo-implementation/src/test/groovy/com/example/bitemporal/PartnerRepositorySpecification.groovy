package com.example.bitemporal

import com.example.bitemporal.model.AddressHead
import com.example.bitemporal.model.PartnerAddressHead
import com.example.bitemporal.model.PartnerHead
import com.example.bitemporal.repository.*
import com.example.bitemporal.test.PartnerDbTestUtilities
import com.example.bitemporal.test.PartnerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.validation.ConstraintViolationException

import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue

@SpringBootTest
@Testcontainers
@Transactional
class PartnerRepositorySpecification extends Specification implements PartnerFactory, PartnerDbTestUtilities {

    @Autowired
    ApplicationContext context

    @Autowired
    EntityManager entityManager

    @Autowired
    PartnerBusinessHistoryRepository partnerBusinessHistoryRepository

    //@Autowired
    PartnerBusinessHistoryStateRepository businessHistoryStateRepository

    @Autowired
    AddressBusinessHistoryRepository addressBusinessHistoryRepository

    //@Autowired
    AddressBusinessHistoryStateRepository addressBusinessHistoryStateRepository

    @Autowired
    PartnerAddressBusinessHistoryRepository partnerAddressBusinessHistoryRepository

    //@Autowired
    JdbcTemplate jdbcTemplate

    @Shared
    PostgreSQLContainer container = new PostgreSQLContainer()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")

    def setup() {

    }

    def "Context initialization."() {

        expect: "Spring context should be up."
        assert context, is(notNullValue())
        assert context.containsBean("biTemporalDemoApplication"), is(true)

    }

    def "A valid Partner history create"() {

        given: "A partner history"
        PartnerHead partnerHead = createPartnerHistory()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        PartnerHead result = partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should be persisted successfully"
        assert result, is(partnerHead)
        reportInfo "Result: ${result}"

        List<PartnerHead> partnerHeads = partnerBusinessHistoryRepository.findAll()
        println(partnerHeads)

    }

    def "A invalid Partner history create - timeline has gaps"() {

        given: "A invalid partner history - timeline has gaps"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithGaps()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        ConstraintViolationException thrown = thrown()
        thrown.message.contains("Time consistency validation failed")
        reportInfo "Exception: ${thrown}"
    }

    def "A invalid Partner history create - exists null dates"() {

        given: "A invalid partner history - exists null dates"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithNullDates()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        ConstraintViolationException thrown = thrown()
        thrown.message.contains("must not be null")
        reportInfo "Exception: ${thrown}"
    }

    def "A invalid Partner history create - overlapping states"() {

        given: "A invalid partner history - overlapping states"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithOverlappingStates()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        ConstraintViolationException thrown = thrown()
        thrown.message.contains("Time consistency validation failed")
        reportInfo "Exception: ${thrown}"
    }

    def "An aggregate history create"() {

        given: "A valid aggregate history"
        PartnerHead partnerHead = createPartnerHistoryWithAddressHistory()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        PartnerHead result = partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should be persisted successfully"
        assert result, is(partnerHead)
        reportInfo "Result: ${result}"

        List<PartnerHead> partnerHeads = partnerBusinessHistoryRepository.findAll()
        List<AddressHead> addressHeads = addressBusinessHistoryRepository.findAll()
        List<PartnerAddressHead> partnerAddressHeads = partnerAddressBusinessHistoryRepository.findAll()


        reportInfo "CHECK PARTNER_HEAD: ${partnerHeads}"
        reportInfo "CHECK ADDRESS_HEAD: ${addressHeads}"
        reportInfo "CHECK PARTNER_ADDRESS_HEAD: ${partnerAddressHeads}"
    }




    //TODO findOne tests
    //TODO technical history queries
    //TODO rest of the api
    //TODO reference relationships save and queries
    //TODO temporal queries

}
