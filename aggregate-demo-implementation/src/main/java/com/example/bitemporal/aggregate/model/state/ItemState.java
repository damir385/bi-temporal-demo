package com.example.bitemporal.aggregate.model.state;

import com.example.bitemporal.aggregate.model.head.ContractHead;
import com.example.bitemporal.aggregate.model.head.ItemHead;
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
public class ItemState extends AbstractUUIDPersistable implements State<ItemHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private ItemHead head;

    @ManyToOne
    private ContractHead contract;

    private String name;

    private Double price;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
