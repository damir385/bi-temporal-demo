package com.example.bitemporal.aggregate.model;

import com.example.persitence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class ContactType extends AbstractUUIDPersistable {

    private String description;

}
