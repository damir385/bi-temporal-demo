package com.example.bitemporal.jpa.test;

import com.example.bitemporal.jpa.Head;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;


@Data
@Builder
@Entity
@Audited
public class AddressHead implements Head {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state", condition=":validityPoint state_begin AND state_end")
    private Collection<PartnerState> states;

    @OneToMany(mappedBy = "addressHead")
    public Collection<PartnerAddressState> partners;
}
