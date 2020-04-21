package com.example.persistence.api.repository;


import com.example.persistence.api.model.Head;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@NoRepositoryBean
public interface BusinessHistoryHeadRepository<H extends Head> extends JpaRepository<H, UUID>, RevisionRepository<H, UUID, Long> {

    H create(H head); //init a new state history
    H overwrite(H head); //overwrites the whole history
    H shift(H head, LocalDate from); //shift the all state history in time
    H shift(H head, Period period); //shift the all state history in time
    H addTimePoints(H head, Set<LocalDate> timePoints); //create a new states in tne history according to timePoints
    Optional<H> findOneByIdAndKeyDate(UUID id, LocalDate keyDate); //fetches the aggregate with a states valid for a given keyDate
    //void delete(H head); //Delete the whole business history --supported by parent



}
