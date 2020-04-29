package com.example.bitemporal.aggregate.model.code;

import com.example.persistence.model.AbstractUUIDPersistable;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
public class Test extends AbstractUUIDPersistable {

    private String dummy;

    @Version
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "test")
    private Collection<Stuff> stuffs;
}
