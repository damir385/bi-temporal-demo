package com.example.bitemporal.contract.repository

import com.example.bitemporal.contract.model.head.Contract
import com.example.bitemporal.contract.model.head.ContractPart
import com.example.bitemporal.contract.model.state.ContractPartState
import com.example.bitemporal.contract.model.state.ContractState

import java.time.LocalDate

trait ContractFactory {


    ContractState request1() {
        def part1 = part1()
        part1.id = UUID.fromString("3a39a6d1-d4e6-440a-9509-e46c352a1432")
        part1.head.id = UUID.fromString("3934a0f1-f8aa-469a-a580-f9aa19ce3047")

        def part2 = part2()
        part2.premium = 30

        Contract head = Contract
                .builder()
                .contractParts([part1, part2])
                .contractNo("VT001")
                .validFrom(LocalDate.of(2020, 1, 1))
                .validTo(LocalDate.of(3000, 1, 1))
                .build()
        head.id = UUID.fromString("2f8cfad4-4c16-4052-9c4d-8e600374df6e")

        ContractState state = ContractState
                .builder()
                .head(head)
                .premium(105)
                .stateBegin(LocalDate.of(2020, 2, 1))
                .stateEnd(LocalDate.of(3000, 1, 1))
                .build()
        head.setStates([state])

        part1.contract= head
        part2.contract= head

        state
    }

    ContractState contract1() {
        Contract head = Contract
                .builder()
                .contractParts([part1(), part2()])
                .contractNo("VT001")
                .validFrom(LocalDate.of(2020, 1, 1))
                .validTo(LocalDate.of(3000, 1, 1))
                .build()
        ContractState state = ContractState
                .builder()
                .head(head)
                .premium(100)
                .stateBegin(LocalDate.of(2020, 1, 1))
                .stateEnd(LocalDate.of(3000, 1, 1))
                .build()
        head.setStates([state])
        state
    }

    def part1() {
        ContractPart head = ContractPart
                .builder()
                .validFrom(LocalDate.of(2020, 1, 1))
                .validTo(LocalDate.of(3000, 1, 1))
                .build()
        ContractPartState state = ContractPartState
                .builder()
                .head(head)
                .contract(null)
                .premium(75)
                .description("Happy Car")
                .stateBegin(LocalDate.of(2020, 1, 1))
                .stateEnd(LocalDate.of(3000, 1, 1))
                .build()
        head.setStates([state])
        state
    }

    def part2() {
        ContractPart head = ContractPart
                .builder()
                .validFrom(LocalDate.of(2020, 1, 1))
                .validTo(LocalDate.of(3000, 1, 1))
                .build()
        ContractPartState state = ContractPartState
                .builder()
                .head(head)
                .contract(null)
                .premium(25)
                .description("Protected Home")
                .stateBegin(LocalDate.of(2020, 1, 1))
                .stateEnd(LocalDate.of(3000, 1, 1))
                .build()
        head.setStates([state])
        state
    }
}