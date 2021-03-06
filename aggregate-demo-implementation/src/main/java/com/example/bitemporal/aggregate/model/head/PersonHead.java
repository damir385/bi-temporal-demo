package com.example.bitemporal.aggregate.model.head;

import com.example.bitemporal.aggregate.model.state.ContactState;
import com.example.bitemporal.aggregate.model.state.ContractState;
import com.example.bitemporal.aggregate.model.state.PersonState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractAggregate;
import com.example.persistence.model.AbstractUUIDPersistable;
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
public class PersonHead extends AbstractAggregate {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state")
    private Collection<PersonState> states;

    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @Filter(name="state")
    private Collection<ContactState> contacts;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @Filter(name="state")
    private Collection<ContractState> contracts;

    private LocalDate validFrom;
    private LocalDate validTo;
}
