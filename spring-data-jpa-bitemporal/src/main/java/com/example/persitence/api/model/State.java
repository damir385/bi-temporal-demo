package com.example.persitence.api.model;

import java.time.LocalDate;
import java.util.UUID;

public interface State<H extends Head>{

    UUID getId();

    H getHead();

    LocalDate getStateBegin();


    LocalDate getStateEnd();
}
