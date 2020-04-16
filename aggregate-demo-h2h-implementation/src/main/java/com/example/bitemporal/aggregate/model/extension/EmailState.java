package com.example.bitemporal.aggregate.model.extension;

import com.example.bitemporal.aggregate.model.state.ContactState;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
@SuppressWarnings("squid:S2160")
public class EmailState extends ContactState {

    @Email
    private String email;

}
