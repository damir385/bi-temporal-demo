package com.example.bitemporal.repository;

import com.example.bitemporal.jpa.Head;
import com.example.bitemporal.jpa.test.PartnerState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<PartnerState, String> {

    Optional<PartnerState> findByHeadAndStateBeginIsBeforeAndStateEndIsAfter(Head head, LocalDate date1, LocalDate date2);


}
