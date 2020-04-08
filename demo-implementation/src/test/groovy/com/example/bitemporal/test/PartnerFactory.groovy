package com.example.bitemporal.test

import com.example.bitemporal.model.PartnerHead
import com.example.bitemporal.model.PartnerState

import static java.time.LocalDate.of

trait PartnerFactory {

    def createPartnerHistory() {
        PartnerHead
                .builder()
                .birthName("Batman")
                .states([
                        PartnerState.builder().name("Bruce").stateBegin(of(2001,1,1)).stateEnd(of(2002,6,1)).build(),
                        PartnerState.builder().name("Joe").stateBegin(of(2002,6,1)).stateEnd(of(2020,12,1)).build()
                ])
        .build()
    }

}