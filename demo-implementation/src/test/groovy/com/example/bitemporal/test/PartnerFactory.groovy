package com.example.bitemporal.test

import com.example.bitemporal.model.AddressHead
import com.example.bitemporal.model.PartnerAddressHead
import com.example.bitemporal.model.PartnerAddressState
import com.example.bitemporal.model.PartnerHead
import com.example.bitemporal.model.PartnerState

import static java.time.LocalDate.of

trait PartnerFactory implements AddressFactory {

    PartnerHead createPartnerHistory() {
        PartnerHead
                .builder()
                .birthName("Batman")
                .states([
                        PartnerState.builder().name("Bruce").stateBegin(of(2001, 1, 1)).stateEnd(of(2002, 6, 1)).build(),
                        PartnerState.builder().name("Joe").stateBegin(of(2002, 6, 1)).stateEnd(of(2020, 12, 1)).build()
                ])
                .build()
    }


    PartnerHead createInvalidPartnerHistoryWithGaps() {
        PartnerHead
                .builder()
                .birthName("Batman")
                .states([
                        PartnerState.builder().name("Bruce").stateBegin(of(2001, 1, 1)).stateEnd(of(2002, 6, 1)).build(),
                        PartnerState.builder().name("Joe").stateBegin(of(2002, 11, 1)).stateEnd(of(2020, 12, 1)).build()
                ])
                .build()
    }

    PartnerHead createInvalidPartnerHistoryWithNullDates() {
        PartnerHead
                .builder()
                .birthName("Batman")
                .states([
                        PartnerState.builder().name("Bruce").stateBegin(of(2001, 1, 1)).build(),
                        PartnerState.builder().name("Joe").stateEnd(of(2020, 12, 1)).build()
                ])
                .build()
    }

    PartnerHead createInvalidPartnerHistoryWithOverlappingStates() {
        PartnerHead
                .builder()
                .birthName("Batman")
                .states([
                        PartnerState.builder().name("Bruce").stateBegin(of(2001, 1, 1)).stateEnd(of(2002, 6, 1)).build(),
                        PartnerState.builder().name("Joe").stateBegin(of(2002, 4, 1)).stateEnd(of(2020, 12, 1)).build()
                ])
                .build()
    }

    PartnerHead createPartnerHistoryWithAddressHistory() {
        PartnerHead partnerHead = createPartnerHistory()
        AddressHead addressHead = createAddressHistory()
        PartnerAddressHead partnerAddressHead = PartnerAddressHead
                .builder().states([
                PartnerAddressState.builder().stateBegin(of(2001, 1, 1)).stateEnd(of(2002, 6, 1)).build(),
                PartnerAddressState.builder().stateBegin(of(2002, 6, 1)).stateEnd(of(2020, 12, 1)).build()
        ]).build()
        partnerHead.setAddresses(partnerAddressHead.getStates())
        addressHead.setPartners(partnerAddressHead.getStates())
        partnerHead
    }
}