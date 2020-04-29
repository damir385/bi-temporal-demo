package com.example.persistence.repository.test

import java.time.LocalDate

trait ObjectFactory {

    def testObject1() {
        new TestObject("Some test", 2, LocalDate.now())
    }

    def testObject2() {
        new TestObject("Some other test", 1, LocalDate.now().plusDays(2))
    }
}
