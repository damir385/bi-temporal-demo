package com.example.bitemporal.aggregate.repository;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.persistence.api.repository.BusinessHistoryRepository;

public interface ContactBusinessHistoryRepository extends BusinessHistoryRepository<ContactHead> {
}
