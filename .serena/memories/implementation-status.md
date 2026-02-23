# talent-of-time Implementation Status

## Plan: requirement3.md (2026-02-23)

### Completed

**Stage 1: Teacher entity + ClassSession update**
- `teacher/domain/ChalkType.java` - enum: SCHOOL, PERSONAL, BOTH
- `teacher/domain/MicType.java` - enum: SCHOOL, PERSONAL
- `teacher/domain/Teacher.java` - JPA entity
- `teacher/repository/TeacherRepository.java`
- `teacher/dto/TeacherCreateRequest.java`, `TeacherUpdateRequest.java`, `TeacherResponse.java`
- `teacher/service/TeacherService.java`
- `teacher/controller/TeacherController.java` (GET/POST/PUT/DELETE `/api/teachers`)
- `teacher/controller/TeacherControllerDocs.java`
- `ClassSession.java` — added nullable `teacher` FK (ManyToOne)
- `ClassSessionResponse.java` — added `TeacherResponse teacher` field
- `ClassSessionCreateRequest.java` / `ClassSessionUpdateRequest.java` — added `teacherId` (nullable)
- `ClassSessionService.java` — wired teacherId into create/bulk/update flows

**Stage 2: Crew auth + Spring Security + JWT**
- `crew/domain/CrewRole.java` - enum: ADMIN, USER
- `Crew.java` — added username, password, email, role fields + `createWithAuth()` factory
- `CrewCreateRequest.java` — added optional auth fields
- `CrewResponse.java` — added username, email, role
- `CrewRepository.java` — added existsByUsername, existsByEmail, findByUsername
- `CrewService.java` — injected PasswordEncoder, added username/email duplicate checks
- `auth/util/JwtTokenProvider.java` — JJWT 0.12.x, generates/validates tokens
- `auth/filter/JwtAuthenticationFilter.java` — OncePerRequestFilter, sets SecurityContext with crewId as principal
- `auth/dto/LoginRequest.java`, `LoginResponse.java`
- `auth/service/AuthService.java` — login with BCrypt
- `auth/controller/AuthController.java` — POST `/api/auth/login`, POST `/api/auth/logout`
- `common/config/SecurityConfig.java` — stateless JWT, role-based authorization

**Stage 3: Schedule self-registration**
- `schedule/dto/ScheduleCreateRequest.java` — classSessionId + taskType
- `ScheduleRepository.java` — added existsByClassSessionAndTaskType
- `ScheduleService.java` — added selfRegister() and cancelRegistration()
- `ScheduleController.java` — POST `/api/schedules` (selfRegister), DELETE `/api/schedules/{id}` (cancel), GET `/api/schedules/today`

**Stage 5: My page**
- `count/dto/MyCountResponse.java`
- `crew/service/MyService.java` — getProfile, getMyCounts
- `crew/controller/MyController.java` — GET `/api/my/profile`, GET `/api/my/counts`
- `CountRepository.java` — added findByCrew

**Error codes added:**
- TEACHER_NOT_FOUND
- CREW_USERNAME_DUPLICATED, CREW_EMAIL_DUPLICATED
- AUTH_INVALID_CREDENTIALS, AUTH_UNAUTHORIZED, AUTH_FORBIDDEN
- SCHEDULE_NOT_OWNER, SCHEDULE_DUPLICATE_TASK

**Config:**
- `build.gradle` — added spring-security, jjwt-api/impl/jackson, spring-mail
- `application.yml` — added jwt.secret and jwt.expiration-ms per profile

### Not Yet Implemented
- Stage 6: Email SMTP for ID/password recovery
- Admin-only enforcement on existing CRUD APIs (currently handled by `anyRequest().hasRole("ADMIN")` in SecurityConfig)
