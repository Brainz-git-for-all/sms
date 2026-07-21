 # SMS Backend — Complete Beginner's Guide

This document explains everything about this project from scratch.
If you are new to Spring Boot, read Section 1 first before looking at the code.

---

## Table of Contents

1. What is Spring Boot and How Does It Work?
2. The Annotations Explained (The "Magic" Words)
3. The 5 Layers of This Project
4. The Entities (Database Tables)
5. How a Request Travels Through the App
6. The Standard API Response Format
7. All API Endpoints With Examples
8. Recommended Creation Order for Testing
9. How Relationships Work in the Database
10. Grade Calculation Logic
11. Common Errors and What They Mean

---

---

# SECTION 1 — What is Spring Boot and How Does It Work?

## The Simple Explanation

Imagine you want to build a restaurant. You need:
- A **front door** where customers walk in and place orders (Controller)
- A **kitchen** that prepares the food (Service)
- A **storage room** where ingredients are kept (Database)
- A **waiter** who fetches things from storage (Repository)

Spring Boot is a framework that helps you build all of this for software.
Instead of writing 500 lines of setup code, Spring Boot handles everything automatically.
You just write the important parts — the logic — and Spring Boot wires everything together.

## What Happens When You Start the App?

When you run the Spring Boot application:

1. Spring scans all your Java files looking for special annotations (like @Entity, @Service, etc.)
2. It reads your `application.properties` file to find the database connection details
3. It connects to your PostgreSQL database (running in Docker)
4. Hibernate (the database tool) looks at all your @Entity classes and automatically creates
   the database tables if they don't exist (because of `ddl-auto=update`)
5. Spring sets up all your REST endpoints so they are ready to receive HTTP requests
6. The application starts listening on port 8080

After that, your API is live and ready at: http://localhost:8080

---

---

# SECTION 2 — The Annotations Explained

Annotations are the words that start with `@`. They are instructions to Spring Boot.
Think of them as labels that tell Spring what each class or method is supposed to do.

---

## Annotations on Entities (Database Classes)

### @Entity
```java
@Entity
public class Student { ... }
```
This tells Hibernate: "This Java class represents a table in the database."
When the app starts, Hibernate will create a table called `student` in PostgreSQL
with columns matching all the fields inside this class.

### @Id
```java
@Id
private Long id;
```
This tells Hibernate: "This field is the Primary Key of the table."
Every row in the database must have a unique ID.

### @GeneratedValue(strategy = GenerationType.IDENTITY)
```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
This tells the database: "You are responsible for generating the ID automatically."
When you save a new student, you do NOT provide an ID. The database assigns 1, 2, 3, 4...
automatically. This is the same as AUTO_INCREMENT in MySQL.

### @ManyToOne
```java
@ManyToOne
private Guardian guardian;
```
This tells Hibernate: "Many students can have ONE guardian."
In the database, this becomes a foreign key column called `guardian_id`
that stores the ID of the guardian record.

Example in the database table:
```
student table:
| id | firstName | guardian_id |
|----|-----------|-------------|
|  1 | Lamin     |           3 |   ← Lamin's guardian has ID 3
|  2 | Fatou     |           3 |   ← Fatou has the same guardian
|  3 | Omar      |           1 |   ← Omar's guardian has ID 1
```

---

## Lombok Annotations (Code Shortcuts)

Lombok is a library that generates repetitive Java code for you automatically.
Without Lombok, you would have to write getters, setters, and constructors by hand.

### @Data
```java
@Data
public class Student { ... }
```
Generates ALL of these automatically:
- `getFirstName()`, `setFirstName()` for every field
- `equals()` — to compare two objects
- `hashCode()` — used internally by Java collections
- `toString()` — to print the object as a string

### @NoArgsConstructor
```java
@NoArgsConstructor
public class Student { ... }
```
Generates: `public Student() {}`
An empty constructor with no parameters. Hibernate requires this to create objects.

### @AllArgsConstructor
```java
@AllArgsConstructor
public class Student { ... }
```
Generates a constructor with ALL fields as parameters:
`public Student(Long id, String firstName, String lastName, ...) {}`

### @RequiredArgsConstructor
```java
@RequiredArgsConstructor
public class StudentService { ... }
```
Used in Services and Controllers. Generates a constructor for all `final` fields.
This is how Spring "injects" (provides) the Repository into the Service automatically.
You will see this used instead of `@Autowired`.

---

## Spring Boot Annotations (The Main Ones)

### @RestController
```java
@RestController
public class StudentController { ... }
```
Tells Spring: "This class handles HTTP requests."
The "Rest" part means all methods in this class automatically convert their
return values to JSON format. You do not need to do any conversion yourself.

### @RequestMapping("/api/students")
```java
@RequestMapping("/api/students")
public class StudentController { ... }
```
Sets the base URL path for ALL methods in this controller.
Every method inside this class will start with `/api/students`.

### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
```java
@GetMapping         // HTTP GET   — used for reading data
@PostMapping        // HTTP POST  — used for creating new data
@PutMapping         // HTTP PUT   — used for updating existing data
@DeleteMapping      // HTTP DELETE — used for deleting data
```
These go on individual methods inside a Controller.
They tell Spring which HTTP method triggers which Java method.

Example:
```java
@GetMapping("/{id}")           // GET /api/students/1
public ResponseEntity<...> getById(@PathVariable Long id) { ... }

