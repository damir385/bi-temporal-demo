package com.example.bitemporal.repository.test;

import com.example.bitemporal.jpa.test.PartnerHead;
import com.example.bitemporal.jpa.test.PartnerState;
import com.example.bitemporal.repository.BusinessHistoryRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PartnerRepository extends BusinessHistoryRepository<PartnerHead, PartnerState> {

    Optional<PartnerState> findStateByIdAndName(String id, LocalDate validityPoint, String name);
}
