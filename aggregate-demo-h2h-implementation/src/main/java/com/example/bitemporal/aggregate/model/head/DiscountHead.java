package com.example.bitemporal.aggregate.model.head;

import com.example.bitemporal.aggregate.model.state.DiscountState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "contract")
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class DiscountHead extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name = "state")
    private Collection<DiscountState> states;


    @ManyToOne
    private ContractHead contract;

}