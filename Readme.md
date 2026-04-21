# API Automation Framework – Fake Store API

A production-style REST API automation framework built with Java, RestAssured, and TestNG.
The project demonstrates how to design scalable, maintainable, and reliable API test systems rather than just writing test scripts.

---

## Architecture

| Layer     | Responsibility                               |
| --------- | -------------------------------------------- |
| Services  | API interactions via Service Object Model    |
| Models    | Request/response POJOs using Builder pattern |
| Utils     | Config handling, singleton TokenManager      |
| Factory   | Request specification builder                |
| Filters   | Retry logic, logging, reporting integration  |
| Listeners | Test lifecycle management + reporting hooks  |

---

## Key Engineering Decisions

* **Retry mechanism** – Automatically retries on 5xx and 429 responses to handle transient failures gracefully
* **ThreadLocal reporting** – Ensures thread-safe Extent report logging during parallel test runs
* **Separation of concerns** – Filters handle cross-cutting logic, listeners manage lifecycle, services encapsulate API calls
* **Environment switching** – Supports dynamic environments via Maven CLI (`-Denv=qa`)
* **Behavior-based validation** – Assertions based on actual API responses, accounting for mock API inconsistencies

---

## Features

* Service Object Model (SOM) for reusable API interactions
* Builder pattern for clean request payload construction
* Singleton TokenManager for centralized authentication handling
* API chaining using dynamic data across endpoints
* JSON schema validation
* Parallel execution using TestNG
* Retry mechanism for transient failures
* Extent HTML reporting with request/response logging
* Response time validation (basic performance checks)

---

## Tech Stack

Java · RestAssured · TestNG · Maven · Jackson · Extent Reports

---

## Prerequisites

* Java 11+
* Maven 3.6+

---

## How to Run

```bash
# Run all tests
mvn clean test

# Run against QA environment
mvn clean test -Denv=qa

# Run specific suite
mvn clean test -DsuiteXmlFile=testng.xml
```

---

## Report

Extent HTML report is generated at:

```
/reports/extent-report.html
```


Includes:

* Test execution status
* Request and response logs
* Failure details and retry traces

---

## Limitations

* Uses a public mock API (no data persistence between runs)
* Some endpoints return inconsistent responses; validations are designed accordingly

---

## Summary

This project focuses on building a production-ready API automation framework with emphasis on maintainability, scalability, and handling real-world API behavior.
