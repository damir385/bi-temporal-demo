package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.ContactHead;
import com.example.persitence.api.repository.BusinessHistoryRepository;

public interface ContactBusinessHistoryRepository extends BusinessHistoryRepository<ContactHead> {
}
