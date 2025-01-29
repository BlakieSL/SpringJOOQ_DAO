# SpringJOOQ_DAO Repository 🚀

A Spring-based project demonstrating seamless integration with JOOQ for type-safe, efficient database interactions.  
*Perfect blend of Spring's dependency injection and JOOQ's SQL power!*

---

## 📖 Overview

This repository showcases how to integrate **JOOQ** (Java Object Oriented Querying) with **Spring** to create robust, type-safe database interactions. Designed for developers seeking to leverage modern Java practices, it provides a foundation for building maintainable data access layers with compile-time SQL validation.

---

## ✨ Key Features

### 🔒 **Type-Safe SQL Queries**
- Write SQL queries checked at **compile-time** for syntax and type correctness.  
- Eliminate runtime errors caused by typos or mismatched data types.

### 🌱 **Spring Framework Integration**
- Leverage Spring's **dependency injection** for clean configuration.  
- Utilize Spring's **transaction management** for consistent data operations.

### 🐳 **Docker Support**
- Consistent database (e.g., PostgreSQL/MySQL) configuration across development, testing, and production.

### 🧩 **Extensible Architecture**
- Modular design for easy customization and scaling.  
- Clear separation of concerns between data access, business logic, and configuration.

---

## 🛠️ Built With
- **Spring Boot** - For application scaffolding and dependency management.  
- **JOOQ** - For type-safe SQL query building and execution.  
- **Docker** - For containerized database instances.  
- **Gradle** - For build automation.

---

### Setup & Run

```bash
clone repository

# Build Docker image for JOOQ database
./gradlew buildJOOQImage

# Start the database container
./gradlew startJOOQContainer

# Wait for database initialization
./gradlew waitForJOOQDB

# Generate type-safe JOOQ classes from DB schema
./gradlew generateJooq

# Run tests (optional)
./gradlew test
