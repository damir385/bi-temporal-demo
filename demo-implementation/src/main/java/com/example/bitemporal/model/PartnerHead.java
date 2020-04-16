package com.example.bitemporal.model;


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
@ToString(callSuper = true)
@Entity
@Audited
@FilterDef(name="state", parameters={@ParamDef(name="keyDate", type="timestamp")})
public class PartnerHead extends AbstractUUIDPersistable implements Head {

    private String birthName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "head")
    @Filter(name="state", condition=":keyDate BETWEEN state_begin AND state_end")
    private Collection<PartnerState> states;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "partnerHeadId")
    public Collection<PartnerAddressState> addresses;


}