@PostMapping                   // POST /api/students
public ResponseEntity<...> create(@RequestBody StudentRequest request) { ... }
```

### @PathVariable
```java
@GetMapping("/{id}")
public ResponseEntity<...> getById(@PathVariable Long id) { ... }
```
When the URL is `/api/students/5`, the `{id}` in the URL becomes the
`id` variable in the method. So `id` will equal `5`.

### @RequestBody
```java
public ResponseEntity<...> create(@RequestBody StudentRequest request) { ... }
```
Tells Spring: "Take the JSON that the frontend sent in the request body
and convert it into this Java object automatically."

The frontend sends:
```json
{ "firstName": "Lamin", "lastName": "Jallow" }
```
Spring turns it into: `StudentRequest` object with firstName="Lamin", lastName="Jallow"

### @RequestParam
```java
@PutMapping("/{id}/promote")
public ResponseEntity<...> promote(@PathVariable Long id, @RequestParam Long newClassId) { ... }
```
Reads a value from the URL query string.
Example URL: `/api/students/1/promote?newClassId=3`
The `newClassId` variable will equal `3`.

### @Service
```java
@Service
public class StudentService { ... }
```
Tells Spring: "This class contains business logic."
Spring will manage this class and make it available for injection into Controllers.

### @Repository
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> { }
```
Tells Spring: "This interface talks to the database."
Spring Data JPA will automatically implement all CRUD methods for you.
You never write SQL. Spring generates it.

### @RestControllerAdvice
```java
@RestControllerAdvice
public class GlobalExceptionHandler { ... }
```
Tells Spring: "This class handles errors from ALL controllers."
If any controller throws an exception, Spring will route it here
so you can return a clean JSON error response instead of a stack trace.

### @ExceptionHandler
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<...> handleNotFound(ResourceNotFoundException ex) { ... }
```
Inside the GlobalExceptionHandler, this tells Spring which exception
this specific method should handle. When a `ResourceNotFoundException`
is thrown anywhere in the app, this method runs and returns a 404 response.

---

## Validation Annotations (Input Checking)

These go on fields inside the `*Request` classes.
They check the data BEFORE it reaches the Service or Database.

```java
@NotBlank   — field must not be null or empty ("" or "   " also fails)
@NotNull    — field must not be null (but can be empty for Strings)
@Email      — field must be a valid email format (must contain @ and a domain)
@Min(0)     — number must be 0 or greater
@Max(20)    — number must be 20 or less
```

### @Valid
```java
public ResponseEntity<...> create(@Valid @RequestBody StudentRequest request) { ... }
```
This goes in the Controller method. It tells Spring to RUN the validation
checks (the @NotBlank, @Email, etc.) on the request object before the method executes.
Without @Valid, all those annotations are completely ignored.

If validation fails, Spring automatically returns a 400 Bad Request response
with the error messages. Your code never even runs.

IMPORTANT: @Valid only works if you have this in your pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
This dependency IS included in this project.

---

---

# SECTION 3 — The 5 Layers of This Project

Every feature (Student, Teacher, Grade, etc.) follows the same 5-layer pattern.
Understanding this pattern is the key to understanding the whole project.

```
REQUEST FROM FRONTEND (JSON)
         ↓
  [ 1. Controller ]   ← Receives the HTTP request, validates input
         ↓
  [ 2. Service    ]   ← Contains all the business logic
         ↓
  [ 3. Repository ]   ← Talks to the database (Spring generates the SQL)
         ↓
  [ 4. Entity     ]   ← Represents a row in the database table
         ↑
  [ 5. DTO        ]   ← Shapes what goes IN (Request) and what comes OUT (Response)

