package com.example.persistence.api.repository;

import com.example.persistence.api.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
@NoRepositoryBean
public interface BusinessHistoryStateRepository <S extends State<?>> extends JpaRepository<S, UUID>, RevisionRepository<S, UUID, Long> {

    Optional<S> findOneByKeyDate(UUID headId, LocalDate keyDate);

}
