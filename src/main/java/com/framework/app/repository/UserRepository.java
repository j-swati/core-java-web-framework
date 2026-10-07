package com.framework.app.repository;

import com.framework.annotations.Repository;
import com.framework.app.model.User;
import com.framework.orm.GenericInMemoryRepository;

@Repository
public class UserRepository extends GenericInMemoryRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }
}

// repository -- generally responsible for accessing/storing data.
// data is stored in application's memory rather than a real database

// User.class is a Java Class object representing the User class