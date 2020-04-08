package com.example.persitence.api.repository;


import com.example.persitence.api.model.Head;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.UUID;


public interface BusinessHistoryRepository<H extends Head> extends JpaRepository<H, UUID>, RevisionRepository<H, UUID, Long> {

    H create(H head); //init a new state history
    H overwrite(H head); //overwrites the whole history
    H shift(H head, LocalDate from); //shift the all state history in time
    H shift(H head, Period period); //shift the all state history in time
    H addTimePoints(H head, Set<LocalDate> timePoints); //create a new states in tne history according to timePoints

    //void delete(H head); //Delete the whole business history --supported by parent



}
