package com.example.bitemporal.aggregate

import com.example.bitemporal.aggregate.model.ContactHead
import com.example.bitemporal.aggregate.model.ContractHead
import com.example.bitemporal.aggregate.model.ContractState
import com.example.bitemporal.aggregate.model.PersonHead
import com.example.bitemporal.aggregate.repository.ContactBusinessHistoryRepository
import com.example.bitemporal.aggregate.repository.ContractBusinessHistoryRepository
import com.example.bitemporal.aggregate.repository.ContractStateRepository
import com.example.bitemporal.aggregate.repository.PersonBusinessHistoryRepository
import com.example.bitemporal.aggregate.test.PersonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.EntityManager

import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue

@SpringBootTest
@Testcontainers
@Transactional
class PersonBusinessHistorySpecification extends Specification implements PersonFactory {

    @Autowired
    ApplicationContext context

    @Autowired
    EntityManager entityManager

    @Autowired
    PersonBusinessHistoryRepository repository

    @Autowired
    ContactBusinessHistoryRepository contactBusinessHistoryRepository

    @Autowired
    ContractBusinessHistoryRepository contractBusinessHistoryRepository

    @Autowired
    ContractStateRepository contractStateRepository

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
        assert context.containsBean("biTemporalDemoAggregateApplication"), is(true)

    }


    def "An aggregate history create"() {

        given: "A valid aggregate history"
        PersonHead personHead = validPerson()
        reportInfo "Input: ${personHead}"

        when: "it is passed to repository create method"
        PersonHead result = repository.create(personHead)

        then: "it should be persisted successfully"
        assert result, is(personHead)
        reportInfo "Result: ${result}"

        List<PersonHead> partnerHeads = repository.findAll()
        List<ContactHead> contactHeads = contactBusinessHistoryRepository.findAll()
        List<ContractHead> contractHeads = contractBusinessHistoryRepository.findAll()
        List<ContractState> contractStates = contractStateRepository.findAll()


        reportInfo "CHECK PARTNER_HEAD: ${partnerHeads}"
        reportInfo "CHECK CONTACT_HEAD: ${contactHeads}"
        reportInfo "CHECK CONTRACT_HEAD: ${contractHeads}"
        reportInfo "CHECK CONTRACT_STATE: ${contractStates}"
    }


}
