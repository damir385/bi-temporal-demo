package com.example.persistence.repository;

import com.example.persistence.api.model.Head;
import com.example.persistence.api.model.State;
import org.hibernate.Session;
import org.springframework.data.repository.core.EntityInformation;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

class RepositoryUtil {

    private RepositoryUtil() {
    }

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    public static final String DEFAULT_FILTER_NAME = "state";
    private static final String KEY_DATE_PARAMETER_NAME = "keyDate";

    public static void setFilterDefaults(EntityManager entityManager, LocalDate keyDate) {
        final Session session = entityManager.unwrap(Session.class);
        if (session.getEnabledFilter(DEFAULT_FILTER_NAME) == null)
            session.enableFilter(DEFAULT_FILTER_NAME);
        final Date date = Date.from(keyDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
        session.getEnabledFilter(DEFAULT_FILTER_NAME).setParameter(KEY_DATE_PARAMETER_NAME, date);

    }

    public static Set<Class<?>> getHeadReferences(Class<?> stateClass, EntityManager entityManager) {
        Metamodel metamodel = entityManager.getMetamodel();
        final Class<?> headClass = getHeadClass(stateClass, metamodel);
        Set<Class<?>> states = metamodel
                .entity(headClass)
                .getDeclaredPluralAttributes()
                .stream()
                .map(pluralAttribute -> pluralAttribute.getElementType().getJavaType())
                .filter(clazz -> !clazz.equals(stateClass))
                .collect(toSet());

        Set<Class<?>> subStates = states
                .stream()
                .flatMap(clazz -> getHeadReferences(clazz, entityManager).stream())
                .collect(toSet());

        states.addAll(subStates);
        return states;

    }

    public static Set<State<?>> getHeadReferenceValues(State<?> state, EntityManager entityManager) {
        Metamodel metamodel = entityManager.getMetamodel();
        final Class<?> stateClass = state.getClass();
        final Class<?> headClass = getHeadClass(stateClass, metamodel);
        SingularAttribute<?, ?> headAttribute = metamodel
                .entity(stateClass)
                .getDeclaredSingularAttributes()
                .stream()
                .filter(head -> Head.class.isAssignableFrom(head.getJavaType()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("Could not find a head for %s", stateClass.getName())));

        Object value = getValue(state, headAttribute);
        return null;
    }

    static State<?> saveState(State<?> state, EntityManager entityManager) {
        //if (entityInformation.isNew(state)) {
            entityManager.persist(state);
            return state;
      //  } else {
       //     return entityManager.merge(state);
       // }
    }

    private static Class<?> getHeadClass(Class<?> stateClass, Metamodel metamodel) {
        return metamodel
                .entity(stateClass)
                .getDeclaredSingularAttributes()
                .stream()
                .map(Attribute::getJavaType)
                .filter(Head.class::isAssignableFrom)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(format("Could not find a head for %s", stateClass.getName()))
                );
    }

    /**
     * Fetches the value of the given SingularAttribute on the given
     * entity.
     *
     * @see http://stackoverflow.com/questions/7077464/how-to-get-singularattribute-mapped-value-of-a-persistent-object
     */
    @SuppressWarnings("unchecked")
    public static Object getValue(Object entity, SingularAttribute<?, ?> field) {
        try {
            Member member = field.getJavaMember();
            if (member instanceof Method) {
                // this should be a getter method:
                return  ((Method)member).invoke(entity);
            } else if (member instanceof Field) {
                return ((Field)member).get(entity);
            } else {
                throw new IllegalArgumentException("Unexpected java member type. Expecting method or field, found: " + member);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
