-- ==============================================
-- 수업 일정 (ClassSession) - 2026-02-25 (수요일)
-- period_id: 1=0교시, 2=1교시, 3=2교시, 4=3교시, 5=4교시, 6=5교시, 7=6교시
-- classroom_id: 1=601, 2=602, 3=603, 4=604, 5=605, 6=606, 7=607, 8=608
-- teacher_id: 송지광, 손정민은 teacher 테이블에 없으므로 NULL 처리
-- ==============================================

-- 1교시 (08:30 ~ 10:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 2, 2, (SELECT teacher_id FROM teacher WHERE name = '송지광'), '사문', 'A(2)', 130, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 2, 3, (SELECT teacher_id FROM teacher WHERE name = '성치경'), '수학(수1)', 'N반', 83, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 2, 5, (SELECT teacher_id FROM teacher WHERE name = '고아름'), '세사', NULL, 17, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 2, 6, (SELECT teacher_id FROM teacher WHERE name = '손정민'), '경제', NULL, 8, 0, 'NORMAL');

-- 2교시 (10:30 ~ 12:10)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 3, 2, (SELECT teacher_id FROM teacher WHERE name = '송지광'), '사문', 'C(2)', 133, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 3, 5, (SELECT teacher_id FROM teacher WHERE name = '고아름'), '동사', NULL, 12, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 3, 6, (SELECT teacher_id FROM teacher WHERE name = '김강민'), '화1A', NULL, 55, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 3, 7, (SELECT teacher_id FROM teacher WHERE name = '이신혁'), '지1A', NULL, 123, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 3, 8, (SELECT teacher_id FROM teacher WHERE name = '황용일'), '국어(문학)', 'D', 124, 0, 'NORMAL');

-- 3교시 (13:20 ~ 15:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 2, (SELECT teacher_id FROM teacher WHERE name = '송지광'), '사문', 'B(2)', 40, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 3, (SELECT teacher_id FROM teacher WHERE name = '강기원'), '수학(미적)', 'O', 125, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 4, (SELECT teacher_id FROM teacher WHERE name = '권구승'), '수학(수1)', 'I', 126, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 5, (SELECT teacher_id FROM teacher WHERE name = '박지윤'), '생1B', NULL, 56, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 6, (SELECT teacher_id FROM teacher WHERE name = '강철중'), '수학(수1)', '기하S반', 15, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 4, 8, (SELECT teacher_id FROM teacher WHERE name = '황용일'), '국어(문학)', 'S', 71, 0, 'NORMAL');

-- 4교시 (15:20 ~ 17:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 5, 4, (SELECT teacher_id FROM teacher WHERE name = '권구승'), '수학(수1)', 'D', 124, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 5, 6, (SELECT teacher_id FROM teacher WHERE name = '강철중'), '수학(수1)', '확통S', 127, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 5, 7, (SELECT teacher_id FROM teacher WHERE name = '김기병'), '영어', 'O', 125, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 5, 8, (SELECT teacher_id FROM teacher WHERE name = '황용일'), '국어(문학)', 'N', 98, 0, 'NORMAL');

-- 5교시 (18:20 ~ 20:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 6, 2, (SELECT teacher_id FROM teacher WHERE name = '최지욱'), '수학(미적)', 'D', 124, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 6, 6, (SELECT teacher_id FROM teacher WHERE name = '오택민'), '영어', 'I', 126, 0, 'NORMAL');

-- 6교시 (20:20 ~ 22:00)
INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 7, 2, (SELECT teacher_id FROM teacher WHERE name = '최지욱'), '수학(미적)', 'S', 71, 0, 'NORMAL');

INSERT INTO class_session (date, period_id, classroom_id, teacher_id, subject, group_name, in_person_count, online_count, status)
VALUES ('2026-02-25', 7, 6, (SELECT teacher_id FROM teacher WHERE name = '오택민'), '영어', '확통S', 127, 0, 'NORMAL');
