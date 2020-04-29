package com.example.persistence.api.model;

import com.example.persistence.api.validation.CheckTimeConsistency;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@CheckTimeConsistency
public interface Head extends Persistable<UUID> {

    <S extends State> Collection<S> getStates();

    @NotNull
    LocalDate getValidFrom();


    LocalDate getValidTo();
}
