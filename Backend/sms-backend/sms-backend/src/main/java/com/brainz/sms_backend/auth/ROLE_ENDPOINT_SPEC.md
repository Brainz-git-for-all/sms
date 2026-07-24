# Role-Based Endpoint Access — Spec

## Roles
| Role     | Who is this?                        |
|----------|-------------------------------------|
| ADMIN    | School principal / admin staff      |
| TEACHER  | A teacher at the school             |
| STUDENT  | A student — sees only their grades  |
| GUARDIAN | Parent/guardian — sees children's grades |

---

## Existing endpoints — who can access them (I add @PreAuthorize)

| Endpoint                          | ADMIN | TEACHER | STUDENT | GUARDIAN |
|-----------------------------------|-------|---------|---------|----------|
| GET /api/students                 | ✓     | ✓       | ✗       | ✗        |
| GET /api/students/{id}            | ✓     | ✓       | own only| ✗        |
| GET /api/students/{id}/grades     | ✓     | ✓       | own only| ✗        |
| POST /api/students                | ✓     | ✗       | ✗       | ✗        |
| PUT /api/students/{id}            | ✓     | ✓       | ✗       | ✗        |
| DELETE /api/students/{id}         | ✓     | ✗       | ✗       | ✗        |
| GET /api/teachers                 | ✓     | ✓       | ✗       | ✗        |
| GET /api/grades                   | ✓     | ✓       | ✗       | ✗        |
| POST/PUT/DELETE /api/grades       | ✓     | ✓       | ✗       | ✗        |
| GET /api/guardians                | ✓     | ✗       | ✗       | ✗        |

---

## NEW endpoints to add

### 1. Student views their own grades
File: StudentController.java — add this method

```
@GetMapping("/my-grades")
@PreAuthorize("hasRole('STUDENT')")
public ResponseEntity<ApiResponse<List<GradeResponse>>> getMyGrades(Authentication authentication)
```

What the service does:
1. Get the logged-in username from authentication.getName()
2. Load AppUser from AppUserRepository by username
3. Use appUser.studentId to call gradeRepository.findByStudentId(studentId)
4. Return the grades

**WHO BUILDS THIS: YOU (YOUR TASK)**
Service method name: getMyGrades(String username)
Controller calls: studentService.getMyGrades(authentication.getName())

---

### 2. Guardian views their children's grades
File: GuardianController.java — add this method

```
@GetMapping("/my-children-grades")
@PreAuthorize("hasRole('GUARDIAN')")
public ResponseEntity<ApiResponse<List<GradeResponse>>> getChildrenGrades(Authentication authentication)
```

What the service does:
1. Get logged-in username from authentication.getName()
2. Load AppUser → use appUser.guardianId
3. Find all students where student.guardian.id == guardianId
4. For each student, get their grades
5. Return combined list

**WHO BUILDS THIS: I BUILD**
This is more complex — multiple lookups across entities.
