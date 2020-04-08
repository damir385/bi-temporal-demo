package com.example.bitemporal.model;


import com.example.persitence.api.model.Head;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@Entity
@Audited
public class PartnerAddressHead implements Head {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state", condition=":validityPoint state_begin AND state_end")
    private Collection<PartnerAddressState> states;
}
