package com.example.bitemporal.aggregate.model;

import com.example.persitence.api.model.Head;
import com.example.persitence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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
    @Filter(name="state", condition=":validityPoint state_begin AND state_end")
    private Collection<ContactState> states;

    @ManyToOne
    @JoinColumn(name = "type")
    private ContactType type;

}
