package com.example.persitence.api.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public interface State<H extends Head> {

    UUID getId();

    H getHead();

    @NotNull
    LocalDate getStateBegin();

    @NotNull
    LocalDate getStateEnd();
}
