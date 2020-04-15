package com.example.bitemporal.aggregate.model;

import com.example.persitence.api.model.State;
import com.example.persitence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "person")
@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("squid:S2160")
public abstract class ContactState extends AbstractUUIDPersistable implements State<ContactHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ContactHead head;

    @ManyToOne
    private PersonHead person;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
