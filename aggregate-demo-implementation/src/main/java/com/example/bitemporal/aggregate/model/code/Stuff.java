package com.example.bitemporal.aggregate.model.code;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
public class Stuff extends AbstractUUIDPersistable {

    private String info;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stuff")
    private Collection<StuffItem> items;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private Test test;
}
