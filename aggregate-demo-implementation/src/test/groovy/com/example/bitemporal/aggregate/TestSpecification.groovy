package com.example.bitemporal.aggregate

import com.example.bitemporal.aggregate.model.code.Test
import com.example.bitemporal.aggregate.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification;

import javax.persistence.EntityManager
import javax.persistence.LockModeType;

@SpringBootTest
@Testcontainers
@Transactional
class TestSpecification extends Specification {

    @Autowired
    EntityManager entityManager

    @Autowired
    TestRepository repository


    private Test saveWithOptimisticForceIncrement(Test test){
        final UUID id = test.id
        if (test.id != null) {
            Optional<Test> loaded = repository.findById(id)
            loaded.ifPresent{
                System.out.println("Applying lock on");
                entityManager.lock(it, LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                Test saved = repository.save(it)
                if (saved.getVersion() == 0) {
                    throw new IllegalStateException()
                }
            }
        }
        repository.save()
    }

    private def newTest = Test.builder().dummy("some text").build()

    def "Simple save"() {
       given:
       def test = newTest
       def version = test.version
        println "-->$test.id"
        println "-->$test.version"

        when:
        def saved = saveWithOptimisticForceIncrement(test)

        then: saved.version != version
    }
}
