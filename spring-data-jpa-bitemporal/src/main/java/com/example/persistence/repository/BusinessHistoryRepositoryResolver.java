package com.example.persistence.repository;

import com.example.persistence.api.model.Head;
import com.example.persistence.api.repository.BusinessHistoryRepository;
import org.hibernate.Session;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryImpl;
import org.springframework.data.envers.repository.support.ReflectionRevisionEntityInformation;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;

@Validated
@Transactional(readOnly = true)
public class BusinessHistoryRepositoryResolver<H extends Head> extends SimpleJpaRepository<H, UUID> implements BusinessHistoryRepository<H> {

    private static final ZoneId defaultZone = ZoneId.systemDefault();
    private static final String DEFAULT_FILTER_NAME = "state";

    private final JpaEntityInformation<H, ?> entityInformation;
    private final RevisionRepository<H, UUID, Long> revisionRepository;

    private final EntityManager em;


    public BusinessHistoryRepositoryResolver(JpaEntityInformation<H, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.revisionRepository = new EnversRevisionRepositoryImpl<>(entityInformation,
                new ReflectionRevisionEntityInformation(DefaultRevisionEntity.class), entityManager);
        this.em = entityManager;
    }

    @Transactional
    public void setFilterDefaults() {
        final Session session = em.unwrap(Session.class);
        if (session.getEnabledFilter(DEFAULT_FILTER_NAME) == null)
            em.unwrap(Session.class).enableFilter(DEFAULT_FILTER_NAME);
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
        setFilterDefaults();
        final Date date = Date.from(keyDate.atStartOfDay(defaultZone).toInstant());
        em.unwrap(Session.class).getEnabledFilter("state").setParameter("keyDate", date);
        Optional<H> head = Optional.ofNullable((H) em.createQuery(format("SELECT h FROM %s h where id = :id", entityInformation.getEntityName())).setParameter("id", id).getSingleResult());
        return head;

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
