package com.example.persistence.repository

import com.example.persistence.repository.test.ObjectFactory
import com.example.persistence.repository.test.TestObject
import spock.lang.Specification

class MergerSpecification extends Specification implements ObjectFactory{

    def "simple merge - id = null"() {
        given:
        TestObject persisted = testObject1()
        TestObject modified = testObject2()

        when:
        Merger.doMerge(persisted, modified)

        then:
        thrown(IllegalArgumentException)
    }

    def "simple merge - different ids"() {
        given:
        TestObject persisted = testObject1()
        TestObject modified = testObject2()
        persisted.id = UUID.randomUUID()
        modified.id = UUID.randomUUID()

        when:
        Merger.doMerge(persisted, modified)

        then:
        thrown(IllegalArgumentException)
    }

    def "simple merge - success"() {
        given:
        TestObject persisted = testObject1()
        TestObject modified = testObject2()
        def id = UUID.randomUUID()
        persisted.id = id
        modified.id = id

        when:
        Merger.doMerge(persisted, modified)

        then:
        persisted == modified
    }
}
