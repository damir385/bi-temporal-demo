package com.example.persistence.repository;

import com.example.persistence.api.repository.BusinessHistoryHeadRepository;
import com.example.persistence.api.repository.BusinessHistoryStateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.util.UUID;


public class BusinessRepositoryFactoryBean <R extends JpaRepository<T, UUID>, T> extends JpaRepositoryFactoryBean<R, T, UUID> {

    private static final Class<?> BUSINESS_HISTORY_HEAD_REPOSITORY_INTERFACE = BusinessHistoryHeadRepository.class;
    private static final Class<?> BUSINESS_HISTORY_STATE_REPOSITORY_INTERFACE = BusinessHistoryStateRepository.class;


    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BusinessRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager)
    {
        return new BusinessRepositoryFactory(entityManager);
    }

    private static class BusinessRepositoryFactory<T> extends JpaRepositoryFactory
    {
        BusinessRepositoryFactory(final EntityManager entityManager)
        {
            super(entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata)
        {
            final Class<?> interfaceClass = metadata.getRepositoryInterface();
            if (BUSINESS_HISTORY_HEAD_REPOSITORY_INTERFACE.isAssignableFrom(interfaceClass))
                return BUSINESS_HISTORY_HEAD_REPOSITORY_INTERFACE;
            if (BUSINESS_HISTORY_STATE_REPOSITORY_INTERFACE.isAssignableFrom(interfaceClass))
                return BUSINESS_HISTORY_STATE_REPOSITORY_INTERFACE;
           else return super.getRepositoryBaseClass(metadata);
        }

        @Override
        @SuppressWarnings({ "unchecked", "rawtypes" })
        protected SimpleJpaRepository<T, UUID> getTargetRepository(final RepositoryInformation metadata, final EntityManager entityManager)
        {
            final Class<?> interfaceClass = metadata.getRepositoryInterface();
            if (BUSINESS_HISTORY_HEAD_REPOSITORY_INTERFACE.isAssignableFrom(interfaceClass))
                return new BusinessHistoryHeadRepositoryResolver(metadata.getDomainType(), entityManager);
            if (BUSINESS_HISTORY_STATE_REPOSITORY_INTERFACE.isAssignableFrom(interfaceClass))
                return new BusinessHistoryStateRepositoryResolver(metadata.getDomainType(), entityManager);
            else return (SimpleJpaRepository<T, UUID>) super.getTargetRepository(metadata, entityManager);
        }
    }
}