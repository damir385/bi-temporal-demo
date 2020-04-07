package com.example.bitemporal.repository.test;

import com.example.bitemporal.jpa.test.AddressHead;
import com.example.bitemporal.jpa.test.AddressState;
import com.example.bitemporal.repository.BusinessHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends BusinessHistoryRepository<AddressHead, AddressState> {
}
