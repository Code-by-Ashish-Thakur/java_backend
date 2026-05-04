# Student REST API — Learning Project

A simple Spring Boot REST API to learn backend development step by step.

---

## Project Structure

```
BACKEND/
├── pom.xml                              ← Maven config (dependencies)
├── src/main/java/com/learning/api/
│   ├── ApiApplication.java              ← START HERE (main method)
│   ├── model/
│   │   └── Student.java                 ← Data class (what a Student looks like)
│   ├── service/
│   │   └── StudentService.java          ← Business logic (add, delete, find)
│   └── controller/
│       └── StudentController.java       ← REST endpoints (handles HTTP requests)
└── src/main/resources/
    └── application.properties           ← App settings
```

## Reading Order (Learn Step by Step)

1. **`Student.java`** — Understand the data model first
2. **`StudentService.java`** — See how data is stored and manipulated
3. **`StudentController.java`** — See how HTTP requests map to service methods
4. **`ApiApplication.java`** — The entry point that starts everything

---

## How to Run

```bash
# From the BACKEND folder, run:
mvn spring-boot:run
```

Or right-click `ApiApplication.java` in your IDE and select **Run**.

The server starts at: **http://localhost:8080**

---

## Test the API (using curl or Postman)

### 1. Create a Student (POST)
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name": "Rahul", "email": "rahul@email.com"}'
```
**Response:** `{"id":1,"name":"Rahul","email":"rahul@email.com"}`

### 2. Get All Students (GET)
```bash
curl http://localhost:8080/api/students
```
**Response:** `[{"id":1,"name":"Rahul","email":"rahul@email.com"}]`

### 3. Get One Student (GET)
```bash
curl http://localhost:8080/api/students/1
```

### 4. Update a Student (PUT)
```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Rahul Kumar", "email": "rahul.kumar@email.com"}'
```

### 5. Delete a Student (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/students/1
```

---

## Key Concepts to Learn

| Concept | Where | What it does |
|---------|-------|-------------|
| `@SpringBootApplication` | ApiApplication.java | Starts the app, enables auto-config |
| `@RestController` | StudentController.java | Marks class as REST API handler |
| `@RequestMapping` | StudentController.java | Sets base URL path |
| `@GetMapping` | StudentController.java | Handles GET requests |
| `@PostMapping` | StudentController.java | Handles POST requests |
| `@PutMapping` | StudentController.java | Handles PUT requests |
| `@DeleteMapping` | StudentController.java | Handles DELETE requests |
| `@PathVariable` | StudentController.java | Extracts value from URL (e.g., /students/**1**) |
| `@RequestBody` | StudentController.java | Converts JSON body → Java object |
| `@Service` | StudentService.java | Marks class as business logic bean |
| `ResponseEntity` | StudentController.java | Lets you control HTTP status codes |

---

## Next Steps (After You're Comfortable)

1. **Add a Database** — Use Spring Data JPA + H2 (in-memory database)
2. **Add Validation** — Use `@Valid` and `@NotBlank` annotations
3. **Add Exception Handling** — Use `@ControllerAdvice` for global error handling
4. **Add Swagger/OpenAPI** — Auto-generate API documentation
5. **Add Security** — Use Spring Security for authentication
