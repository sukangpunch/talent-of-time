-- ==============================================
-- 수업 일정 (ClassSession) - 2026-02-26 (목요일)
-- period_id: 1=0교시, 2=1교시, 3=2교시, 4=3교시, 5=4교시, 6=5교시, 7=6교시
-- classroom_id: 1=601, 2=602, 3=603, 4=604, 5=605, 6=606, 7=607, 8=608
-- ==============================================

-- 1교시 (08:30 ~ 10:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 2, 4, (SELECT teacher_id FROM teacher WHERE name = '한세빈'), '영어', 'D', 124, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 2, 5, (SELECT teacher_id FROM teacher WHERE name = '권경수'), '수학(기하)', '기하S반', 15, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 2, 6, (SELECT teacher_id FROM teacher WHERE name = '김범찬'), '수학(미적)', 'N반', 83, 0, 'NORMAL');

-- 2교시 (10:30 ~ 12:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 3, 3, (SELECT teacher_id FROM teacher WHERE name = '서지현'), '수리논술', 'S반', 62, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 3, 4, (SELECT teacher_id FROM teacher WHERE name = '한세빈'), '영어', 'N', 98, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 3, 6, (SELECT teacher_id FROM teacher WHERE name = '김범찬'), '수학(미적)', 'I', 126, 0, 'NORMAL');

-- 3교시 (13:20 ~ 15:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 4, 3, (SELECT teacher_id FROM teacher WHERE name = '서지현'), '수리논술', 'N반', 81, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 4, 4, (SELECT teacher_id FROM teacher WHERE name = '한세빈'), '영어', 'S', 71, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 4, 6, (SELECT teacher_id FROM teacher WHERE name = '유주오'), '수학(수2)', '확통S', 127, 0, 'NORMAL');

-- 4교시 (15:20 ~ 17:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 5, 3, (SELECT teacher_id FROM teacher WHERE name = '서지현'), '수리논술', '기하S반', 9, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 5, 6, (SELECT teacher_id FROM teacher WHERE name = '강준호'), '화1A', NULL, 55, 0, 'NORMAL');

-- 5교시 (18:20 ~ 20:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 6, 2, (SELECT teacher_id FROM teacher WHERE name = '김성묵'), '윤사', NULL, 22, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 6, 3, (SELECT teacher_id FROM teacher WHERE name = '윤준수'), '사문', 'C(1)', 77, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 6, 4, (SELECT teacher_id FROM teacher WHERE name = '한혜선'), '생1B', NULL, 56, 0, 'NORMAL');

-- 6교시 (20:20 ~ 22:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 7, 2, (SELECT teacher_id FROM teacher WHERE name = '김성묵'), '윤사', NULL, 22, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-26', 7, 4, (SELECT teacher_id FROM teacher WHERE name = '한혜선'), '생1A', NULL, 125, 0, 'NORMAL');
