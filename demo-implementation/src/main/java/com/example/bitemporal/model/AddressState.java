package com.example.bitemporal.model;


import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
public class AddressState extends AbstractUUIDPersistable implements State<AddressHead> {

    private String zipCode;
    private String street;
    private String buildingNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="head_id")
    private AddressHead head;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
