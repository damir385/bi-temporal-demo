package com.example.bitemporal.aggregate.model.head;

import com.example.bitemporal.aggregate.model.state.ContractState;
import com.example.bitemporal.aggregate.model.state.DiscountState;
import com.example.bitemporal.aggregate.model.state.ItemState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
public class ContractHead extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state")
    private Collection<ContractState> states;

    private String number;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    @Filter(name="state")
    public Collection<ItemState> items;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    @Filter(name="state")
    public Collection<DiscountState> discounts;

}
