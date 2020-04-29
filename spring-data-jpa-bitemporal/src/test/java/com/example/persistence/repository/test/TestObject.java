package com.example.persistence.repository.test;

import com.example.persistence.model.AbstractUUIDPersistable;

import java.time.LocalDate;


public class TestObject extends AbstractUUIDPersistable {

    private String stringProperty;
    private Long longProperty;
    private LocalDate localDateProperty;


    public TestObject(String stringProperty, Long longProperty, LocalDate localDateProperty) {
        this.stringProperty = stringProperty;
        this.longProperty = longProperty;
        this.localDateProperty = localDateProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public Long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
    }

    public LocalDate getLocalDateProperty() {
        return localDateProperty;
    }
}
