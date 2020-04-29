package com.example.bitemporal.contract.model.state;

import com.example.bitemporal.contract.model.head.Contract;
import com.example.bitemporal.contract.model.head.ContractPart;
import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
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
@ToString(callSuper = true, exclude = "contract")
@Entity
@Audited
@Filter(name="stateFilter")
@SuppressWarnings("squid:S2160")
public class ContractPartState extends AbstractUUIDPersistable implements State<ContractPart> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ContractPart head;

    @ManyToOne
    private Contract contract;

    private Double premium;

    private String description;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
