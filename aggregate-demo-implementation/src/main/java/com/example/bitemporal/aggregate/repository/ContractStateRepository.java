package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.state.ContractState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractStateRepository extends JpaRepository<ContractState, UUID> {
}
