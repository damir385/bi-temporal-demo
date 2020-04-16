package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.head.PersonHead;
import com.example.persistence.api.repository.BusinessHistoryRepository;

public interface PersonBusinessHistoryRepository extends BusinessHistoryRepository<PersonHead> {
}