RESPONSE TO FRONTEND (JSON)
```

---

## Layer 1: Controller (e.g., StudentController.java)

**Job:** Be the front door. Receive HTTP requests, call the Service, return a response.

The Controller does NOT contain business logic. It just:
1. Receives the request
2. Calls the right Service method
3. Wraps the result in an ApiResponse and returns it

```java
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService; // Spring injects this automatically

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        // 1. Call the service
        StudentResponse student = studentService.getById(id);
        // 2. Wrap in ApiResponse and return
        return ResponseEntity.ok(ApiResponse.success("Student retrieved", student));
    }
}
```

`ResponseEntity` is a Spring class that lets you control the HTTP status code
(200 OK, 201 Created, 404 Not Found, etc.) along with the response body.

---

## Layer 2: Service (e.g., StudentService.java)

**Job:** The kitchen. Contains all the business logic and decisions.

The Service is the most important layer. It:
1. Validates that referenced records exist (e.g., does the Guardian with this ID actually exist?)
2. Maps the Request DTO to an Entity
3. Calls the Repository to save/read from the database
4. Maps the Entity back to a Response DTO
5. Throws `ResourceNotFoundException` if something is not found

```java
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final GuardianRepository guardianRepository;

    public StudentResponse create(StudentRequest request) {
        Student student = new Student();               // Create empty entity
        student.setFirstName(request.getFirstName());  // Fill it from the request
        student.setStatus("ACTIVE");                   // Business rule: new students are ACTIVE

        // Look up the guardian by ID
        Guardian guardian = guardianRepository.findById(request.getGuardianId())
            .orElseThrow(() -> new ResourceNotFoundException("Guardian not found"));
        student.setGuardian(guardian);

        Student saved = studentRepository.save(student); // Save to database
        return StudentResponse.from(saved);              // Convert to response DTO
    }
}
```

---

## Layer 3: Repository (e.g., StudentRepository.java)

**Job:** The waiter. Fetches things from the database.

This is an interface — you do NOT write any implementation code.
Spring Data JPA reads the method names and generates the SQL automatically.

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Spring reads this method name and generates:
    // SELECT * FROM student WHERE current_class_id = ?
    List<Student> findByCurrentClassId(Long classId);

    // SELECT * FROM student WHERE status = ?
    List<Student> findByStatus(String status);

    // SELECT * FROM student WHERE email = ? LIMIT 1
    Optional<Student> findByEmail(String email);
}
```

`JpaRepository<Student, Long>` already gives you these methods for free:
- `findAll()` — get all students
- `findById(id)` — get one student by ID
- `save(student)` — insert or update a student
- `deleteById(id)` — delete a student
- `existsById(id)` — check if a student exists
- `count()` — count all students

---

## Layer 4: Entity (e.g., Student.java)

**Job:** The blueprint of a database table.

Each field in the class becomes a column in the database table.
Each instance of the class represents one row in that table.

```java
@Entity        // → Creates the "student" table in PostgreSQL
@Data          // → Generates getters/setters
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;               // → column: id (auto-increment)

    private String firstName;      // → column: first_name
    private String status;         // → column: status

    @ManyToOne
    private Guardian guardian;     // → column: guardian_id (foreign key)
}
```

Note: Hibernate converts camelCase to snake_case automatically.
`firstName` becomes the column `first_name` in PostgreSQL.

---

## Layer 5: DTOs — Data Transfer Objects

**Job:** Control what data goes IN and what data comes OUT. Keep the API clean.

There are two types of DTOs per feature:

### Request DTO (e.g., StudentRequest.java)
What the frontend sends to the API. Contains validation rules.
```java
public class StudentRequest {
    @NotBlank(message = "First name is required")
    private String firstName;      // Required

    private Long guardianId;       // Frontend sends the ID, not the whole object
    private Long currentClassId;   // Same here — just the ID
}
```

### Response DTO (e.g., StudentResponse.java)
What the API sends back to the frontend. Contains the full nested objects.
```java
public class StudentResponse {
    private Long id;
    private String firstName;
    private GuardianResponse guardian;    // Full guardian object (not just the ID)
    private ClassRoomResponse currentClass; // Full classroom object
}
```

Why use DTOs instead of returning the Entity directly?

1. SAFETY — Your Entity might have sensitive fields. DTOs let you choose what to expose.
2. CONTROL — The Entity has JPA annotations that can cause errors if serialized directly.
3. NESTING — The Entity stores `guardian_id`. The Response DTO shows the full guardian object.

---

---

# SECTION 4 — The Entities (Database Tables)

## Guardian
Parent or caregiver of a student.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated. Never send this in a request. |
| name | String | Yes | Full name of the guardian |
| phone | String | Yes | Contact phone number |
| email | String | No | Must be valid email format if provided |
| address | String | No | Physical address |
| relationship | String | Yes | "Father", "Mother", "Uncle", "Aunt", etc. |
| occupation | String | No | What the guardian does for work |

---

## Teacher
A teacher at the school.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| firstName | String | Yes | |
| lastName | String | Yes | |
| email | String | Yes | Must be valid email format |
| phone | String | No | |
| qualification | String | No | e.g., "BSc Mathematics", "MA Education" |
| specialization | String | No | e.g., "Mathematics", "English Literature" |

---

## Semester
An academic term / period of time.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| termName | String | Yes | e.g., "First Term", "Second Term" |
| academicYear | String | Yes | e.g., "2024/2025" |
| startDate | LocalDate | Yes | Format: "YYYY-MM-DD" e.g., "2024-09-01" |
| endDate | LocalDate | Yes | Format: "YYYY-MM-DD" e.g., "2024-12-15" |

---

