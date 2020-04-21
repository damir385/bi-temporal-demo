package com.example.persistence.repository;

import com.example.persistence.api.model.Head;
import com.example.persistence.api.repository.BusinessHistoryHeadRepository;
import org.hibernate.Session;
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
import java.time.Period;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.example.persistence.repository.RepositoryUtil.*;
import static java.lang.String.format;

@Validated
@Transactional(readOnly = true)
@NoRepositoryBean
public class BusinessHistoryHeadRepositoryResolver<H extends Head> extends SimpleJpaRepository<H, UUID> implements BusinessHistoryHeadRepository<H> {



    private final JpaEntityInformation<H, ?> entityInformation;
    private final RevisionRepository<H, UUID, Long> revisionRepository;

    private final EntityManager em;


    public BusinessHistoryHeadRepositoryResolver(JpaEntityInformation<H, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.revisionRepository = new EnversRevisionRepositoryImpl<>(entityInformation,
                new ReflectionRevisionEntityInformation(DefaultRevisionEntity.class), entityManager);
        this.em = entityManager;
    }

    public BusinessHistoryHeadRepositoryResolver(Class<H> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Transactional
    @Override
    public H create(H head) {
        if (entityInformation.isNew(head)) {
            return saveAndFlush(head);
        }
        throw new RuntimeException("Head already exists");
    }

    @Transactional
    @Override
    public H overwrite(H head) {
        deleteById(head.getId());
        return saveAndFlush(head);
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
    @Transactional(readOnly = true)
    public Optional<H> findOneByIdAndKeyDate(UUID id, LocalDate keyDate) {
        setFilterDefaults(em, keyDate);
        return Optional.ofNullable((H) em.createQuery(format("SELECT h FROM %s h where id = :id", entityInformation.getEntityName())).setParameter("id", id).getSingleResult());
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
