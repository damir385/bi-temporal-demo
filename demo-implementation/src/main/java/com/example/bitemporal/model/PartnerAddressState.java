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
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
public class PartnerAddressState extends AbstractUUIDPersistable implements State<PartnerAddressHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="head_id")
    private PartnerAddressHead head;

    private UUID partnerHeadId;

    private UUID addressHeadId;

    private LocalDate stateBegin;

    private LocalDate stateEnd;

}
