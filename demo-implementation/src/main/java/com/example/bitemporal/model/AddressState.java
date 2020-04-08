package com.example.bitemporal.model;


import com.example.persitence.api.model.State;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Entity
@Audited
public class AddressState implements State<AddressHead> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String zipCode;
    private String street;
    private String buildingNumber;

    @ManyToOne
    @JoinColumn(name="head_id")
    private AddressHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
