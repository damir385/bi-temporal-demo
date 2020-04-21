package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.head.PersonHead;
import com.example.persistence.api.repository.BusinessHistoryHeadRepository;

public interface PersonBusinessHistoryHeadRepository extends BusinessHistoryHeadRepository<PersonHead> {
}
