package com.example.bitemporal.aggregate.model.state;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.bitemporal.aggregate.model.head.PersonHead;
import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "person")
@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@Filter(name="stateFilter")
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
