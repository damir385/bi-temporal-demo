package com.example.bitemporal.jpa;

import java.time.LocalDate;

public interface State<H extends Head>{

    String getId();

    H getHead();

    LocalDate getStateBegin();


    LocalDate getStateEnd();
}
