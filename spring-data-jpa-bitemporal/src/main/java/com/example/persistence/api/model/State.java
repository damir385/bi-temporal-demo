package com.example.persistence.api.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public interface State<H extends Head>  {

    H getHead();

    @NotNull
    LocalDate getStateBegin();

    @NotNull
    LocalDate getStateEnd();
}