## ClassRoom
A class group at the school.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| name | String | Yes | e.g., "Grade 10A", "Form 3B" |
| room | String | No | Physical room label, e.g., "Block B Room 3" |
| academicYear | String | Yes | e.g., "2024/2025" |
| capacity | Integer | No | Maximum number of students allowed |
| classTeacherId | Long | No | ID of the Teacher responsible for this class |

In the response, `classTeacherId` becomes a full `classTeacher` object.

---

## Subject
A course/subject taught at the school.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| name | String | Yes | e.g., "Mathematics", "English", "Science" |
| code | String | Yes | Short code, e.g., "MATH101", "ENG201" |
| description | String | No | Brief description of the subject |
| teacherId | Long | No | ID of the Teacher who teaches this subject |

---

## Student
A student enrolled at the school.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| firstName | String | Yes | |
| lastName | String | Yes | |
| email | String | No | Must be valid email format |
| phone | String | No | Contact number |
| gender | String | Yes | "MALE" or "FEMALE" |
| dateOfBirth | LocalDate | Yes | Format: "YYYY-MM-DD" e.g., "2008-03-15" |
| enrollmentDate | LocalDate | No | Defaults to today's date if not provided |
| status | String | No | Defaults to "ACTIVE". Options: ACTIVE, INACTIVE, GRADUATED, SUSPENDED |
| guardianId | Long | No | ID of the student's Guardian |
| currentClassId | Long | No | ID of the ClassRoom the student is in |

---

## Grade
A student's scores for one subject in one semester.

| Field | Type | Required | Notes |
|---|---|---|---|
| id | Long | Auto | Auto-generated |
| studentId | Long | Yes | Which student this grade belongs to |
| subjectId | Long | Yes | Which subject this grade is for |
| semesterId | Long | Yes | Which semester/term this grade is from |
| test1 | Double | Yes | Score out of 20 (must be between 0 and 20) |
| test2 | Double | Yes | Score out of 20 (must be between 0 and 20) |
| exams | Double | Yes | Score out of 60 (must be between 0 and 60) |
| total | Double | Auto | Calculated automatically. Never send this. |
| gradeLetter | String | Auto | Assigned automatically. Never send this. |
| remarks | String | Auto | Assigned automatically. Never send this. |

---

---

# SECTION 5 — How a Request Travels Through the App

Let's trace exactly what happens when the frontend sends:
`POST http://localhost:8080/api/students`

With this JSON body:
```json
{
  "firstName": "Lamin",
  "lastName": "Jallow",
  "gender": "MALE",
  "dateOfBirth": "2008-03-15",
  "guardianId": 1,
  "currentClassId": 2
}
```

### Step 1 — Spring receives the request
Spring sees a POST request to `/api/students`.
It finds that `StudentController.create()` is mapped to `POST /api/students`.
It converts the JSON body into a `StudentRequest` Java object.

### Step 2 — Validation runs (@Valid)
Because the method has `@Valid`, Spring runs all the validation checks:
- Is `firstName` blank? No → pass
- Is `lastName` blank? No → pass
- Is `gender` blank? No → pass
- Is `dateOfBirth` null? No → pass
If any check fails, Spring immediately returns a 400 error. The rest never runs.

### Step 3 — Controller calls the Service
```java
StudentResponse result = studentService.create(request);
```
The controller passes the validated request to the Service.

### Step 4 — Service does the work
The Service:
1. Creates a new empty `Student` entity
2. Copies all fields from the `StudentRequest` into the `Student`
3. Sets `enrollmentDate` to today (business rule: default to today)
4. Sets `status` to "ACTIVE" (business rule: new students start as active)
5. Looks up the Guardian with ID=1 from the database
   - If not found → throws `ResourceNotFoundException` → returns 404
6. Looks up the ClassRoom with ID=2 from the database
   - If not found → throws `ResourceNotFoundException` → returns 404
7. Links the guardian and classroom to the student object

### Step 5 — Repository saves to the database
```java
Student saved = studentRepository.save(student);
```
Spring Data JPA generates this SQL and runs it:
```sql
INSERT INTO student (first_name, last_name, gender, date_of_birth, enrollment_date, status, guardian_id, current_class_id)
VALUES ('Lamin', 'Jallow', 'MALE', '2008-03-15', '2026-07-20', 'ACTIVE', 1, 2);
```
PostgreSQL assigns `id = 1` and returns the saved object.

### Step 6 — Service converts to Response DTO
```java
return StudentResponse.from(saved);
```
The `StudentResponse.from()` method builds a clean response object.
It includes the full Guardian and ClassRoom objects (not just their IDs).

### Step 7 — Controller returns the response
```java
return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Student created", studentResponse));
```
Spring converts the `ApiResponse` object to JSON and sends it back.

