package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.head.ContractHead;
import com.example.persistence.api.repository.BusinessHistoryRepository;

public interface ContractBusinessHistoryRepository extends BusinessHistoryRepository<ContractHead> {
}
