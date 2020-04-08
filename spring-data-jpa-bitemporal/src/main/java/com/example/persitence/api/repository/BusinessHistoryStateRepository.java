package com.example.persitence.api.repository;

import com.example.persitence.api.model.State;
import com.example.persitence.api.model.TimeStretchOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BusinessHistoryStateRepository <S extends State<?>> extends JpaRepository<S, UUID>, RevisionRepository<S, UUID, Long> {


    //TODO check again if this should be supported
    //void insert(S state); //adds new state validity segment (at least one end of the validity points has to be in touch with the current state)
    //TODO check if tu use new method or to implement existing save without inserts
    S update(S state); //changes existing validity segment (only state could be changed, change to validity points rises an exception)
    void delete(S state, TimeStretchOption option); //deletes the state validity segment and resolves the gaps

    Optional<S> findOneByKeyDate(String headId, LocalDate keyDate);
    //Optional<S> findOneById(String id);  ..already supported

}
