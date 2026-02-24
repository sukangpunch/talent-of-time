-- ==============================================
-- 수업 일정 (ClassSession) - 2026-02-27 (금요일)
-- period_id: 1=0교시, 2=1교시, 3=2교시, 4=3교시, 5=4교시, 6=5교시, 7=6교시
-- classroom_id: 1=601, 2=602, 3=603, 4=604, 5=605, 6=606, 7=607, 8=608
-- ==============================================

-- 1교시 (08:30 ~ 10:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 2, 4, (SELECT teacher_id FROM teacher WHERE name = '김재훈'), '국어(문학)', '확통S', 127, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 2, 6, (SELECT teacher_id FROM teacher WHERE name = '차주현'), '수학(수2)', 'N반', 83, 0, 'NORMAL');

-- 2교시 (10:30 ~ 12:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 3, 4, (SELECT teacher_id FROM teacher WHERE name = '김재훈'), '국어(문학)', 'O', 125, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 3, 5, (SELECT teacher_id FROM teacher WHERE name = '이승헌'), '한지', NULL, 38, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 3, 6, (SELECT teacher_id FROM teacher WHERE name = '차주현'), '수학(수2)', '기하S반', 15, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 3, 7, (SELECT teacher_id FROM teacher WHERE name = '남지현'), '수학(확통)', '확통S', 127, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 3, 8, (SELECT teacher_id FROM teacher WHERE name = '유신'), '국어(독서)', 'I', 126, 0, 'NORMAL');

-- 3교시 (13:20 ~ 15:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 4, 5, (SELECT teacher_id FROM teacher WHERE name = '이승헌'), '세지', NULL, 55, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 4, 6, (SELECT teacher_id FROM teacher WHERE name = '변춘수'), '생1A', NULL, 125, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 4, 7, (SELECT teacher_id FROM teacher WHERE name = '심규원'), '물1A', NULL, 107, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 4, 8, (SELECT teacher_id FROM teacher WHERE name = '유신'), '국어(독서)', 'S', 71, 0, 'NORMAL');

-- 4교시 (15:20 ~ 17:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 5, 4, (SELECT teacher_id FROM teacher WHERE name = '엄소연'), '수학(수2)', 'I', 126, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 5, 7, (SELECT teacher_id FROM teacher WHERE name = '김기원'), '수리논술', 'O반', 121, 0, 'NORMAL');

-- 5교시 (18:20 ~ 20:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 6, 5, (SELECT teacher_id FROM teacher WHERE name = '이태민'), '경제', NULL, 8, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 6, 6, (SELECT teacher_id FROM teacher WHERE name = '이서준'), '생2', NULL, 34, 0, 'NORMAL');

-- 6교시 (20:20 ~ 22:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-27', 7, 6, (SELECT teacher_id FROM teacher WHERE name = '이서준'), '생2', NULL, 34, 0, 'NORMAL');
