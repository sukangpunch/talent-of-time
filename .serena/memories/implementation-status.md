# talent-of-time Implementation Status

## Plan: API Cleanup (2026-02-24)

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
- `ClassSession.java` — added nullable `teacher` FK (ManyToOne) + `cancelled` boolean field + `cancel()` method
- `ClassSessionResponse.java` — added `TeacherResponse teacher` + `cancelled` fields
- `ClassSessionCreateRequest.java` / `ClassSessionUpdateRequest.java` — added `teacherId` (nullable)
- `ClassSessionService.java` — wired teacherId into create/bulk/update flows; added `cancelClassSession()`

**Stage 2: Crew auth + Spring Security + JWT**
- `crew/domain/Role.java` - enum: ADMIN, USER (renamed from CrewRole)
- `Crew.java` — added username, password, email, role fields + `createWithAuth()` factory
- `CrewResponse.java` — added username, email, role
- `CrewRepository.java` — added existsByUsername, existsByEmail, findByUsername, findByName
- `CrewService.java` — injected PasswordEncoder, added username/email duplicate checks, added `findCrewByName()`
- `auth/util/JwtTokenProvider.java` — JJWT 0.12.x, generates/validates tokens
- `security/filter/JwtAuthenticationFilter.java` — OncePerRequestFilter, sets SecurityContext with crewId as principal
- `auth/dto/LoginRequest.java`, `LoginResponse.java`, `SignupRequest.java`, `SignupResponse.java`
- `auth/service/AuthService.java` — login with BCrypt, signup with count initialization
- `auth/controller/AuthController.java` — POST `/api/auth/signup`, `/api/auth/login`, `/api/auth/logout` — now implements `AuthControllerDocs`
- `auth/controller/AuthControllerDocs.java` — NEW Swagger docs interface
- `security/config/SecurityConfig.java` — stateless JWT, anyRequest().authenticated()

**Stage 3: Schedule self-registration (simplified)**
- `schedule/controller/ScheduleController.java` — POST `/api/schedules` (selfRegister), POST `/api/schedules/swap` (swap), DELETE `/api/schedules/{id}` (cancel)
- `schedule/controller/ScheduleControllerDocs.java` — 3 API docs only
- `ScheduleService.java` — `swapSchedules()`, `selfRegister()`, `cancelRegistration()` only

**Stage 4: ClassSession API cleanup**
- Controller: GET `/today`, GET `?date=`, POST `/bulk`, PUT `/{id}`, PATCH `/{id}/cancel`
- Docs updated to match

**Stage 5: Crew API cleanup**
- Controller: GET `/api/crews` (all/by-type), GET `/api/crews/name?name=`
- Docs updated to match

**Stage 6: Count API cleanup**
- Controller: GET `/api/counts`, GET `/api/counts/crew/{id}`, GET `/api/counts/task-type/{type}`
- Removed: reset endpoints
- TaskType changed: `EXAM_SUPERVISION` → `CLEANUP`
- data.sql updated accordingly

**Stage 7: My page**
- `count/dto/MyCountResponse.java`
- `crew/service/MyService.java` — getProfile, getMyCounts
- `crew/controller/MyController.java` — GET `/api/my/profile`, GET `/api/my/counts` — now implements `MyControllerDocs`
- `crew/controller/MyControllerDocs.java` — NEW Swagger docs interface

**Error codes added:**
- TEACHER_NOT_FOUND
- CREW_USERNAME_DUPLICATED, CREW_EMAIL_DUPLICATED
- AUTH_INVALID_CREDENTIALS, AUTH_UNAUTHORIZED, AUTH_FORBIDDEN
- SCHEDULE_NOT_OWNER, SCHEDULE_DUPLICATE_TASK
- CLASS_SESSION_ALREADY_CANCELLED

**TaskType enum values:**
- SETTING (셋팅), ENTRY (입실), EXIT (퇴실), JOG (조그), CLEANUP (미화)

**Config:**
- `build.gradle` — added spring-security, jjwt-api/impl/jackson, spring-mail
- `application.yml` — added jwt.secret and jwt.expiration-ms per profile

**Auth 시스템 대규모 개편 (2026-02-24):**
- Username/password 방식 제거 → Kakao OAuth 전용으로 전환
- `auth/service/AuthService.java` 삭제됨
- `auth/controller/AuthController.java` — logout만 남김
- `auth/controller/AuthControllerDocs.java` — logout만 남김
- `auth/controller/OAuthController.java` — GET `/api/v1/oauth/kakao?code=&dest=` 추가
- `auth/service/oauth/OAuthLoginProcessor.java` — Crew 기반으로 완전 재작성 (카운트 초기화 포함)
- `auth/service/AuthTokenProvider.java` — User→Crew, 예외 타입 수정
- `auth/token/JwtProvider.java` — User→Crew, validateToken boolean 반환, getClaims public
- `auth/token/config/TokenProperties.java` — @PostConstruct 및 static 메서드 제거
- `crew/domain/Crew.java` — providerId 필드 추가, crewType nullable, createWithOAuth() 추가
- `crew/repository/CrewRepository.java` — findByProviderId() 추가
- `common/exception/ErrorCode.java` — OAuth/토큰 에러코드 추가, DEST_NOT_VALID 중복코드 수정
- `security/config/SecurityConfig.java` — /api/v1/oauth/** permitAll 추가
- `application.yml` — prod 프로파일에 token.access.expire-time 추가

### Not Yet Implemented
- Stage 8: Email SMTP for ID/password recovery
