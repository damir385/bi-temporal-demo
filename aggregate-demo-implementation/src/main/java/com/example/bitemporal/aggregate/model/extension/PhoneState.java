package com.example.bitemporal.aggregate.model.extension;

import com.example.bitemporal.aggregate.model.head.ContactHead;
import com.example.bitemporal.aggregate.model.state.ContactState;
import com.example.persistence.api.model.State;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class PhoneState extends ContactState implements State<ContactHead>{

    private String number;

}
