package com.example.bitemporal.jpa;

import java.util.Collection;

public interface Head {

    String getId();

    <S extends State> Collection<S> getStates();
}
