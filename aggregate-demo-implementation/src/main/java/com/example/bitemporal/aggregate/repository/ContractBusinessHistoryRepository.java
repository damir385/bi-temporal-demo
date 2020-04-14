package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.ContractHead;
import com.example.persitence.api.repository.BusinessHistoryRepository;

public interface ContractBusinessHistoryRepository extends BusinessHistoryRepository<ContractHead> {
}
