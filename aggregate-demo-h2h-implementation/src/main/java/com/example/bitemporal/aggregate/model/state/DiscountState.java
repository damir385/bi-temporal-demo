package com.example.bitemporal.aggregate.model.state;

import com.example.bitemporal.aggregate.model.head.DiscountHead;
import com.example.persistence.api.model.State;
import com.example.persistence.model.AbstractUUIDPersistable;
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
@ToString(callSuper = true, exclude="head")
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class DiscountState extends AbstractUUIDPersistable implements State<DiscountHead>{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "head_id")
    private DiscountHead head;

    private String reason;

    private Double value;

    private LocalDate stateBegin;

    private LocalDate stateEnd;
}
