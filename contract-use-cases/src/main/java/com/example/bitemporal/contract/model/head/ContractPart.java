package com.example.bitemporal.contract.model.head;

import com.example.bitemporal.contract.model.state.ContractPartState;
import com.example.persistence.api.model.Head;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
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
public class ContractPart extends AbstractUUIDPersistable implements Head {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name = "state")
    private Collection<ContractPartState> states;

    private LocalDate validFrom;

    private LocalDate validTo;
}
