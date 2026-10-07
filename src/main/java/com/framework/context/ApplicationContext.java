package com.framework.context;

import com.framework.annotations.Inject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public ApplicationContext(Class<?>... components) {
        // Pass 1: instantiate every component and register it
        for (Class<?> component : components) {
            try {
                Object instance = component.getDeclaredConstructor().newInstance();
                beans.put(component, instance);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to instantiate bean: " + component.getName(), e);
            }
        }

        // Pass 2: inject @Inject fields
        for (Object bean : beans.values()) {
            injectDependencies(bean);
        }
    }

    private void injectDependencies(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Object dependency = beans.get(field.getType());
            if (dependency == null) {
                throw new IllegalStateException(
                        "No bean found for type " + field.getType().getName()
                                + " required by " + bean.getClass().getName());
            }

            try {
                field.setAccessible(true);
                field.set(bean, dependency);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                        "Failed to inject field " + field.getName()
                                + " in " + bean.getClass().getName(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        Object bean = beans.get(type);
        if (bean == null) {
            throw new IllegalStateException("No bean registered for type: " + type.getName());
        }
        return (T) bean;
    }
}

// component.getDeclaredConstructor().newInstance() -- using reflection to dynamically create objects