### Final Response (HTTP 201 Created):
```json
{
  "success": true,
  "message": "Student created",
  "data": {
    "id": 1,
    "firstName": "Lamin",
    "lastName": "Jallow",
    "gender": "MALE",
    "dateOfBirth": "2008-03-15",
    "enrollmentDate": "2026-07-20",
    "status": "ACTIVE",
    "guardian": {
      "id": 1,
      "name": "Ousman Jallow",
      "phone": "7771234",
      "relationship": "Father"
    },
    "currentClass": {
      "id": 2,
      "name": "Grade 10A",
      "academicYear": "2024/2025"
    }
  }
}
```

---

---

# SECTION 6 — The Standard API Response Format

Every single endpoint in this API returns the same JSON structure.
This makes it easy for the frontend to always know what to expect.

## Success Response
```json
{
  "success": true,
  "message": "Students retrieved",
  "data": [ ... ]
}
```

## Single Item Response
```json
{
  "success": true,
  "message": "Student retrieved",
  "data": { ... }
}
```

## Created Response (HTTP 201)
```json
{
  "success": true,
  "message": "Student created",
  "data": { ... }
}
```

## Not Found Error (HTTP 404)
Happens when you request an ID that does not exist in the database.
```json
{
  "success": false,
  "message": "Student not found: 99",
  "data": null
}
```

## Validation Error (HTTP 400)
Happens when the request body is missing required fields or has invalid values.
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "firstName": "First name is required",
    "email": "Invalid email",
    "gender": "Gender is required"
  }
}
```

## Server Error (HTTP 500)
Happens when something unexpected breaks inside the code.
```json
{
  "success": false,
  "message": "Unexpected error: ...",
  "data": null
}
```

---

---

# SECTION 7 — All API Endpoints With Examples

Base URL: `http://localhost:8080`

---

## Guardians — `/api/guardians`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/guardians | Returns all guardians | 200 |
| GET | /api/guardians/1 | Returns guardian with ID 1 | 200 |
| POST | /api/guardians | Creates a new guardian | 201 |
| PUT | /api/guardians/1 | Updates guardian with ID 1 | 200 |
| DELETE | /api/guardians/1 | Deletes guardian with ID 1 | 200 |

**Create Guardian — POST /api/guardians**
```json
{
  "name": "Ousman Jallow",
  "phone": "7771234",
  "email": "ousman@gmail.com",
  "address": "Banjul, The Gambia",
  "relationship": "Father",
  "occupation": "Merchant"
}
```
Required fields: `name`, `phone`, `relationship`

---

## Teachers — `/api/teachers`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/teachers | Returns all teachers | 200 |
| GET | /api/teachers/1 | Returns teacher with ID 1 | 200 |
| POST | /api/teachers | Creates a new teacher | 201 |
| PUT | /api/teachers/1 | Updates teacher with ID 1 | 200 |
| DELETE | /api/teachers/1 | Deletes teacher with ID 1 | 200 |

**Create Teacher — POST /api/teachers**
```json
{
  "firstName": "Fatou",
  "lastName": "Ceesay",
  "email": "fatou@school.gm",
  "phone": "7009876",
  "qualification": "BSc Mathematics",
  "specialization": "Mathematics"
}
```
Required fields: `firstName`, `lastName`, `email`

---

## Semesters — `/api/semesters`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/semesters | Returns all semesters | 200 |
| GET | /api/semesters/1 | Returns semester with ID 1 | 200 |
| POST | /api/semesters | Creates a new semester | 201 |
| PUT | /api/semesters/1 | Updates semester with ID 1 | 200 |
| DELETE | /api/semesters/1 | Deletes semester with ID 1 | 200 |

**Create Semester — POST /api/semesters**
```json
{
  "termName": "First Term",
  "academicYear": "2024/2025",
  "startDate": "2024-09-01",
  "endDate": "2024-12-15"
}
```
Required fields: `termName`, `academicYear`, `startDate`, `endDate`

---

## ClassRooms — `/api/classrooms`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/classrooms | Returns all classrooms | 200 |
| GET | /api/classrooms/1 | Returns classroom with ID 1 | 200 |
| GET | /api/classrooms/1/students | Returns all students in classroom 1 | 200 |
| POST | /api/classrooms | Creates a new classroom | 201 |
| PUT | /api/classrooms/1 | Updates classroom with ID 1 | 200 |
| DELETE | /api/classrooms/1 | Deletes classroom with ID 1 | 200 |

**Create ClassRoom — POST /api/classrooms**
```json
{
  "name": "Grade 10A",
  "room": "Block B Room 3",
  "academicYear": "2024/2025",
  "capacity": 35,
  "classTeacherId": 1
}
```
Required fields: `name`, `academicYear`
Optional: `room`, `capacity`, `classTeacherId` (the ID of a Teacher that already exists)

---

## Subjects — `/api/subjects`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/subjects | Returns all subjects | 200 |
| GET | /api/subjects/1 | Returns subject with ID 1 | 200 |
| POST | /api/subjects | Creates a new subject | 201 |
| PUT | /api/subjects/1 | Updates subject with ID 1 | 200 |
| DELETE | /api/subjects/1 | Deletes subject with ID 1 | 200 |

