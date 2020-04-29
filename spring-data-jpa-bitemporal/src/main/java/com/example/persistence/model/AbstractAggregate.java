package com.example.persistence.model;

import com.example.persistence.api.model.Aggregate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
public abstract class AbstractAggregate extends AbstractUUIDPersistable implements Aggregate {

    @Version
    protected Long version;
}
