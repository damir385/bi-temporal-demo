package com.example.persitence.api.model;

import com.example.persitence.api.validation.CheckTimeConsistency;
import org.springframework.data.domain.Persistable;

import java.util.Collection;
import java.util.UUID;

@CheckTimeConsistency
public interface Head extends Persistable<UUID> {

    <S extends State> Collection<S> getStates();
}
