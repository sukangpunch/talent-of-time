-- ==============================================
-- 크루 (Crew)
-- 오전(MORNING) 3명: 오전 수업 전부터 근무 (~17:00)
-- 미들(MIDDLE)  2명: 09:00 ~ 17:00
-- 오후(AFTERNOON) 3명: 14:20 ~ 22:00
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
-- crew_id 1~3: 오전, 4~5: 미들, 6~8: 오후
-- task_type: SETTING, ENTRY, JOG, EXIT, CLEANUP
-- ==============================================

-- 장강민 (crew_id = 1, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (1, 'CLEANUP', 0);

-- 김수민 (crew_id = 2, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (2, 'CLEANUP', 0);

-- 조하은 (crew_id = 3, 오전)
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (3, 'CLEANUP', 0);

-- 문승찬 (crew_id = 4, 미들)
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (4, 'CLEANUP', 0);

-- 임혜령 (crew_id = 5, 미들)
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (5, 'CLEANUP', 0);

-- 박지호 (crew_id = 6, 오후)
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (6, 'CLEANUP', 0);

-- 이서준 (crew_id = 7, 오후)
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'SETTING', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'ENTRY', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'JOG', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'EXIT', 0);
INSERT INTO count (crew_id, task_type, count) VALUES (7, 'CLEANUP', 0);
