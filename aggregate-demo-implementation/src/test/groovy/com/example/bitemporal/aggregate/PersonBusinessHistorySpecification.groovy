package com.example.bitemporal.aggregate

import com.example.bitemporal.aggregate.model.head.ContactHead
import com.example.bitemporal.aggregate.model.head.ContractHead
import com.example.bitemporal.aggregate.model.head.PersonHead
import com.example.bitemporal.aggregate.model.state.ContractState
import com.example.bitemporal.aggregate.model.state.DiscountState
import com.example.bitemporal.aggregate.model.state.PersonState
import com.example.bitemporal.aggregate.repository.ContactBusinessHistoryHeadRepository
import com.example.bitemporal.aggregate.repository.ContractBusinessHistoryHeadRepository
import com.example.bitemporal.aggregate.repository.ContractStateRepository
import com.example.bitemporal.aggregate.repository.PersonBusinessHistoryHeadRepository
import com.example.bitemporal.aggregate.repository.PersonStateRepository
import com.example.bitemporal.aggregate.test.PersonFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Ignore
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
    PersonBusinessHistoryHeadRepository repository

    @Autowired
    ContactBusinessHistoryHeadRepository contactBusinessHistoryRepository

    @Autowired
    ContractBusinessHistoryHeadRepository contractBusinessHistoryRepository

    @Autowired
    ContractStateRepository contractStateRepository

    @Autowired
    PersonStateRepository stateRepository


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

    @Sql()
    def "A snapshot fetch for an aggregate for given key date"() {

        given: "A valid aggregate history stored in database"
        PersonHead person = repository.create(validPerson())
        UUID id = person.id
        reportInfo "Initial: ${person}"

        and: "a keyDate"
        LocalDate keyDate = LocalDate.of(2003, 1, 1)


        when: "it is read with a keyDate"
        entityManager.clear() //clears the cache
        PersonHead snapshot = repository.findOneByIdAndKeyDate(id, keyDate).get()


        then: "it should return only the states valid for a given keyDate"
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

    @Ignore
    def "Partial update test"() {

        given: "A valid aggregate history stored in database"
        PersonHead person = repository.create(validPerson())
        UUID id = person.id
        reportInfo "Initial discounts: ${person}"

        when: "it is being retrieved with a state for a given keyDate"
        entityManager.clear() //without this keyDate is ignored --cache!!!!!
        LocalDate keyDate = LocalDate.of(2003, 1, 1)
        PersonHead snapshot = repository.findOneByIdAndKeyDate(id, keyDate).get()

        and: "then changed"
        snapshot.states[0].firstName = "first name changed by test"

        and: "then saved"
        repository.saveAndFlush(snapshot)


        then: "other aggregate states in the business history have to be unaffected"
        entityManager.clear() //clears the cache
        List<PersonState> allStates = stateRepository.findAll()
        reportInfo "All states: ${allStates}"
        PersonHead newPerson = repository.findById(id).get()
        //assert newPerson.states.size() > 1 //this fails here
        //assert newPerson.states.any{ it.firstName =! "first name changed by test"} //this fails here

        and: "again without cache"
        entityManager.clear() //and now it should work
        PersonHead yetAnotherPerson = repository.findById(id).get()
        assert yetAnotherPerson.states.size() > 1
        assert yetAnotherPerson.states.any { it.firstName = !"first name changed by test" }
    }

    //states
    def "A snapshot fetch for an aggregate via state entity for given key date"() {

        given: "A valid aggregate history stored in database"
        PersonHead person = repository.create(validPerson())
        UUID id = person.id
        reportInfo "Initial aggregate: ${person}"
        entityManager.clear()

        when: "It is being retrieved via state entity"
        LocalDate keyDate = LocalDate.of(2003, 1, 1)
        PersonState state = stateRepository.findOneByKeyDate(id, keyDate).get()

        then: "it should return only the states valid for a given keyDate"

        reportInfo "Fetched states by a key date: ${state}"
        reportInfo "Fetched head via state by a key date: ${state.head}"
        assert state.head.states.size() == 1
        assert state.head.states[0].stateBegin.isBefore(keyDate)
        assert state.head.states[0].stateEnd.isAfter(keyDate)
        assert state.head.contracts.every {
            it.stateBegin.isBefore(keyDate) &&
                    it.stateEnd.isAfter(keyDate) &&
                    it.head.items.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    } &&
                    it.head.discounts.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    }

        }
        assert state.head.contacts.every { it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate) }
    }

    //@Ignore
    def "A snapshot fetch and save for an aggregate via state entity for given key date"() {

        given: "A valid aggregate snapshot"
        PersonHead person = repository.create(validPerson())
        UUID id = person.id
        reportInfo "Initial aggregate: ${person}"
        entityManager.clear()
        LocalDate keyDate = LocalDate.of(2003, 1, 1)
        PersonState state = stateRepository.findOneByKeyDate(id, keyDate).get()

        when: "it is being manipulated"
        entityManager.clear()

        and: "saved"
        stateRepository.saveAllCurrentStates(state)

        then: "it should be saved successfully"
        reportInfo "Fetched states by a key date: ${state}"
        reportInfo "Fetched head via state by a key date: ${state.head}"
        assert state.head.states.size() == 1
        assert state.head.states[0].stateBegin.isBefore(keyDate)
        assert state.head.states[0].stateEnd.isAfter(keyDate)
        assert state.head.contracts.every {
            it.stateBegin.isBefore(keyDate) &&
                    it.stateEnd.isAfter(keyDate) &&
                    it.head.items.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    } &&
                    it.head.discounts.every {
                        it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate)
                    }

        }
        assert state.head.contacts.every { it.stateBegin.isBefore(keyDate) && it.stateEnd.isAfter(keyDate) }
    }

}
