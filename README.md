# Virtual Threads and Structured Task Scopes Demo

This project demonstrates different approaches to handling concurrent operations in Spring Boot, comparing synchronous code, CompletableFuture pitfalls, and the modern approach using virtual threads with StructuredTaskScope.

## Purpose

The project showcases three different patterns for handling concurrent operations:

1. **Synchronous Code** - Traditional blocking approach
2. **CompletableFuture Pitfalls** - Common issues with async programming
3. **Virtual Threads with StructuredTaskScope** - Modern Java 21+ approach

## Endpoints

### Synchronous Endpoints

- `GET /api/speakers` - Retrieves speakers synchronously
- `GET /api/talks` - Retrieves speakers and their talks sequentially (blocking)

### CompletableFuture Endpoints

- `GET /api/speakers-async` - Retrieves speakers asynchronously
- `GET /api/talks-async` - Demonstrates CompletableFuture pitfalls with nested futures

### Virtual Threads with StructuredTaskScope

- `GET /api/talks-optimized` - Uses StructuredTaskScope for concurrent talk retrieval
- `GET /api/talks-optimized-refactored` - Uses custom CollectionTaskScope for cleaner code

## CompletableFuture Pitfalls

The `/api/talks-async` endpoint demonstrates common issues with CompletableFuture:

- **Blocking in async context**: Using `join()` inside `thenApply()` blocks the thread
- **Nested futures complexity**: Managing multiple futures becomes error-prone
- **Error handling**: Difficult to handle partial failures
- **Resource management**: No structured lifecycle for concurrent tasks

## Virtual Threads and StructuredTaskScope Benefits

The optimized endpoints showcase advantages of StructuredTaskScope:

- **Structured concurrency**: Automatic lifecycle management with try-with-resources
- **Virtual threads**: Lightweight threads that don't block platform threads
- **Error handling**: Built-in support for handling partial failures
- **Readable code**: Sequential-looking code that executes concurrently
- **Resource safety**: Automatic cleanup when scope closes

## Requirements

- Java 21+
- Spring Boot 3.5.7+
- Preview features enabled (configured in `build.gradle`)

## Running the Application

```bash
./gradlew bootRun
```

The application will start on the default Spring Boot port (typically 8080).

