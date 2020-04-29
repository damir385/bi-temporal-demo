package com.example.bitemporal.contract.repository;

import com.example.bitemporal.contract.model.state.ContractState;
import com.example.persistence.api.repository.BusinessHistoryStateRepository;

public interface ContractStateRepository extends BusinessHistoryStateRepository<ContractState> {
}
