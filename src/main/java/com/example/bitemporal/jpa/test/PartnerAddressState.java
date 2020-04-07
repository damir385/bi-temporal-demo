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
public class PartnerAddressState implements State<PartnerAddressHead> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;


    @ManyToOne
    @JoinColumn(name="head_id")
    private PartnerAddressHead head;

    @ManyToOne
    @JoinColumn(name="partner_head_id")
    private PartnerHead partnerHead;

    @ManyToOne
    @JoinColumn(name="address_head_id")
    private AddressHead addressHead;

    private LocalDate stateBegin;

    private LocalDate stateEnd;


}
