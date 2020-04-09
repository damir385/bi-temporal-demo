package com.example.persitence.api.model;

import com.example.persitence.api.validation.CheckTimeConsistency;

import java.util.Collection;
import java.util.UUID;

@CheckTimeConsistency
public interface Head {

    UUID getId();

    <S extends State> Collection<S> getStates();
}
