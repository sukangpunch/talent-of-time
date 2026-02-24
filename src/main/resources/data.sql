-- ==============================================
-- 강의실 (Classroom)
-- 실제 운영 강의실: 601, 602, 603, 604, 605, 606, 607, 608호
-- ==============================================
INSERT INTO classroom (room_number) VALUES (601);
INSERT INTO classroom (room_number) VALUES (602);
INSERT INTO classroom (room_number) VALUES (603);
INSERT INTO classroom (room_number) VALUES (604);
INSERT INTO classroom (room_number) VALUES (605);
INSERT INTO classroom (room_number) VALUES (606);
INSERT INTO classroom (room_number) VALUES (607);
INSERT INTO classroom (room_number) VALUES (608);

-- ==============================================
-- 교시 (Period)
-- 0교시: 세팅 전용 (수업 시작 전)
-- 1교시: 08:30 ~ 10:10
-- 2교시: 10:30 ~ 12:10
-- 3교시: 13:20 ~ 15:00
-- 4교시: 15:20 ~ 17:00
-- 5교시: 18:20 ~ 20:00
-- 6교시: 20:20 ~ 22:00
-- ==============================================
INSERT INTO period (period_number, start_time, end_time) VALUES (0, '07:00:00', '08:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (1, '08:30:00', '10:10:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (2, '10:30:00', '12:10:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (3, '13:20:00', '15:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (4, '15:20:00', '17:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (5, '18:20:00', '20:00:00');
INSERT INTO period (period_number, start_time, end_time) VALUES (6, '20:20:00', '22:00:00');

-- ==============================================
-- 강사 (Teacher) 초기 데이터
-- TeacherData.ts 기반으로 생성
-- chalk_type: ACADEMY(학원분필) / PERSONAL(개인분필) / MIXED(혼합)
-- mic_type  : ACADEMY(학원마이크) / PERSONAL(개인마이크)
-- eraser_detail: NULL = 기본 세팅
-- ==============================================

INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강기원', 'ACADEMY', '학원분필 사용 (흰4, 노4, 빨2, 파2 세팅)', '5개, 쉬는시간 3분간 사용하던 지우개로만 정리(물x)', 'PERSONAL', false, '좌측 topic/개념설명 제외, 우측 문제들만 지우기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강수영', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강승연', 'PERSONAL', NULL, NULL, 'ACADEMY', true, '칠판 조명 끄기 / 강의 시작 전 파우치 받아서 분필 세팅 및 종료 후 개인물품 정리하여 6층 강사대기실 강승연T에게 전달/ 강의 종료 후 영상 메일 전송 필수', true);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강준호', 'MIXED', '분필 1세트 : 학원흰색2+개인컬러분필(빨노파)(6층교재실)', '[분필 - 지우개 - 분필 - 지우개 - 분필 - 분필 - 지우개] 세팅 + 젖은 물 지우개 6개 교탁 옆에 세팅', 'ACADEMY', false, '[지-분-지-분-지-분-분-지] 순서 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강지연', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강영종', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('강철중', 'MIXED', '개인분필은 직접 세팅하심', NULL, 'ACADEMY', false, '개인분필은 직접 세팅하심', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('고아름', 'MIXED', '칠판 가운데는 비우고 양 옆에 학원분필 각 1세트씩(총 2세트) 세팅', NULL, 'ACADEMY', true, '단상은 피피티 피해서 교단위로 올려주세요', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('권구승', 'PERSONAL', '강사님이 직접 세팅하심', NULL, 'ACADEMY', false, '강사님이 직접 세팅하심', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김강민', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김기대', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김기병', 'PERSONAL', NULL, NULL, 'ACADEMY', false, '포인터 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김기원', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김미향', 'PERSONAL', NULL, NULL, 'ACADEMY', true, '개인노트북+ppt = 직원세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김범찬', 'PERSONAL', '학원 분필도 일단 세팅', NULL, 'ACADEMY', false, '시험 30분', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김성도', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김연호', 'ACADEMY', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김윤환', 'ACADEMY', NULL, '쉬는시간에 패브릭으로 지워주세요', 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김재홍', 'ACADEMY', '학원분필 비어보이지않도록 많이 깔기, [ 지우개 + 흰2빨노파 + 흰분필2개 ] 로 5세트 학원 흰 분필 두 통 교탁위에 두기', '5세트 [지우개 + 흰2빨노파 + 흰분필2개]', 'PERSONAL', false, '흰 분필 두 통 교탁 위에 두기, 비어보이지 않게 많이 깔기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김재훈', 'ACADEMY', NULL, '스크린 기준 가운데 2개 + 스크린 벗어나서 2개', 'ACADEMY', false, '마이크 볼륨 작게(2/3까지)', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김종진', 'ACADEMY', NULL, NULL, 'ACADEMY', true, '빔프로젝터 켜기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김지혁', 'ACADEMY', NULL, NULL, 'ACADEMY', true, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김태훈', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('나진환', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('남지현', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('류동원', 'PERSONAL', '왼쪽 위 시간 적혀 있으면 지우지 말 것', NULL, 'PERSONAL', false, '왼쪽 위 시간 적혀 있으면 지우지 말 것', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('문서연', 'PERSONAL', NULL, '지우개패드 개인용 사용하심 (강의 전 개인용 붙여서 세팅하기), 여분 지우개 패드 5개, 옆책상에 꺼내두기 + 강의끝나고 강사님 가실때 개인용 지우개패드 가방에 넣어드리기 (쉬는시간, 수업끝나고 지우는건 우리껄로)', 'PERSONAL', true, '수업 후 개인 패드 가방에 넣어드리기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('박근수', 'ACADEMY', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('박준호', 'PERSONAL', NULL, '4개 (조금 멀리 세팅)', 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('박지윤', 'PERSONAL', NULL, NULL, 'ACADEMY', false, '포인터 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('배경민', 'PERSONAL', NULL, '지우개 옆 학원 노란색 1개 세팅', 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('변춘수', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('서준혁', 'PERSONAL', '강사님 오시면 분필 받아서 세팅', NULL, 'ACADEMY', false, '강사님 오시면 분필 받아서 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('서지현', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('성치경', 'MIXED', '학원 분필은 노란색만 세팅', NULL, 'PERSONAL', false, '학원 분필은 노란색만 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('송준혁', 'MIXED', '분필세팅 : 흰4(학원)+ 개인분필(6층강대실) 색깔별로 1개씩 + 혼자 와서 연습하시니까 안 하면 세팅(9:50까지) + 개인분필통은 세팅 후 교탁 옆에 통째로 두기', '스크린 화면 반 기준 좌측에만 4개', 'ACADEMY', false, '9:50까지 세팅 완료, 개인분필통은 교탁 옆에 두기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('심규원', 'ACADEMY', NULL, '물지우개 2개 세팅', 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('심용선', 'ACADEMY', NULL, NULL, 'ACADEMY', false, '마이크 볼륨 절반으로 줄이기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('심찬우', 'MIXED', '배치표 준수, 쉬는시간 없음, 영상 자료 촬영 시 조그 조정', '지우개 5개 + 문학 수업 시 마른 지우개 여분 5개씩 2세트', 'PERSONAL', true, '배치표 준수, 쉬는시간 없음, 영상 자료 촬영 시 조그 조정', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('안가람', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('엄소연', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('엄영대', 'MIXED', '포인터 세팅, 문항분석표 뽑기', '쉬는시간 패브릭 지우개로 정리', 'ACADEMY', true, '포인터 세팅, 문항분석표 뽑기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('오택민', 'PERSONAL', '남은 분필 상태에 따른 처리 규정 확인', '쉬는시간에 패브릭 지우개로 지우기', 'ACADEMY', false, '남은 분필 상태에 따른 처리 규정 확인', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('유성호', 'ACADEMY', '학원분필 5개 세팅', '지우개 + 물지우개 2개 (교탁 옆)', 'ACADEMY', false, '학원분필 5개 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('유신', 'PERSONAL', '포인터 세팅, 조그 터치 절대 금지', '스크린 안에 5개', 'ACADEMY', false, '포인터 세팅, 조그 터치 절대 금지', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('유주오', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('윤준수', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('윤지환', 'MIXED', '조그 판서 위주(인물 우측 고정) 촬영', NULL, 'ACADEMY', false, '조그 판서 위주(인물 우측 고정) 촬영', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이승헌', 'ACADEMY', '같이 오시는 실장님이 세팅하심', '연강 시 쉬는시간에 지우기', 'ACADEMY', false, '같이 오시는 실장님이 세팅하심', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이신혁', 'PERSONAL', NULL, NULL, 'ACADEMY', false, '마이크 소리 조금 줄이기', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이윤희', 'ACADEMY', NULL, '스크린 범위에 가깝게 세팅', 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이종걸', 'MIXED', '학원 분필은 흰색만 2개씩', '쉬는시간에 마른 지우개로 지우기', 'ACADEMY', false, '학원 분필은 흰색만 2개씩', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이종길', 'ACADEMY', '본인이 필요하면 직접 더 꺼내 쓰심', NULL, 'ACADEMY', false, '본인이 필요하면 직접 더 꺼내 쓰심', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이준호', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('장의순', 'ACADEMY', NULL, NULL, 'ACADEMY', false, '시험 20분', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('장재원', 'PERSONAL', NULL, NULL, 'PERSONAL', false, '조그 집중 촬영', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('전현정', 'PERSONAL', NULL, '쉬는시간 칠판 정리 x', 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('정승준', 'PERSONAL', '종이컵 속 분필 우선 사용', NULL, 'ACADEMY', false, '종이컵 속 분필 우선 사용', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('정재영', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('정재일', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('정태혁', 'MIXED', '(개인 분필은 강사 대기실) 학원 분필은 흰색만 2개씩 세팅', '지우개-분필 4세트 모두 피피티 안쪽으로 들어오도록 세팅', 'ACADEMY', true, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('조영상', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('조은정', 'PERSONAL', '분필 8개(보라 제외), 색별 하나는 케이스 장착', '패브릭 여분 5개 교탁 옆 세팅', 'ACADEMY', false, '분필 8개(보라 제외), 색별 하나는 케이스 장착', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('차주현', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('최원준', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('최은석', 'MIXED', '포인터 교탁 위 세팅', 'ppt 주변으로 4개', 'ACADEMY', true, '포인터 교탁 위 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('최정은', 'PERSONAL', '마이크 확인 필요', '칠판 중앙부터 우측으로 4개', 'ACADEMY', true, '마이크 확인 필요', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('최지욱', 'PERSONAL', '에어컨 18도 최대바람, 왼쪽 공지사항 확인', '지우개 6개 + 패브릭 6개 여분', 'PERSONAL', false, '에어컨 18도 최대바람, 왼쪽 공지사항 확인', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('탁윤석', 'MIXED', '본인 직접 세팅', NULL, 'ACADEMY', false, '본인 직접 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('한세빈', 'PERSONAL', '포인터 세팅, 강의영상+시험분석 메일 전송', '지우개 ppt 근처로 세팅', 'ACADEMY', true, '포인터 세팅, 강의영상+시험분석 메일 전송', true);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('한혜선', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('현유찬', 'PERSONAL', '6층 강사대기실에서 쓰시기 좋게만 꺼내놓기', NULL, 'PERSONAL', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('현정훈', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('황석진', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('황용일', 'PERSONAL', '직접 세팅', NULL, 'ACADEMY', true, '직접 세팅', false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('송지광', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('손정민', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('권경수', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('김성묵', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이서준', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('염소연', null, null, NULL, null, false, null, false);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, has_email) VALUES ('이태민', null, null, NULL, null, false, null, false);


-- ==============================================
-- 크루 (Crew)
-- 오전(MORNING) 3명: 오전 수업 전부터 근무 (~17:00)
-- 미들(MIDDLE)  2명: 09:00 ~ 17:00
-- 오후(AFTERNOON) 3명: 14:20 ~ 22:00
-- ==============================================
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('장강민', 'MORNING', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('김수민', 'MORNING', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('조하은', 'MORNING', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('문승찬', 'MIDDLE', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('임혜령', 'MIDDLE', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('박지호', 'AFTERNOON', true);
INSERT INTO crew (name, crew_type, is_onboarded) VALUES ('이서준', 'AFTERNOON', true);

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
