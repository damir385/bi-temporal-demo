package com.example.persitence.api.model;

import java.util.Collection;
import java.util.UUID;

public interface Head {

    UUID getId();

    <S extends State> Collection<S> getStates();
}
