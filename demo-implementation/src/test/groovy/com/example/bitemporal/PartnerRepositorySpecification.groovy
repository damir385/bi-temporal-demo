package com.example.bitemporal

import com.example.bitemporal.model.PartnerHead
import com.example.bitemporal.repository.AddressBusinessHistoryRepository
import com.example.bitemporal.repository.AddressBusinessHistoryStateRepository
import com.example.bitemporal.repository.PartnerBusinessHistoryRepository
import com.example.bitemporal.repository.PartnerBusinessHistoryStateRepository
import com.example.bitemporal.test.PartnerDbTestUtilities
import com.example.bitemporal.test.PartnerFactory
import com.example.persitence.api.validation.CheckTimeConsistency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.TransactionSystemException
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
class PartnerRepositorySpecification extends Specification implements PartnerFactory, PartnerDbTestUtilities {

    @Autowired
    ApplicationContext context

    @Autowired
    EntityManager entityManager

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

    }

    def "A invalid Partner history create - timeline has gaps"() {

        given: "A invalid partner history - timeline has gaps"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithGaps()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        TransactionSystemException thrown = thrown()
        causes(thrown).any {
            it.class == ConstraintViolationException &&
                    it.message.contains("Time consistency validation failed")
        }
        reportInfo "Exception: ${thrown}"
    }

    def "A invalid Partner history create - exists null dates"() {

        given: "A invalid partner history - exists null dates"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithNullDates()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        TransactionSystemException thrown = thrown()
        causes(thrown).any {
            it.class == ConstraintViolationException &&
                    it.message.contains("must not be null")
        }
        reportInfo "Exception: ${thrown}"
    }

    def "A invalid Partner history create - overlapping states"() {

        given: "A invalid partner history - overlapping states"
        PartnerHead partnerHead = createInvalidPartnerHistoryWithOverlappingStates()
        reportInfo "Input: ${partnerHead}"

        when: "it is passed to repository create method"
        partnerBusinessHistoryRepository.create(partnerHead)

        then: "it should raise an exception"
        TransactionSystemException thrown = thrown()
        causes(thrown).any {
            it.class == ConstraintViolationException &&
                    it.message.contains("Time consistency validation failed")
        }
        reportInfo "Exception: ${thrown}"
    }

    //TODO findOne tests
    //TODO technical history queries
    //TODO rest of the api
    //TODO reference relationships save and queries

}