**Create Subject — POST /api/subjects**
```json
{
  "name": "Mathematics",
  "code": "MATH101",
  "description": "Core mathematics for Grade 10",
  "teacherId": 1
}
```
Required fields: `name`, `code`
Optional: `description`, `teacherId`

---

## Students — `/api/students`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/students | Returns all students | 200 |
| GET | /api/students/1 | Returns student with ID 1 | 200 |
| GET | /api/students/1/grades | Returns all grades for student 1 | 200 |
| POST | /api/students | Creates a new student | 201 |
| PUT | /api/students/1 | Updates student with ID 1 | 200 |
| PUT | /api/students/1/promote?newClassId=3 | Moves student 1 to classroom 3 | 200 |
| DELETE | /api/students/1 | Deletes student with ID 1 | 200 |

**Create Student — POST /api/students**
```json
{
  "firstName": "Lamin",
  "lastName": "Jallow",
  "email": "lamin@student.gm",
  "phone": "7001234",
  "gender": "MALE",
  "dateOfBirth": "2008-03-15",
  "guardianId": 1,
  "currentClassId": 1
}
```
Required fields: `firstName`, `lastName`, `gender`, `dateOfBirth`
Optional: `email`, `phone`, `guardianId`, `currentClassId`
Auto-set: `enrollmentDate` = today, `status` = "ACTIVE"

**Promote a Student — PUT /api/students/1/promote?newClassId=3**

No request body needed. Just the URL with the query parameter.
This moves student with ID=1 into the classroom with ID=3.

---

## Grades — `/api/grades`

| Method | URL | What it does | HTTP Status |
|---|---|---|---|
| GET | /api/grades | Returns all grades | 200 |
| GET | /api/grades/1 | Returns grade with ID 1 | 200 |
| GET | /api/grades/student/1 | Returns all grades for student 1 | 200 |
| GET | /api/grades/student/1/semester/2 | Returns grades for student 1 in semester 2 | 200 |
| POST | /api/grades | Creates a new grade | 201 |
| PUT | /api/grades/1 | Updates grade with ID 1 | 200 |
| DELETE | /api/grades/1 | Deletes grade with ID 1 | 200 |

**Create Grade — POST /api/grades**
```json
{
  "studentId": 1,
  "subjectId": 1,
  "semesterId": 1,
  "test1": 18.0,
  "test2": 16.5,
  "exams": 52.0
}
```
Required fields: ALL fields are required
Auto-calculated: `total`, `gradeLetter`, `remarks`

The API calculates automatically:
- `total` = 18.0 + 16.5 + 52.0 = **86.5**
- `gradeLetter` = **"A"**
- `remarks` = **"DISTINCTION"**

Score limits enforced by validation:
- `test1`: must be 0–20
- `test2`: must be 0–20
- `exams`: must be 0–60

---

---

# SECTION 8 — Recommended Creation Order for Testing

The database has relationships between tables. You cannot create a Student
if the Guardian it references does not exist yet. Follow this order in Postman:

```
Step 1: POST /api/teachers        ← No dependencies. Create first.
        Save the ID from the response (e.g., id: 1)

Step 2: POST /api/guardians       ← No dependencies. Create first.
        Save the ID from the response (e.g., id: 1)

Step 3: POST /api/semesters       ← No dependencies. Create first.
        Save the ID from the response (e.g., id: 1)

Step 4: POST /api/classrooms      ← Needs: classTeacherId (from Step 1)
        Save the ID from the response (e.g., id: 1)

Step 5: POST /api/subjects        ← Needs: teacherId (from Step 1)
        Save the ID from the response (e.g., id: 1)

Step 6: POST /api/students        ← Needs: guardianId (Step 2), currentClassId (Step 4)
        Save the ID from the response (e.g., id: 1)

Step 7: POST /api/grades          ← Needs: studentId (Step 6), subjectId (Step 5), semesterId (Step 3)
```

---

---

# SECTION 9 — How Relationships Work in the Database

When you use `@ManyToOne` in Java, Hibernate creates a Foreign Key in the database.
Here is how the database tables look after you create some data:

**teacher table:**
```
| id | first_name | last_name | email           |
|----|------------|-----------|-----------------|
|  1 | Fatou      | Ceesay    | fatou@school.gm |
```

**class_room table:**
```
| id | name       | academic_year | class_teacher_id |
|----|------------|---------------|------------------|
|  1 | Grade 10A  | 2024/2025     |                1 |  ← Points to teacher id=1
|  2 | Grade 11B  | 2024/2025     |                1 |  ← Same teacher
```

**guardian table:**
```
| id | name         | relationship |
|----|--------------|--------------|
|  1 | Ousman Jallow | Father      |
|  2 | Aminata Bah   | Mother      |
```

**student table:**
```
| id | first_name | status | guardian_id | current_class_id |
|----|------------|--------|-------------|------------------|
|  1 | Lamin      | ACTIVE |           1 |                1 |  ← Guardian=Ousman, Class=10A
|  2 | Fatou      | ACTIVE |           2 |                1 |  ← Guardian=Aminata, Class=10A
```

