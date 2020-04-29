package com.example.persistence.repository;

import com.example.persistence.api.model.Aggregate;
import com.example.persistence.api.model.Head;
import com.example.persistence.api.model.State;
import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.util.ProxyUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class Merger {

    public static <T extends AbstractPersistable<UUID>> void doMerge(T persisted, T modified) {

        if (!ProxyUtils.getUserClass(persisted).equals(ProxyUtils.getUserClass(modified))) {
            throw new IllegalArgumentException(format("Can not merge %s with %s!", ProxyUtils.getUserClass(persisted), ProxyUtils.getUserClass(modified)));
        }

        if (persisted.getId() == null || !persisted.getId().equals(modified.getId())) {
            throw new IllegalArgumentException(format("Can not merge %s with %s! There is an id mismatch.", persisted, modified));
        }

        final Class<?> clazz = ProxyUtils.getUserClass(persisted);

        if (clazz.isAssignableFrom(Aggregate.class)) {
            checkAndDoMerge((Aggregate) persisted, (Aggregate) modified);
        } else if (clazz.isAssignableFrom(Head.class)) {
            doHeadMerge((Head) persisted, (Head) modified);
        } else if (clazz.isAssignableFrom(State.class)) {
            doStateMerge((State<?>) persisted, (State<?>) modified);
        } else {
            doBasicMerge(persisted, modified);
        }

    }

    private static <T extends AbstractPersistable<UUID>> void doBasicMerge(T persisted, T modified) {
        PropertyDescriptor[] collectionAttributeDescriptors = Stream.of(BeanUtils.getPropertyDescriptors(ProxyUtils.getUserClass(persisted)))
                .filter(propertyDescriptor -> propertyDescriptor.getPropertyType().isAssignableFrom(Collection.class))
                .toArray(PropertyDescriptor[]::new);
        PropertyDescriptor[] objectAttributeDescriptors = Stream.of(BeanUtils.getPropertyDescriptors(ProxyUtils.getUserClass(persisted)))
                .filter(propertyDescriptor -> !BeanUtils.isSimpleProperty(propertyDescriptor.getPropertyType()))
                .toArray(PropertyDescriptor[]::new);
        String[] collectionAttributeNames = Stream.of(collectionAttributeDescriptors).map(PropertyDescriptor::getName).toArray(String[]::new);
        String[] objectAttributeNames = Stream.of(objectAttributeDescriptors).map(PropertyDescriptor::getName).toArray(String[]::new);

        BeanUtils.copyProperties(persisted, modified, collectionAttributeNames);

        Collection<Collection<T>> collectionValues = Stream
                .of(collectionAttributeDescriptors)
                .map(property -> mergeCollections(retrieveValue(property, persisted), retrieveValue(property, modified)))
                .collect(Collectors.toSet());
        Seq.of(collectionAttributeDescriptors)
                .zip(collectionValues)
                .forEach(Unchecked.consumer(attribute -> attribute.v1.getWriteMethod().invoke(persisted, attribute.v2())));
    }

    private static <T extends AbstractPersistable<UUID>> Collection<T> retrieveValue(PropertyDescriptor property, T object) {
        try {
            return (Collection<T>) property.getReadMethod().invoke(object);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void checkAndDoMerge(Aggregate persisted, Aggregate modified) {
        if (persisted.getVersion() == null || !persisted.getVersion().equals(modified.getVersion())) {
            throw new IllegalArgumentException(format("Can not merge %s with %s! There is a version mismatch.", persisted, modified));
        }
        doHeadMerge(persisted, modified);
    }

    private static <T extends AbstractPersistable<UUID>> void doHeadMerge(Head persisted, Head modified) {
        doBasicMerge((T) persisted, (T) modified);

    }


    private static <T extends AbstractPersistable<UUID>> void doStateMerge(State<?> persisted, State<?> modified) {
        doBasicMerge((T) persisted, (T) modified);
    }

    private static <T extends AbstractPersistable<UUID>> T mergeAndReturn(T persisted, T modified) {
        doMerge(persisted, modified);
        return persisted;
    }

    private static <T extends AbstractPersistable<UUID>> Collection<T> mergeCollections(Collection<T> target, Collection<T> source) {
        Map<UUID, T> mappedTarget = mapCollection(target);
        Map<UUID, T> mappedSource = mapCollection(source);
        Map<UUID, T> output = mapCollection(target);

        mappedSource.entrySet()
                .stream()
                .map(entry ->
                        Optional
                                .ofNullable(mappedTarget.get(entry.getKey()))
                                .map(t -> mergeAndReturn(t, entry.getValue()))
                                .orElse(entry.getValue())

                )
                .forEach(merged -> output.put(merged.getId(), merged));
        return output.values();
    }

    private static <T extends AbstractPersistable<UUID>> Map<UUID, T> mapCollection(Collection<T> collectionToMap) {
        return collectionToMap
                .stream()
                .collect(
                        Collectors.toMap(
                                item ->
                                        Optional.ofNullable(item.getId()).orElseGet(UUID::randomUUID),
                                Function.identity()
                        )
                );
    }


}
