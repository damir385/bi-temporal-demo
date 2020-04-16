package com.example.bitemporal.aggregate.model.state;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
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
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("squid:S2160")
public class ContactState extends AbstractUUIDPersistable implements State<ContactHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ContactHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
