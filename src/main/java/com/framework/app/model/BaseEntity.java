package com.framework.app.model;

import com.framework.annotations.Id;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    @Id
    private Long id;

    private LocalDateTime createdAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

// Whenever a subclass creates an object, the BaseEntity constructor runs as part of object construction.

// we will have many entities that will need -- id, createdAt ; Instead of writing this in every class, you put common functionality in: BaseEntity

// Each subclass automatically gets: id, createdAt, getId(), getCreatedAt(), setId()

// getDeclaredFields() doesn't include inherited fields.
// Framework must traverse the class hierarchy to find inherited fields like @Id.

