package com.framework.orm;

import com.framework.annotations.Id;
import com.framework.exception.EntityNotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class GenericInMemoryRepository<T, ID extends Long> {

    private final Map<Long, T> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Class<T> entityClass;

    public GenericInMemoryRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        try {
            Field idField = getIdField();
            idField.setAccessible(true);
            Long id = (Long) idField.get(entity);

            if (id == null) {
                id = idGenerator.getAndIncrement();
                idField.set(entity, id);
            }
            storage.put(id, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(ID id) {
        if (!storage.containsKey(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found");
        }
        storage.remove(id);
    }

    private Field getIdField() {
        return Arrays.stream(entityClass.getSuperclass() != null
                        ? concatFields(entityClass.getDeclaredFields(),
                        entityClass.getSuperclass().getDeclaredFields())
                        : entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No @Id field declared in " + entityClass.getSimpleName()));
    }

    private Field[] concatFields(Field[] a, Field[] b) {
        Field[] result = new Field[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}


// Generic repository that stores entities in memory

// storage -- this is where actual data is stored
// ConcurrentHashMap -- thread-safe implementation of Map, multiple threads can safely access the repository concurrently
// idGenerator -- generates IDs
// AtomicLong is also thread safe

// entityClass stores information about which class the repository is managing

// Field is Java's reflection representation of a field


// entityClass.getDeclaredFields() -- return s fields declared directly inside the class
// entityClass.getSuperclass().getDeclaredFields() -- get fields of super class
// concatFields() -- combine all current class and super class fields
// filter field with @Id annotation

// reflection allows u to override normal access check
// idField.setAccessible(true);

// ID extends Long -- ID must be Long or a subtype of Long.

// Class<T> -- A Java Class object representing the type T.
// User.class gives you a Class<User> object.


