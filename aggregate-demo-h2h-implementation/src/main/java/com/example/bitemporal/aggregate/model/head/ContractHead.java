package com.example.bitemporal.aggregate.model.head;

import com.example.bitemporal.aggregate.model.state.ContractState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "person")
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class ContractHead extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state")
    private Collection<ContractState> states;

    @ManyToOne
    private PersonHead person;

    private String number;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    public Collection<ItemHead> items;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    public Collection<DiscountHead> discounts;

}
