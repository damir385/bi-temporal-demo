package com.example.bitemporal.aggregate.test

import com.example.bitemporal.aggregate.model.*

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

    List<ContactState> validContacts() {
        ContactType email = ContactType.builder().description("email").build()
        email.setId(UUID.fromString("65e89d8e-0641-43ce-9b6a-0c3f2d053107"))
        ContactHead emailHead = ContactHead.builder().type(email).build()

        EmailState emailState1 = EmailState.builder().email("Batman@gotham.de").build()
        emailState1.setHead(emailHead)
        emailState1.setStateBegin(of(2000, 1, 1))
        emailState1.setStateEnd(of(2002, 06, 01))

        EmailState emailState2 = EmailState.builder().email("lars@sanitarium.com").build()
        emailState2.setHead(emailHead)
        emailState2.setStateBegin(of(2002, 6, 1))
        emailState2.setStateEnd(of(2007, 2, 01))

        emailHead.setStates([emailState1, emailState2])

        ContactType phone = ContactType.builder().description("phone").build()
        phone.setId(UUID.fromString("7ca46d0b-fdc4-44ff-9790-97475ba993b8"))
        ContactHead phoneHead = ContactHead.builder().type(phone).build()

        PhoneState phoneState1 = PhoneState.builder().number("122343545").build()
        phoneState1.setHead(phoneHead)
        phoneState1.setStateBegin(of(2000, 1, 1))
        phoneState1.setStateEnd(of(2002, 06, 01))

        PhoneState phoneState2 = PhoneState.builder().number("911").build()
        phoneState2.setHead(phoneHead)
        phoneState2.setStateBegin(of(2002, 6, 1))
        phoneState2.setStateEnd(of(2007, 2, 01))

        phoneHead.setStates([phoneState1, phoneState2])

        ContactType postal = ContactType.builder().description("postal address").build()
        postal.setId(UUID.fromString("4311f113-36c6-4908-aba6-7702562b91ce"))
        ContactHead postalHead = ContactHead.builder().type(postal).build()

        PostalAddressState postalAddressState1 = PostalAddressState.builder().zipCode("130002").street("Dark Lane").city("Gotham").streetNumber("2a").build()
        postalAddressState1.setHead(postalHead)
        postalAddressState1.setStateBegin(of(2000, 1, 1))
        postalAddressState1.setStateEnd(of(2002, 06, 01))

        PostalAddressState postalAddressState2 = PostalAddressState.builder().zipCode("4423678").street("1st Avenue").city("New York").streetNumber("667").build()
        postalAddressState2.setHead(postalHead)
        postalAddressState2.setStateBegin(of(2002, 6, 1))
        postalAddressState2.setStateEnd(of(2007, 2, 01))

        postalHead.setStates([postalAddressState1, postalAddressState2])

        [emailState1, emailState2, phoneState1, phoneState2, postalAddressState1, postalAddressState2]
    }

    List<ContractState> validContracts() {
        ContractHead contractHead1 = ContractHead.builder().number("11111111").discounts(validDiscounts1()).items(validItems1()).build()
        ContractHead contractHead2 = ContractHead.builder().number("22222222").discounts(validDiscounts2()).items(validItems2()).build()

        ContractState contractState1 = ContractState.builder().head(contractHead1).description("first contract").stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ContractState contractState2 = ContractState.builder().head(contractHead1).description("changed first contract").stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()
        ContractState contractState3 = ContractState.builder().head(contractHead2).description("second contract").stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ContractState contractState4 = ContractState.builder().head(contractHead2).description("changed second contract").stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        contractHead1.setStates([contractState1, contractState2])
        contractHead2.setStates([contractState3, contractState4])

        [contractState1, contractState2, contractState3, contractState4]
    }

    List<DiscountState> validDiscounts1() {
        DiscountHead discountHead = DiscountHead.builder().build()

        DiscountState discountState1 = DiscountState.builder().head(discountHead).reason("Aprils discount").value(33.3d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        DiscountState discountState2 = DiscountState.builder().head(discountHead).reason("The business is slow").value(25d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        discountHead.setStates([discountState1, discountState2])

        [discountState1, discountState2]
    }

    List<DiscountState> validDiscounts2() {
        DiscountHead discountHead = DiscountHead.builder().build()

        DiscountState discountState1 = DiscountState.builder().head(discountHead).reason("Just like that").value(12.4d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        DiscountState discountState2 = DiscountState.builder().head(discountHead).reason("No reason at all").value(1.5d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        discountHead.setStates([discountState1, discountState2])

        [discountState1, discountState2]
    }

    List<ItemState> validItems1() {
        ItemHead itemHead = ItemHead.builder().build()

        ItemState itemState1 = ItemState.builder().head(itemHead).name("first item").price(123.33d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ItemState itemState2 = ItemState.builder().head(itemHead).name("modified first item").price(59.06d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        itemHead.setStates([itemState1, itemState2])

        [itemState1, itemState2]
    }

    List<ItemState> validItems2() {
        ItemHead itemHead = ItemHead.builder().build()

        ItemState itemState1 = ItemState.builder().head(itemHead).name("second item").price(22.5d).stateBegin(of(2000, 1, 1)).stateEnd(of(2002, 06, 01)).build()
        ItemState itemState2 = ItemState.builder().head(itemHead).name("modified second item").price(69.95d).stateBegin(of(2002, 6, 1)).stateEnd(of(2007, 2, 01)).build()

        itemHead.setStates([itemState1, itemState2])

        [itemState1, itemState2]
    }
}