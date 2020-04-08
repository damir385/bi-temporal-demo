package com.example.persitence.repository;

import com.example.persitence.api.model.Head;
import com.example.persitence.api.repository.BusinessHistoryRepository;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryImpl;
import org.springframework.data.envers.repository.support.ReflectionRevisionEntityInformation;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.history.support.RevisionEntityInformation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class BusinessHistoryRepositoryResolver<H extends Head> extends SimpleJpaRepository<H, UUID> implements BusinessHistoryRepository<H> {

    private final JpaEntityInformation<H, ?> entityInformation;
    private final RevisionRepository<H, UUID, Long> revisionRepository;


    public BusinessHistoryRepositoryResolver(JpaEntityInformation<H, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.revisionRepository = new EnversRevisionRepositoryImpl<>(entityInformation,
                new ReflectionRevisionEntityInformation(DefaultRevisionEntity.class), entityManager);
    }


    public BusinessHistoryRepositoryResolver(Class<H> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Transactional
    @Override
    public H create(H head) {
        if (entityInformation.isNew(head)) {
            return save(head);
        }
        throw new RuntimeException("Head already exists");
    }

    @Override
    public H overwrite(H head) {
        deleteById(head.getId());
        return save(head);
    }

    @Override
    public H shift(H head, LocalDate from) {
        return null;
    }

    @Override
    public H shift(H head, Period period) {
        return null;
    }

    @Override
    public H addTimePoints(H head, Set<LocalDate> timePoints) {
        return null;
    }

    @Override
    public Optional<Revision<Long, H>> findLastChangeRevision(UUID uuid) {
        return revisionRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revisions<Long, H> findRevisions(UUID uuid) {
        return revisionRepository.findRevisions(uuid);
    }

    @Override
    public Page<Revision<Long, H>> findRevisions(UUID uuid, Pageable pageable) {
        return revisionRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Optional<Revision<Long, H>> findRevision(UUID uuid, Long revisionNumber) {
        return revisionRepository.findRevision(uuid, revisionNumber);
    }


}
