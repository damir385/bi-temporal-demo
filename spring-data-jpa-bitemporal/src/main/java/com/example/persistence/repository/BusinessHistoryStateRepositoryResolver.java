package com.example.persistence.repository;

import com.example.persistence.api.model.State;
import com.example.persistence.api.model.TimeStretchOption;
import com.example.persistence.api.repository.BusinessHistoryStateRepository;
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
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;

import static com.example.persistence.repository.RepositoryUtil.*;
import static java.lang.String.format;

@Validated
@Transactional(readOnly = true)
@NoRepositoryBean
public class BusinessHistoryStateRepositoryResolver<S extends State<?>> extends SimpleJpaRepository<S, UUID> implements BusinessHistoryStateRepository<S> {

    private static final class NotImplemented extends RuntimeException {
    }

    private final JpaEntityInformation<S, ?> entityInformation;
    private final RevisionRepository<S, UUID, Long> revisionRepository;

    private final EntityManager em;

    public BusinessHistoryStateRepositoryResolver(JpaEntityInformation<S, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.revisionRepository = new EnversRevisionRepositoryImpl<>(entityInformation,
                new ReflectionRevisionEntityInformation(DefaultRevisionEntity.class), entityManager);
        this.em = entityManager;
    }

    public BusinessHistoryStateRepositoryResolver(Class<S> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }


    @Override
    public Optional<S> findOneByKeyDate(UUID headId, LocalDate keyDate) {
        setFilterDefaults(em, keyDate);
        return Optional.ofNullable(
                (S) em.createQuery(format("SELECT s FROM %s s WHERE head_id = :headId AND :keyDate BETWEEN state_begin AND state_end", entityInformation.getEntityName()))
                        .setParameter("headId", headId)
                        .setParameter("keyDate", keyDate)
                        .getSingleResult());
    }

    @Override
    public <T extends S> T save(T entity) {
        if (entityInformation.isNew(entity) && entity.getHead().isNew()) return super.save(entity);
        throw new NotImplemented();
    }

    @Override
    public Optional<Revision<Long, S>> findLastChangeRevision(UUID uuid) {
        return revisionRepository.findLastChangeRevision(uuid);
    }

    @Override
    public Revisions<Long, S> findRevisions(UUID uuid) {
        return revisionRepository.findRevisions(uuid);
    }

    @Override
    public Page<Revision<Long, S>> findRevisions(UUID uuid, Pageable pageable) {
        return revisionRepository.findRevisions(uuid, pageable);
    }

    @Override
    public Optional<Revision<Long, S>> findRevision(UUID uuid, Long revisionNumber) {
        return revisionRepository.findRevision(uuid, revisionNumber);
    }
}
