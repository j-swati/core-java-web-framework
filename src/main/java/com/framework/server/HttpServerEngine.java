package com.framework.server;

import com.framework.app.dto.CreateUserRequest;
import com.framework.app.dto.ErrorResponseDto;
import com.framework.app.service.UserService;
import com.framework.config.AppConfig;
import com.framework.context.ApplicationContext;
import com.framework.exception.EntityNotFoundException;
import com.framework.exception.ValidationException;
import com.framework.http.HttpMethod;
import com.framework.http.HttpRequest;
import com.framework.http.HttpResponse;
import com.framework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerEngine {

    private static final Logger log = LoggerFactory.getLogger(HttpServerEngine.class);

    private final int port;
    private final ExecutorService threadPool;
    private final ApplicationContext context;

    public HttpServerEngine(ApplicationContext context) {
        this.context = context;
        this.port = AppConfig.getInt("server.port", 8080);
        int poolSize = AppConfig.getInt("server.thread-pool-size", 10);
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        log.info("Starting Multi-Threaded HTTP Server Engine on port {}...", port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleConnection(clientSocket));
            }
        } catch (Exception e) {
            log.error("Fatal Server Engine Error", e);
        } finally {
            threadPool.shutdown();
        }
    }

    private void handleConnection(Socket socket) {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            routeRequest(request, response);
        } catch (Exception e) {
            log.error("Connection Handling Error", e);
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void routeRequest(HttpRequest request, HttpResponse response) {
        try {
            UserService userService = context.getBean(UserService.class);

            // GET /api/users
            if ("/api/users".equals(request.getPath())
                    && request.getMethod() == HttpMethod.GET) {
                var users = userService.getAllUsers();
                response.sendJson(HttpStatus.OK, users);
            }
            // GET /api/users/{id}
            else if (request.getPath().startsWith("/api/users/")
                    && request.getMethod() == HttpMethod.GET) {
                Long id = Long.parseLong(
                        request.getPath().substring("/api/users/".length()));
                var user = userService.getUserById(id);
                response.sendJson(HttpStatus.OK, user);
            }
            // POST /api/users
            else if ("/api/users".equals(request.getPath())
                    && request.getMethod() == HttpMethod.POST) {
                CreateUserRequest createRequest = request.getBodyAs(CreateUserRequest.class);
                var createdUser = userService.createUser(createRequest);
                response.sendJson(HttpStatus.CREATED, createdUser);
            }
            // Unknown route
            else {
                response.sendJson(HttpStatus.NOT_FOUND,
                        new ErrorResponseDto(404, "Not Found",
                                "Endpoint " + request.getPath() + " not found"));
            }

        } catch (ValidationException e) {
            log.warn("Validation Error: {}", e.getMessage());
            response.sendJson(e.getStatus(),
                    new ErrorResponseDto(
                            e.getStatus().getCode(), "Validation Error", e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.warn("Resource Not Found: {}", e.getMessage());
            response.sendJson(e.getStatus(),
                    new ErrorResponseDto(
                            e.getStatus().getCode(), "Resource Not Found", e.getMessage()));
        } catch (Exception e) {
            log.error("Global Catch - 500 Internal Server Error", e);
            response.sendJson(HttpStatus.INTERNAL_SERVER_ERROR,
                    new ErrorResponseDto(500, "Internal Server Error", e.getMessage()));
        }
    }
}

// port -- the server listens on this port
// threadPool -- allows server to handle multiple clients concurrently
// context -- object manager

// ServerSocket serverSocket = new ServerSocket(port) -- actual network listener
// while (!Thread.currentThread().isInterrupted()) -- Keep accepting clients until the server thread is interrupted.
// serverSocket.accept(); -- accept() waits until a client connects.


// threadPool.execute(() -> handleConnection(clientSocket)) -- run handleConnection(clientSocket) on a thread from the thread pool