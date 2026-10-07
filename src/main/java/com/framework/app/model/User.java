package com.framework.app.model;

import com.framework.annotations.Entity;

@Entity
public class User extends BaseEntity{
    private String name;
    private String email;
    private Role role;

    public User(){
        super();
    }

    public User(Long id, String name, String email, Role role){
        super();
        setId(id);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}


// super() calls the constructor of the parent class.

// setId(id) instead of this.id = id? -- because id belongs to the parent class BaseEntity. ecause it's private, User cannot directly access it.


// User is an Entity that extends BaseEntity.
// It inherits id and createdAt from BaseEntity.
// @Entity marks the class for the framework to detect using reflection.
// The no-arg constructor is useful for framework/reflection-based object creation.
