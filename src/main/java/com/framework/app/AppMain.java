//package com.framework.app;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import tools.jackson.databind.json.JsonMapper;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//public class AppMain {
//
//    private static final Logger log = LoggerFactory.getLogger(AppMain.class);
//
//    public static void main(String[] args) {
//        log.info("=== starting test ===");
//
//        JsonMapper mapper = JsonMapper.builder().build();
//
//        Map<String, Object> payload = Map.of(
//                "status", 200,
//                "message", "OK",
//                "timestamp", LocalDateTime.now()
//        );
//
//        String json = mapper.writeValueAsString(payload);
//        System.out.println("Serialized  : " + json);
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> parsed = mapper.readValue(json, Map.class);
//        System.out.println("Deserialized: " + parsed);
//
//        log.info("=== Test Finished ===");
//    }
//}



package com.framework.app;

import com.framework.app.model.Role;
import com.framework.app.repository.UserRepository;
import com.framework.app.service.UserService;
import com.framework.context.ApplicationContext;
import com.framework.server.HttpServerEngine;

public class AppMain {

    public static void main(String[] args) {
        // 1. Initialize IoC container with registered components
        ApplicationContext context = new ApplicationContext(
                UserRepository.class,
                UserService.class
        );

        // 2. Pre-populate dummy records using the domain service
        UserService userService = context.getBean(UserService.class);
        userService.createUser("ABC", "abc@example.com", Role.ADMIN);
        userService.createUser("XYZ", "xyz@example.com", Role.USER);

        // 3. Boot the HTTP server engine (this call blocks)
        HttpServerEngine engine = new HttpServerEngine(context);
        engine.start();
    }
}