package com.example.bitemporal.repository;

import com.example.bitemporal.jpa.Head;
import com.example.bitemporal.jpa.State;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

//TODO make two repositories??
public interface BusinessHistoryRepository<H extends Head, S extends State<H>> {

    void create(H head); //init a new state history
    void overwrite(H head); //overwrites the whole history
    void shift(H head, LocalDate from); //shift the all state history in time
    void shift(H head, Period period); //shift the all state history in time
    void delete(H head); //Delete the whole business history

    void insert(S state); //adds new state validity segment (at least one end of the validity points has to be in touch with the current state)
    void update(S state); //changes existing validity segment (validity and state could be changed)

    Optional<H> findHistoryById(String id);
    Optional<S> findStateById(String headId, LocalDate validityPoint);
    Optional<S> findStateById(String id);

}
