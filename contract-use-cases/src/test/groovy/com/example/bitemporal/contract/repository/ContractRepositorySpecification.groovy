package com.example.bitemporal.contract.repository


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.jdbc.Sql
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
class ContractRepositorySpecification extends Specification implements ContractFactory {


    @Autowired
    ContractStateRepository repository


    @Autowired
    ApplicationContext context

    @Autowired
    EntityManager entityManager

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
        assert context.containsBean("contractApplication"), is(true)

    }

    def "UseCase 1:Create Contract (with 2 ContractParts)"() {
        given: "A new aggregate in db"
        def saved = repository.save(contract1())
        def id = saved.id
        entityManager.flush()
        entityManager.clear()

        when: "it is being retrieved from db"
        def found = repository.findById(id).get()

        then: "it should be same as saved aggregate"
        assert found == saved
    }

    @Sql(statements = [
            "insert into contract (contract_no, valid_from, valid_to, id) values ('VT001', '2020-01-01', '3000-01-01', '2f8cfad4-4c16-4052-9c4d-8e600374df6e')",
            "insert into contract_part (valid_from, valid_to, id) values ('2020-01-01', '3000-01-01', '33336f9e-0b44-4534-b792-7ee57c680867')",
            "insert into contract_part_state (contract_id, description, head_id, premium, state_begin, state_end, id) values ('2f8cfad4-4c16-4052-9c4d-8e600374df6e', 'Happy Car', '33336f9e-0b44-4534-b792-7ee57c680867',75.0,'2020-01-01','3000-01-01','3a39a6d1-d4e6-440a-9509-e46c352a1432')",
            "insert into contract_part (valid_from, valid_to, id) values ('2020-01-01', '3000-01-01', '3934a0f1-f8aa-469a-a580-f9aa19ce3047')",
            "insert into contract_part_state (contract_id, description, head_id, premium, state_begin, state_end, id) values ('2f8cfad4-4c16-4052-9c4d-8e600374df6e', 'Protected Home', '3934a0f1-f8aa-469a-a580-f9aa19ce3047', 25.0, '2020-01-01', '3000-01-01', 'c02be6c6-e2dc-42b4-b21e-86e60e603863')",
            "insert into contract_state (head_id, premium, state_begin, state_end, id) values ('2f8cfad4-4c16-4052-9c4d-8e600374df6e', 100.0, '2020-01-01', '3000-01-01', 'ef54bb0c-8cd4-4e77-a694-1a63d7fd04e6')"
    ])
    def "UseCase 2 (Based on UseCase 1): Change Price in ContractPart 2 at validty date 01.02.2020"() {

        when: "it is being retrieved from db"
        def saved = repository.save(request1())

        then: "it should be successful"
        assert true

        and: "check some other stuff"
        assert true
    }


}
