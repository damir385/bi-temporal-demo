package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.state.PersonState;
import com.example.persistence.api.repository.BusinessHistoryStateRepository;

public interface PersonStateRepository extends BusinessHistoryStateRepository<PersonState> {
}
