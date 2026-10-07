package com.framework.app.dto;

import com.framework.app.model.Role;

public record CreateUserRequest(String name, String email, Role role) {
}


// DTO -- Data Transfer Object -- commonly used to carry data between different parts of an application, such as from an HTTP request into your application.

// Its like pojo class with
// name  , name  , role   fields && all args constructor

//CreateUserRequest request =
//    new CreateUserRequest(
//            "Swati",
//            "swati@gmail.com",
//            Role.USER
//    );

//System.out.println(request.name());
//System.out.println(request.email());
//System.out.println(request.role());
