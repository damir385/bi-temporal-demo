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
public class PartnerState implements State<PartnerHead> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;


    @ManyToOne
    @JoinColumn(name="head_id")
    private PartnerHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
