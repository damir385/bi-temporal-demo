package com.example.persitence.model;

import com.example.persitence.api.model.Head;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
public abstract class AbstractUUIDPersistable extends AbstractPersistable<UUID>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
}

