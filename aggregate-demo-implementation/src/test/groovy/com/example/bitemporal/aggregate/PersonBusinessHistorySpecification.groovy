package com.example.bitemporal.aggregate


import com.example.bitemporal.aggregate.model.head.ContactHead
import com.example.bitemporal.aggregate.model.head.ContractHead
import com.example.bitemporal.aggregate.model.head.PersonHead
import com.example.bitemporal.aggregate.model.state.ContractState
import com.example.bitemporal.aggregate.model.state.DiscountState
import com.example.bitemporal.aggregate.repository.ContactBusinessHistoryRepository
import com.example.bitemporal.aggregate.repository.ContractBusinessHistoryRepository
import com.example.bitemporal.aggregate.repository.ContractStateRepository
import com.example.bitemporal.aggregate.repository.PersonBusinessHistoryRepository
import com.example.bitemporal.aggregate.repository.PersonRepository
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
import java.time.LocalDate

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

    @Autowired
    PersonRepository personRepository

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

    def "An aggregate history update"() {

        given: "A valid aggregate history stored and read from database"
        repository.create(validPerson())
        PersonHead personHead = repository.findAll()[0]
        String initial = personHead.contracts[0].head.discounts.toString()
        int initialSize = personHead.contracts[0].head.discounts.size()
        reportInfo "Initial discounts: ${initial}"


        when: "it discounts are being manipulated"
        personHead.contracts[0].head.discounts.remove(personHead.contracts[0].head.discounts[0])
        DiscountState newDiscount = newDiscount(personHead.contracts[0].head.discounts[0].stateEnd, 12)
        personHead.contracts[0].head.discounts.add(newDiscount)

        and: "the change is saved"
        repository.saveAndFlush(personHead)

        then: "it should be persisted successfully"
        PersonHead result = repository.findById(personHead.id).get()
        assert result.contracts[0].head.discounts.any { it.reason == newDiscount.reason }
        assert result.contracts[0].head.discounts.size() == initialSize


        reportInfo "CHECK DISCOUNTS: ${result.contracts[0].head.discounts}"

    }

    def "A snapshot fetch for an aggregate for given key date"() {

        given: "A valid aggregate history stored in database"
        PersonHead person = repository.create(validPerson())
        UUID id = person.id
        reportInfo "Initial discounts: ${person}"

        and: "a keyDate"
        LocalDate keyDate = LocalDate.of(2003, 1, 1)


        when: "it is read with a keyDate"
        entityManager.clear() //clears the cache
        PersonHead snapshot = repository.findOneByIdAndKeyDate(id, keyDate).get()


        then: "it should return only hte states valid for a given keyDate"
        assert snapshot.states.size() == 1
        assert snapshot.states[0].stateBegin.isBefore(keyDate)
        assert snapshot.states[0].stateEnd.isAfter(keyDate)
        assert snapshot.contracts.every {
            it.stateBegin.isBefore(keyDate) &&
                    it.stateEnd.isAfter(keyDate) &&
                    it.head.items.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    } &&
                    it.head.discounts.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    }

        }
        assert snapshot.contacts.every { it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate) }
        reportInfo "CHECK SNAPSHOT: ${snapshot}"

    }

}
