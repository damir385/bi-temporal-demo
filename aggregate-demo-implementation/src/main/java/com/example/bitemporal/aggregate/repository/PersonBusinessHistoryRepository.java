package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.PersonHead;
import com.example.persitence.api.repository.BusinessHistoryRepository;

public interface PersonBusinessHistoryRepository extends BusinessHistoryRepository<PersonHead> {
}
