package com.example.bitemporal.aggregate.model.code;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
public class StuffItem extends AbstractUUIDPersistable {
    String detail;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stuff_id")
    private Stuff stuff;
}
