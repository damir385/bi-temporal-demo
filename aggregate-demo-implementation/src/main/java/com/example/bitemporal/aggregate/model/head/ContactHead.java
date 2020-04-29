package com.example.bitemporal.aggregate.model.head;

import com.example.bitemporal.aggregate.model.code.ContactType;
import com.example.bitemporal.aggregate.model.state.ContactState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "states")
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class ContactHead extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state")
    private Collection<ContactState> states;

    @ManyToOne
    @JoinColumn(name = "type")
    private ContactType type;

    private LocalDate validFrom;
    private LocalDate validTo;

}
