package com.example.bitemporal.aggregate.model;

import com.example.persitence.api.model.State;
import com.example.persitence.model.AbstractUUIDPersistable;
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
@ToString(callSuper = true, exclude = "head")
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class ContractState  extends AbstractUUIDPersistable implements State<ContractHead>{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ContractHead head;

    private String description;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
