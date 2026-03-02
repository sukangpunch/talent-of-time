-- ============================================================
-- V1: Initial Schema
-- 기존 DB에는 baseline-on-migrate=true, baseline-version=1 로
-- 이 스크립트가 스킵됩니다. 신규 DB 설치 시에만 실행됩니다.
-- ============================================================

CREATE TABLE IF NOT EXISTS classroom
(
    classroom_id BIGINT NOT NULL AUTO_INCREMENT,
    room_number  INT    NOT NULL,
    PRIMARY KEY (classroom_id)
);

CREATE TABLE IF NOT EXISTS period
(
    period_id     BIGINT NOT NULL AUTO_INCREMENT,
    period_number INT    NOT NULL,
    start_time    TIME   NOT NULL,
    end_time      TIME   NOT NULL,
    PRIMARY KEY (period_id)
);

CREATE TABLE IF NOT EXISTS teacher
(
    teacher_id    BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    chalk_type    VARCHAR(255),
    chalk_detail  TEXT,
    eraser_detail TEXT,
    mic_type      VARCHAR(255),
    has_ppt       BOOLEAN,
    notes         TEXT,
    has_email     BOOLEAN,
    PRIMARY KEY (teacher_id)
);

CREATE TABLE IF NOT EXISTS crew
(
    crew_id      BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(255) NOT NULL,
    crew_type    VARCHAR(255),
    email        VARCHAR(255),
    provider_id  VARCHAR(255) UNIQUE,
    is_onboarded BOOLEAN      NOT NULL DEFAULT FALSE,
    role         VARCHAR(255),
    PRIMARY KEY (crew_id)
);

CREATE TABLE IF NOT EXISTS count
(
    count_id  BIGINT       NOT NULL AUTO_INCREMENT,
    crew_id   BIGINT       NOT NULL,
    task_type VARCHAR(255) NOT NULL,
    count     INT          NOT NULL,
    PRIMARY KEY (count_id),
    FOREIGN KEY (crew_id) REFERENCES crew (crew_id)
);

CREATE TABLE IF NOT EXISTS class_session
(
    class_session_id BIGINT       NOT NULL AUTO_INCREMENT,
    date             DATE         NOT NULL,
    period_id        BIGINT       NOT NULL,
    classroom_id     BIGINT       NOT NULL,
    teacher_id       BIGINT,
    subject          VARCHAR(255),
    group_name       VARCHAR(255),
    in_person_count  INT          NOT NULL,
    online_count     INT          NOT NULL,
    status           VARCHAR(255) NOT NULL,
    PRIMARY KEY (class_session_id),
    FOREIGN KEY (period_id) REFERENCES period (period_id),
    FOREIGN KEY (classroom_id) REFERENCES classroom (classroom_id),
    FOREIGN KEY (teacher_id) REFERENCES teacher (teacher_id)
);

CREATE TABLE IF NOT EXISTS schedule
(
    schedule_id      BIGINT       NOT NULL AUTO_INCREMENT,
    date             DATE         NOT NULL,
    task_type        VARCHAR(255) NOT NULL,
    crew_id          BIGINT       NOT NULL,
    class_session_id BIGINT,
    PRIMARY KEY (schedule_id),
    FOREIGN KEY (crew_id) REFERENCES crew (crew_id),
    FOREIGN KEY (class_session_id) REFERENCES class_session (class_session_id)
);