**grade table:**
```
| id | test1 | test2 | exams | total | grade_letter | student_id | subject_id | semester_id |
|----|-------|-------|-------|-------|--------------|------------|------------|-------------|
|  1 |  18.0 |  16.5 |  52.0 |  86.5 | A            |          1 |          1 |           1 |
```

When you call `GET /api/students/1`, Spring automatically JOINs all these tables
and returns the full nested response with the guardian and classroom objects included.

---

## Relationships Summary Diagram

```
Teacher ────────────────────────────────────────────┐
   │                                                 │
   │ (classTeacher)                  (teacher)       │
   ▼                                                 ▼
ClassRoom                                        Subject
   │                                                 │
   │ (currentClass)                  (subject)       │
   ▼                                                 ▼
Student ──────────────────────────────────────── Grade
   │                                (student)        │
   │ (guardian)                                      │ (semester)
   ▼                                                 ▼
Guardian                                        Semester
```

Reading it: "A ClassRoom has one Teacher (classTeacher). A Student is in one ClassRoom
(currentClass). A Grade links a Student + Subject + Semester together."

---

---

# SECTION 10 — Grade Calculation Logic

The grading is out of 100 total:
- Test 1: max 20 marks
- Test 2: max 20 marks
- Final Exam: max 60 marks

Total = test1 + test2 + exams

| Total Score | Grade Letter | Remarks |
|---|---|---|
| 80 – 100 | A | DISTINCTION |
| 70 – 79 | B | PASS |
| 60 – 69 | C | PASS |
| 50 – 59 | D | PASS |
| 0 – 49 | F | FAIL |

You NEVER send `total`, `gradeLetter`, or `remarks` from the frontend.
The GradeService calculates all three automatically every time a grade is saved or updated.

---

---

# SECTION 11 — Common Errors and What They Mean

## "Connection refused" when starting the app
Your Docker container is not running.
Fix: Open a terminal and run: `docker-compose up -d`
Then wait 5 seconds and start the app again.

## "Password authentication failed for user postgres"
Your `application.properties` username/password does not match your Docker container.
Check that these match exactly:
```
# application.properties
spring.datasource.username=sms
spring.datasource.password=password

# docker-compose.yml
POSTGRES_USER: sms
POSTGRES_PASSWORD: password
```

## HTTP 404 — "Guardian not found: 5"
You are trying to create a Student with `guardianId: 5` but no Guardian with ID=5
exists in the database. Create the Guardian first, then use its ID.

## HTTP 400 — "Validation failed"
One or more required fields are missing or have invalid values.
Read the `data` field in the response — it tells you exactly which field failed and why.

## HTTP 500 — Unexpected error
Something broke in the code. Check the IntelliJ console/logs for the full error message.
The most common cause is a null value where the code expected a real object.

## Column does not exist error in logs
This happens when you added a new field to an Entity but the database table was already created
without that column. Fix: stop the app, drop the table (or the whole database), and restart.
With `ddl-auto=update`, Hibernate will recreate the table with the new column.

---

## Tech Stack Reference

| Technology | What It Does |
|---|---|
| Spring Boot | Framework that wires everything together and starts the app |
| Spring Web MVC | Handles HTTP requests and routing (@RestController, @GetMapping, etc.) |
| Spring Data JPA | Generates SQL from method names and manages database connections |
| Hibernate | The actual ORM engine that converts Java objects to database rows |
| PostgreSQL | The relational database where all data is stored |
| Docker | Runs PostgreSQL in an isolated container so you do not need to install it on Windows |
| Lombok | Generates boilerplate Java code (@Data, @RequiredArgsConstructor, etc.) |
| spring-boot-starter-validation | Enables @Valid, @NotBlank, @Email, etc. to actually work |
| Maven | Build tool that downloads dependencies and compiles the project |

---

---

# SECTION 12 — What Was Built and Changed

This section documents every file that was created or modified when the service,
controller, and DTO layers were added to this project.

---

## New Files Created

### Infrastructure — 2 new packages

| File | Package | Purpose |
|---|---|---|
| `ApiResponse.java` | `common` | Standard wrapper returned by every endpoint: `{ success, message, data }` |
| `ResourceNotFoundException.java` | `exception` | Thrown when a record is not found. Causes a 404 response. |
| `GlobalExceptionHandler.java` | `exception` | Catches errors from ALL controllers in one place and returns clean JSON |

---

### Request DTOs — 7 new files

These are the objects the frontend sends in the request body.
They carry the validation rules (`@NotBlank`, `@Email`, `@Min`, `@Max`).

