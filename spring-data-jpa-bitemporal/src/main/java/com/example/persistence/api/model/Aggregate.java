package com.example.persistence.api.model;

public interface Aggregate extends Head {

    Long getVersion();
}
