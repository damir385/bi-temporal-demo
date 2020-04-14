package com.example.bitemporal.aggregate.model;

import com.example.persitence.api.model.Head;
import com.example.persitence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class PersonHead extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state", condition=":validityPoint state_begin AND state_end")
    private Collection<PersonState> states;

    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Collection<ContactState> contacts;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Collection<ContractState> contracts;
}