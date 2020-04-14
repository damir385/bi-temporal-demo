package com.example.bitemporal.test

import com.example.bitemporal.model.AddressHead
import com.example.bitemporal.model.AddressState

import static java.time.LocalDate.of

trait AddressFactory {

    AddressHead createAddressHistory() {
        AddressHead
                .builder()
                .description("Home office")
                .states([
                        AddressState.builder().buildingNumber("99").street("Fancy street").zipCode("2344222").stateBegin(of(2001,1,1)).stateEnd(of(2002,6,1)).build(),
                        AddressState.builder().buildingNumber("21").street("Ugly lane").zipCode("2344222").stateBegin(of(2002,6,1)).stateEnd(of(2020,12,1)).build()
                ])
                .build()
    }
}
