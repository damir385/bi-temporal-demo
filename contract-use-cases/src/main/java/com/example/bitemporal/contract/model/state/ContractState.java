package com.example.bitemporal.contract.model.state;

import com.example.bitemporal.contract.model.head.Contract;
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
@Entity
@Audited
@Filter(name="stateFilter")
@SuppressWarnings("squid:S2160")
public class ContractState extends AbstractUUIDPersistable implements State<Contract> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private Contract head;

    private Double premium;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
