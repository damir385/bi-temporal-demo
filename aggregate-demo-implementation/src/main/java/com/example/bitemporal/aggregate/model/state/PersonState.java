package com.example.bitemporal.aggregate.model.state;

import com.example.bitemporal.aggregate.model.head.PersonHead;
import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = "head")
@Entity
@Audited
@FilterDef(name="stateFilter",   parameters = {@ParamDef(name="keyDate", type="timestamp")},
        defaultCondition = ":keyDate BETWEEN state_begin AND state_end")
@Filter(name="stateFilter", condition = ":keyDate BETWEEN state_begin AND state_end")
@SuppressWarnings("squid:S2160")
public class PersonState extends AbstractUUIDPersistable implements State<PersonHead> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private PersonHead head;

    private String firstName;

    private String lastName;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
