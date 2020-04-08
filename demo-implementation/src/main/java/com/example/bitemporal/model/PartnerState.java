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
public class PartnerState implements State<PartnerHead> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;


    @ManyToOne
    @JoinColumn(name="head_id")
    private PartnerHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
