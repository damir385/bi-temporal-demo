package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.code.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
}
