package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.head.ContractHead;
import com.example.persistence.api.repository.BusinessHistoryHeadRepository;

public interface ContractBusinessHistoryHeadRepository extends BusinessHistoryHeadRepository<ContractHead> {
}
