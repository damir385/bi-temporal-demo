package com.example.bitemporal.aggregate.test

import com.example.bitemporal.aggregate.model.*

import java.time.LocalDate

import static java.time.LocalDate.of

trait PersonFactory {

    PersonHead validPerson() {
        List<PersonState> states = validPersonStates()

        PersonHead head = PersonHead
                .builder()
                .birthDate(of(1982, 04, 11))
                .states(states)
                .contacts(validContacts())
                .contracts(validContracts())
                .build()

        states.forEach { it.setHead(head) }
        head
    }

    List<PersonState> validPersonStates() {
        [
                PersonState.builder().firstName("Batman").lastName("Prince of Darkness").stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build(),
                PersonState.builder().firstName("Lars").lastName("Urlich").stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 11, 01)).build(),
        ]
    }

    List<ContactHead> validContacts() {
        ContactType email = ContactType.builder().description("email").build()
        email.setId(UUID.fromString("65e89d8e-0641-43ce-9b6a-0c3f2d053107"))

        EmailState emailState1 = EmailState.builder().email("Batman@gotham.de").build()
        emailState1.setStateBegin(of(2000, 1, 1))
        emailState1.setStateEnd(of(2002, 06, 01))



        EmailState emailState2 = EmailState.builder().email("lars@sanitarium.com").build()
        emailState2.setStateBegin(of(2002, 6, 1))
        emailState2.setStateEnd(of(2007, 2, 01))

        ContactHead emailHead = ContactHead.builder().type(email).states([emailState1, emailState2]).build()

        ContactType phone = ContactType.builder().description("phone").build()
        phone.setId(UUID.fromString("7ca46d0b-fdc4-44ff-9790-97475ba993b8"))

        PhoneState phoneState1 = PhoneState.builder().number("122343545").build()
        phoneState1.setStateBegin(of(2000, 1, 1))
        phoneState1.setStateEnd(of(2002, 06, 01))

        PhoneState phoneState2 = PhoneState.builder().number("911").build()
        phoneState2.setStateBegin(of(2002, 6, 1))
        phoneState2.setStateEnd(of(2007, 2, 01))

        ContactHead phoneHead = ContactHead.builder().type(phone).states([phoneState1, phoneState2]).build()

        ContactType postal = ContactType.builder().description("postal address").build()
        postal.setId(UUID.fromString("4311f113-36c6-4908-aba6-7702562b91ce"))

        PostalAddressState postalAddressState1 = PostalAddressState.builder().zipCode("130002").street("Dark Lane").city("Gotham").streetNumber("2a").build()
        postalAddressState1.setStateBegin(of(2000, 1, 1))
        postalAddressState1.setStateEnd(of(2002, 06, 01))

        PostalAddressState postalAddressState2 = PostalAddressState.builder().zipCode("4423678").street("1st Avenue").city("New York").streetNumber("667").build()
        postalAddressState2.setStateBegin(of(2002, 6, 1))
        postalAddressState2.setStateEnd(of(2007, 2, 01))

        ContactHead postalHead = ContactHead.builder().type(postal).states([postalAddressState1, postalAddressState2]).build()

        [emailHead, phoneHead, postalHead]
    }

    List<ContractHead> validContracts() {
        ContractState contractState1 = ContractState.builder().description("first contract").stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ContractState contractState2 = ContractState.builder().description("changed first contract").stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()
        ContractState contractState3 = ContractState.builder().description("second contract").stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ContractState contractState4 = ContractState.builder().description("changed second contract").stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        ContractHead contractHead1 = ContractHead.builder().number("11111111").states([contractState1, contractState2]).discounts(validDiscounts1()).items(validItems1()).build()
        ContractHead contractHead2 = ContractHead.builder().number("22222222").states([contractState3, contractState4]).discounts(validDiscounts2()).items(validItems2()).build()

        [contractHead1, contractHead2]
    }

    List<DiscountHead> validDiscounts1() {
        DiscountState discountState1 = DiscountState.builder().reason("Aprils discount").value(33.3d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        DiscountState discountState2 = DiscountState.builder().reason("The business is slow").value(25d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        DiscountHead discountHead = DiscountHead.builder().states([discountState1, discountState2]).build()

        [discountHead]
    }

    List<DiscountHead> validDiscounts2() {
        DiscountState discountState1 = DiscountState.builder().reason("Just like that").value(12.4d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        DiscountState discountState2 = DiscountState.builder().reason("No reason at all").value(1.5d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        DiscountHead discountHead = DiscountHead.builder().states([discountState1, discountState2]).build()

        [discountHead]
    }

    List<ItemHead> validItems1() {
        ItemState itemState1 = ItemState.builder().name("first item").price(123.33d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ItemState itemState2 = ItemState.builder().name("modified first item").price(59.06d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        ItemHead itemHead = ItemHead.builder().states([itemState1, itemState2]).build()

        [itemHead]
    }

    List<ItemHead> validItems2() {
        ItemState itemState1 = ItemState.builder().name("second item").price(22.5d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ItemState itemState2 = ItemState.builder().name("modified second item").price(69.95d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        ItemHead itemHead = ItemHead.builder().states([itemState1, itemState2]).build()
        itemHead.setStates([itemState1, itemState2])

        [itemHead]
    }

    DiscountState newDiscount(LocalDate startDate, int durationInMonths) {
        LocalDate endDate = startDate.plusMonths(durationInMonths)
        DiscountState discountState = DiscountState.builder().reason("A brand new promotional thing").value(77.1d).stateBegin(startDate).stateEnd(endDate).build()

        discountState
    }
}