| File | Required Fields |
|---|---|
| `GuardianRequest.java` | name, phone, relationship |
| `TeacherRequest.java` | firstName, lastName, email |
| `ClassRoomRequest.java` | name, academicYear |
| `SemesterRequest.java` | termName, academicYear, startDate, endDate |
| `SubjectRequest.java` | name, code |
| `StudentRequest.java` | firstName, lastName, gender, dateOfBirth |
| `GradeRequest.java` | studentId, subjectId, semesterId, test1, test2, exams |

---

### Response DTOs — 7 new files

These are the objects the API sends back to the frontend.
They include full nested objects instead of just IDs.

| File | What it nests |
|---|---|
| `GuardianResponse.java` | No nesting (standalone) |
| `TeacherResponse.java` | No nesting (standalone) |
| `SemesterResponse.java` | No nesting (standalone) |
| `ClassRoomResponse.java` | Full TeacherResponse (classTeacher) |
| `SubjectResponse.java` | Full TeacherResponse (teacher) |
| `StudentResponse.java` | Full GuardianResponse + Full ClassRoomResponse |
| `GradeResponse.java` | Full StudentResponse + Full SubjectResponse + Full SemesterResponse |

Example: when you call `GET /api/grades/1`, the response includes the full student,
the full subject (with teacher), and the full semester — not just their IDs.

---

### Services — 7 new files

All business logic lives in these files.

| File | Special Logic |
|---|---|
| `GuardianService.java` | Standard CRUD |
| `TeacherService.java` | Standard CRUD |
| `SemesterService.java` | Standard CRUD |
| `SubjectService.java` | Looks up Teacher by ID before saving |
| `ClassRoomService.java` | Looks up Teacher by ID before saving. Also has `getStudents(classId)` |
| `StudentService.java` | Looks up Guardian + ClassRoom before saving. Auto-sets `enrollmentDate` to today and `status` to ACTIVE. Has `promote(studentId, newClassId)` method |
| `GradeService.java` | Looks up Student + Subject + Semester before saving. Auto-calculates `total`, `gradeLetter`, and `remarks` |

---

### Controllers — 7 new files

These expose the REST endpoints.

| File | Base URL | Special Endpoints |
|---|---|---|
| `GuardianController.java` | /api/guardians | Standard CRUD only |
| `TeacherController.java` | /api/teachers | Standard CRUD only |
| `SemesterController.java` | /api/semesters | Standard CRUD only |
| `SubjectController.java` | /api/subjects | Standard CRUD only |
| `ClassRoomController.java` | /api/classrooms | + GET /{id}/students |
| `StudentController.java` | /api/students | + GET /{id}/grades, PUT /{id}/promote?newClassId=X |
| `GradeController.java` | /api/grades | + GET /student/{id}, GET /student/{id}/semester/{id} |

---

## Existing Files Updated

### 7 Entities — fields added

| Entity | Fields Removed | Fields Added |
|---|---|---|
| `Student.java` | `age` | `email`, `phone`, `gender`, `dateOfBirth`, `enrollmentDate`, `status` |
| `Teacher.java` | nothing | `phone`, `qualification`, `specialization` |
| `Guardian.java` | nothing | `relationship`, `occupation` |
| `ClassRoom.java` | nothing | `academicYear`, `capacity`, `classTeacher` (→ Teacher) |
| `Subject.java` | nothing | `description` |
| `Semester.java` | nothing | `academicYear`, `startDate`, `endDate` |
| `Grade.java` | nothing | `gradeLetter` (auto), `remarks` (auto) |

Why `age` was replaced with `dateOfBirth`:
Storing age as a number means it goes out of date every year.
Storing the date of birth means you can always calculate the correct age from it.

---

### 4 Repositories — custom query methods added

Spring Data JPA generates the SQL from the method name automatically.
No SQL is written manually.

**StudentRepository.java**
```java
List<Student> findByCurrentClassId(Long classId);   // used by GET /api/classrooms/{id}/students
List<Student> findByGuardianId(Long guardianId);
List<Student> findByStatus(String status);
Optional<Student> findByEmail(String email);
```

**GradeRepository.java**
```java
List<Grade> findByStudentId(Long studentId);                              // used by GET /api/grades/student/{id}
List<Grade> findByStudentIdAndSemesterId(Long studentId, Long semesterId); // used by GET /api/grades/student/{id}/semester/{id}
List<Grade> findBySemesterId(Long semesterId);
```

**SubjectRepository.java**
```java
List<Subject> findByTeacherId(Long teacherId);
Optional<Subject> findByCode(String code);
```

**ClassRoomRepository.java**
```java
List<ClassRoom> findByAcademicYear(String academicYear);
```

---

### pom.xml — one dependency added

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Why this was needed:
The `@Valid` annotation in controllers and the `@NotBlank`, `@Email`, `@Min`, `@Max`
annotations in Request DTOs are all part of the Jakarta Validation standard.
However, they are just labels — they do nothing on their own.
This dependency provides the actual **Hibernate Validator engine** that reads those
labels and runs the checks at runtime. Without it, all validation is silently skipped
and any input (even empty or invalid) is accepted by the API.
