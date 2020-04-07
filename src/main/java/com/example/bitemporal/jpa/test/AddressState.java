package com.example.bitemporal.jpa.test;

import com.example.bitemporal.jpa.State;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity
@Audited
public class AddressState implements State<AddressHead> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String zipCode;
    private String street;
    private String buildingNumber;

    @ManyToOne
    @JoinColumn(name="head_id")
    private AddressHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
