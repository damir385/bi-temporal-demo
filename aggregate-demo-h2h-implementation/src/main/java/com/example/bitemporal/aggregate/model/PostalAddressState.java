package com.example.bitemporal.aggregate.model;

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
public class PostalAddressState extends ContactState {

    private String zipCode;

    private String city;

    private String street;

    private String streetNumber;

}
