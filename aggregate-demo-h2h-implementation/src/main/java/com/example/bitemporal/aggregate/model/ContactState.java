package com.example.bitemporal.aggregate.model;

import com.example.persitence.api.model.State;
import com.example.persitence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "head")
@Entity
@Audited
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuppressWarnings("squid:S2160")
public abstract class ContactState extends AbstractUUIDPersistable implements State<ContactHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ContactHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
