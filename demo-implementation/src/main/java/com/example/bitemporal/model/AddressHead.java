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
public class AddressHead implements Head {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state", condition=":validityPoint state_begin AND state_end")
    private Collection<AddressState> states;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressHead")
    public Collection<PartnerAddressState> partners;
}
