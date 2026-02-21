-- ==============================================
-- 강의실 (Classroom)
-- requirements.md: 603, 604, 605, 606호
-- ==============================================
INSERT INTO classroom (room_number) VALUES (603);
INSERT INTO classroom (room_number) VALUES (604);
INSERT INTO classroom (room_number) VALUES (605);
INSERT INTO classroom (room_number) VALUES (606);

-- ==============================================
-- 교시 (Period)
-- 0교시: 세팅 전용 (수업 전)
-- 1교시: 8:30 ~ 10:10
-- 2교시: 10:30 ~ 12:10
-- 3교시: 13:20 ~ 15:00
-- 4교시: 15:20 ~ 17:00
-- ==============================================
INSERT INTO period (period_number, start_time, end_time) VALUES (0, '07:00:00', '08:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (1, '08:30:00', '10:10:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (2, '10:30:00', '12:10:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (3, '13:20:00', '15:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (4, '15:20:00', '17:00:00');

-- ==============================================
-- 크루 (Crew)
-- 오전(MORNING) 3명: 오전 수업 전부터 근무
-- 미들(MIDDLE)  2명: 오전 9시 ~ 오후 5시
-- 오후(AFTERNOON) 2명: 오후 2시 20분 ~ 오후 10시
-- ==============================================
INSERT INTO crew (name, crew_type) VALUES ('장강민', 'MORNING');
INSERT INTO crew (name, crew_type) VALUES ('김수민', 'MORNING');
INSERT INTO crew (name, crew_type) VALUES ('조하은', 'MORNING');
INSERT INTO crew (name, crew_type) VALUES ('문승찬', 'MIDDLE');
INSERT INTO crew (name, crew_type) VALUES ('임혜령', 'MIDDLE');
INSERT INTO crew (name, crew_type) VALUES ('박지호', 'AFTERNOON');
INSERT INTO crew (name, crew_type) VALUES ('이서준', 'AFTERNOON');

-- ==============================================
-- 카운트 (Count) - 크루별 작업유형별 초기값 0
-- crew_id 1~3: 오전, 4~5: 미들, 6~7: 오후
-- task_type: SETTING, ENTRY, JOG, EXIT, EXAM_SUPERVISION
-- ==============================================

-- 장강민 (crew_id = 1, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'EXAM_SUPERVISION', 0);

-- 김수민 (crew_id = 2, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'EXAM_SUPERVISION', 0);

-- 조하은 (crew_id = 3, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'EXAM_SUPERVISION', 0);

-- 문승찬 (crew_id = 4, 미들)
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'EXAM_SUPERVISION', 0);

-- 임혜령 (crew_id = 5, 미들)
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'EXAM_SUPERVISION', 0);

-- 박지호 (crew_id = 6, 오후)
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'EXAM_SUPERVISION', 0);

-- 이서준 (crew_id = 7, 오후)
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'EXAM_SUPERVISION', 0);
