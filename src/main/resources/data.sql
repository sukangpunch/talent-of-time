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
-- 강사 (Teacher) 강의실 세팅 데이터
--
-- [공통 준수 사항]
-- 1. 개인분필 세팅방법 미기재 시: 쓰시기 편하게만 세팅
-- 2. 마이크 커버: 매교시 교체 / 배터리: 1교시 수업 전, 2/4교시 종료 후 교체 (단, 2칸 이하면 즉시 교체)
-- 3. 분필 기본 세팅(학원): 흰2, 노1, 빨1, 파1 / 기본 지우개: 4개
-- 4. 서바이벌 시즌: 정오사항 바로 지우지 말고 강사님께 지우기 여부 확인 필수
-- ==============================================

-- ==============================================
-- 강사 (Teacher) 강의실 세팅 데이터
--
-- [공통 준수 사항]
-- 1. 개인분필 세팅방법 미기재 시: 쓰시기 편하게만 세팅
-- 2. 마이크 커버: 매교시 교체 / 배터리: 1교시 수업 전, 2/4교시 종료 후 교체 (단, 2칸 이하면 즉시 교체)
-- 3. 분필 기본 세팅(학원): 흰2, 노1, 빨1, 파1 / 기본 지우개: 4개
-- 4. 서바이벌 시즌: 정오사항 바로 지우지 말고 강사님께 지우기 여부 확인 필수
-- ==============================================

-- ==============================================
-- 강사 (Teacher) 강의실 세팅 데이터
--
-- [공통 준수 사항]
-- 1. 개인분필 세팅방법 미기재 시: 쓰시기 편하게만 세팅
-- 2. 마이크 커버: 매교시 교체 / 배터리: 1교시 수업 전, 2/4교시 종료 후 교체 (단, 2칸 이하면 즉시 교체)
-- 3. 분필 기본 세팅(학원): 흰2, 노1, 빨1, 파1 / 기본 지우개: 4개
-- 4. 서바이벌 시즌: 정오사항 바로 지우지 말고 강사님께 지우기 여부 확인 필수
-- ==============================================

-- 강기원T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강기원', 'ACADEMY', '학원분필 사용 (흰4, 노4, 빨2, 파2 세팅)', '5개, 쉬는시간 3분간 사용하던 지우개로만 정리(물x)', 'PERSONAL', false, '좌측 topic/개념설명 지우지 않고 우측 문제들만 지우기 (#문항번호 표시)', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강수영', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강승연', 'PERSONAL', '강의 시작 전 파우치 받아서 분필 세팅 (쓰기 편하게)', NULL, 'ACADEMY', true, '칠판 조명 끄기 / 종료 후 개인물품 정리하여 6층 강대실 강승연T에게 파우치 전달 / 강의 종료 후 영상 메일 전송 필수', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강준호', 'MIXED', '분필 1세트 : 학원흰색2+개인컬러분필(빨노파)(6층교재실)', '[분필-지우개-분필-지우개-분필-분필-지우개] 세팅 + 젖은 물 지우개 6개 교탁 옆에 세팅', 'ACADEMY', false, '[지-분-지-분-지-분-분-지] 순서 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강지연', 'ACADEMY', NULL, 'PPT 범위 내로 4개', 'ACADEMY', true, 'PPT 켜기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강영종', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('강철중', 'MIXED', '개인분필은 직접 세팅하심', NULL, 'ACADEMY', false, '개인분필은 직접 세팅하심', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('고아름', 'MIXED', '칠판 가운데는 비우고 양 옆에 학원분필 각 1세트씩(총 2세트) 세팅', NULL, 'ACADEMY', true, '단상은 피피티 피해서 교단위로 올려주세요', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('권구승', 'PERSONAL', '강사님이 직접 세팅하심', NULL, 'ACADEMY', false, '강사님이 직접 세팅하심', NULL);

-- 김강민T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김강민', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김기대', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김기병', 'PERSONAL', NULL, NULL, 'ACADEMY', false, '포인터 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김기원', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김미향', 'PERSONAL', NULL, NULL, 'ACADEMY', true, '개인노트북+ppt = 직원세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김범찬', 'PERSONAL', '학원 분필도 일단 세팅', NULL, 'ACADEMY', false, '시험 30분', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김성도', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김연호', 'ACADEMY', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김윤환', 'ACADEMY', NULL, '쉬는시간에 패브릭으로 지워주세요', 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김재홍', 'ACADEMY', '학원분필 비어보이지않도록 많이 깔기, [지우개 + 흰2빨노파 + 흰분필2개] 로 5세트, 학원 흰 분필 두 통 교탁위에 두기', '5세트 [지우개 + 흰2빨노파 + 흰분필2개]', 'PERSONAL', false, '흰 분필 두 통 교탁 위에 두기, 비어보이지 않게 많이 깔기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김재훈', 'ACADEMY', '스크린 기준 가운데 2개 + 스크린 벗어나서 2개', '스크린 기준 가운데 2개 + 스크린 벗어나서 2개', 'ACADEMY', false, '마이크 볼륨 작게(2/3까지만)', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김종진', 'ACADEMY', NULL, NULL, 'ACADEMY', true, '빔프로젝터 켜기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김지연', 'ACADEMY', NULL, NULL, 'ACADEMY', false, '기본 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김지혁', 'ACADEMY', NULL, NULL, 'ACADEMY', true, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김태훈', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);

-- 나진환T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('나진환', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('남지현', 'PERSONAL', '6층 교재실 보관', NULL, 'PERSONAL', false, NULL, NULL);

-- 류동원T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('류동원', 'PERSONAL', '6층 강사대기실 보관 (직접 세팅하심)', NULL, 'PERSONAL', false, '왼쪽 위 시간 적혀 있으면 지우지 말 것', NULL);

-- 문서연T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('문서연', 'PERSONAL', NULL, '지우개패드 개인용 사용 (강의 전 세팅, 여분 5개 옆책상) / 쉬는시간 및 수업끝 정리는 학원 지우개 사용', 'PERSONAL', true, '수업 후 개인 패드 가방에 넣어드리기', NULL);

-- 박근수T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('박근수', 'ACADEMY', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('박준호', 'PERSONAL', NULL, '4개 (조금 멀리 세팅)', 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('박지윤', 'PERSONAL', '6층 강사대기실 보관', NULL, 'ACADEMY', false, '포인터 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('배경민', 'PERSONAL', '지우개 옆에 학원분필 노란색 1개만 세팅', NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('변춘수', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);

-- 서준혁T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('서준혁', 'PERSONAL', '강사님 오시면 분필 받아서 세팅', NULL, 'ACADEMY', false, '강사님 오시면 분필 받아서 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('서지현', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('성치경', 'MIXED', '학원 분필은 노란색만 세팅', NULL, 'PERSONAL', false, '학원 분필은 노란색만 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('송준혁', 'MIXED', '6층 강사대기실 보관 / 학원 흰4 + 개인분필 색깔별 1개씩 (안하면 9:50까지 세팅, 개인분필통은 교탁 옆 통째로)', '스크린 화면 반 기준 좌측에만 4개', 'ACADEMY', false, '9:50까지 세팅 완료, 개인분필통은 교탁 옆에 두기', NULL);

-- 심규원T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('심규원', 'ACADEMY', NULL, '물지우개 2개 세팅, 여분 준비', 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('심용선', 'ACADEMY', NULL, NULL, 'ACADEMY', false, '마이크 볼륨 절반으로 줄이기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('심찬우', 'MIXED', '칠판 중앙부터 우측으로 [지-지-학원(흰4노1파1)-개인4-개인3-지-학원(흰4노1파1)-개인(파1보1노1초1)-개인(주1빨1흰1)-지] 세트 반복', '지우개 5개 + 문학 수업 시 마른 지우개 여분 5개씩 2세트', 'PERSONAL', true, '배치표 준수, 쉬는시간 없음, 영상 자료 촬영 시 조그 조정(줌아웃 후 줌인)', NULL);

-- 안가람T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('안가람', 'ACADEMY', '학원분필 세팅 (단, 개인분필 사용시 6층 강사대기실 보관됨)', NULL, 'ACADEMY', false, NULL, NULL);

-- 엄소연 -> 염소연 (수정됨)
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('염소연', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);

INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('엄영대', 'MIXED', '학원분필+개인분필(6층 강사대기실 보관, 가져다 놓으면 직접 세팅하심)', '쉬는시간 패브릭 지우개로 정리', 'ACADEMY', true, '포인터 세팅, 문항분석표만 뽑기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('오택민', 'PERSONAL', '직접 가져와서 세팅하심 (남은 분필 50% 이하시 버리기, 이상은 연강시 사용 또는 보관함)', '쉬는시간에 패브릭 지우개로 지우기', 'ACADEMY', false, '남은 분필 상태에 따른 처리 규정 확인', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('우주설', 'PERSONAL', '6층 강사대기실 보관', NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('유성호', 'ACADEMY', '학원분필 5개 세팅', '지우개 + 물지우개 2개 (교탁 옆)', 'ACADEMY', false, '학원분필 5개 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('유신', 'PERSONAL', '포인터 세팅', '스크린 안에 5개', 'ACADEMY', false, '주차별 분권 교재, 조그 터치 절대 금지 (쉬는시간 포함)', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('유주오', 'ACADEMY', NULL, NULL, 'ACADEMY', false, NULL, NULL);

-- 윤준수T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('윤준수', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('윤지환', 'MIXED', '선생님께 받아서 중앙에 분필 두기', NULL, 'ACADEMY', false, '조그 판서 위주 촬영 (선생님이 우측 끝에 위치하도록 잡기, 줌아웃 금지)', NULL);

-- 이승헌T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이승헌', 'ACADEMY', '같이 오시는 실장님이 세팅하심 (개인분필 보관시 6층 강사대기실)', '연강 시 쉬는시간에 지우기', 'ACADEMY', false, '같이 오시는 실장님이 세팅하심', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이신혁', 'PERSONAL', '6층 강사대기실 보관', NULL, 'ACADEMY', false, '마이크 소리 조금 줄이기', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이윤희', 'ACADEMY', '스크린 범위에 가깝게 세팅', '스크린 범위에 가깝게 세팅', 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이종걸', 'MIXED', '학원 분필은 흰색만 2개씩', '쉬는시간에 마른 지우개로 지우기', 'ACADEMY', false, '학원 분필은 흰색만 2개씩 (현재 개인마이크 고장으로 임시 학원마이크 세팅)', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이종길', 'ACADEMY', '기본 세팅 (본인이 필요하면 직접 더 꺼내 쓰심)', NULL, 'ACADEMY', false, '본인이 필요하면 직접 더 꺼내 쓰심', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이준호', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, NULL);

-- 장의순T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('장의순', 'ACADEMY', NULL, NULL, 'ACADEMY', false, '시험 20분', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('장재원', 'PERSONAL', NULL, NULL, 'PERSONAL', false, '조그 집중 촬영', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('전현정', 'PERSONAL', '6층 강사대기실 보관', '쉬는시간 칠판 정리 x', 'ACADEMY', false, NULL, NULL);

-- 정승준T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('정승준', 'PERSONAL', '6층 강사대기실 보관 / 분필 상자 속 종이컵 분필 우선 세팅 (흰2 빨노파 1개씩 지우개 옆)', NULL, 'ACADEMY', false, '종이컵 속 분필 우선 사용', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('정재영', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('정재일', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('정태혁', 'MIXED', '학원 분필은 흰색만 2개씩 세팅 (개인 분필은 강사 대기실)', '지우개-분필 4세트 모두 피피티 안쪽으로 들어오도록 세팅', 'ACADEMY', true, NULL, NULL);

-- 조영상T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('조영상', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('조은정', 'PERSONAL', '6층 강사대기실 보관 / 칠판 중앙에 핑크,주황,노랑,초록 2개씩 총 8개 세팅(보라X). 색별 1개는 케이스 장착', '패브릭 여분 5개 교탁 옆 세팅', 'ACADEMY', false, '분필 8개(보라 제외), 색별 하나는 케이스 장착', NULL);

-- 차주현T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('차주현', 'PERSONAL', NULL, NULL, 'PERSONAL', false, NULL, NULL);

-- 최원준T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('최원준', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('최은석', 'MIXED', '포인터 교탁 위 세팅', 'ppt 주변으로 4개', 'ACADEMY', true, '포인터 교탁 위 세팅', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('최정은', 'PERSONAL', '6층 강사대기실 보관', '칠판 중앙부터 우측으로 4개', 'ACADEMY', true, '마이크 확인 필요', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('최지욱', 'PERSONAL', '강사님 공구박스에서 분필만 꺼내서 세팅 / 분필머리가 왼쪽으로 오도록 [흰 빨 주 노 초 파 보] 일렬 3세트 (왼쪽/중앙/오른쪽) / 다음교시 위해 케이스 닦기', '지우개 6개 + 패브릭 6개 여분, [지우개-분필-지우개] 순서로 3세트', 'PERSONAL', false, '에어컨 18도 최대바람 / 학생 교재 한세트 세팅 / 왼쪽 공지사항 확인(있으면 지우지 말 것)', NULL);

-- 탁윤석T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('탁윤석', 'MIXED', '본인 직접 세팅', NULL, 'ACADEMY', false, '본인 직접 세팅', NULL);

-- 한세빈T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('한세빈', 'PERSONAL', '6층 강사대기실 보관', '지우개 ppt 근처로 세팅', 'ACADEMY', true, '교탁에 포인터 세팅, 시험 타이머 직접 켜심, 강의영상+시험분석 메일 전송', NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('한혜선', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);

-- 현유찬T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('현유찬', 'PERSONAL', '6층 강사대기실에서 쓰시기 좋게만 꺼내놓기', NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('현정훈', 'PERSONAL', NULL, NULL, 'ACADEMY', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('홍준석', 'PERSONAL', '6층 교재실 보관', '쉬는시간에 패브릭으로 칠판 지우기', 'PERSONAL', false, NULL, NULL);

-- 황석진T ~
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('황석진', 'PERSONAL', NULL, NULL, 'ACADEMY', true, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('황용일', 'PERSONAL', '직접 세팅', NULL, 'ACADEMY', true, '직접 세팅', NULL);

-- 기타 (송지광T~ )
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('송지광', 'PERSONAL', '선생님 오시면 분필 받아서 세팅하기', NULL, 'PERSONAL', false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('손정민', NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('권경수', null, null, NULL, null, false, null, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('김성묵', NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이서준', NULL, NULL, NULL, NULL, false, NULL, NULL);
INSERT INTO teacher (name, chalk_type, chalk_detail, eraser_detail, mic_type, has_ppt, notes, email) VALUES ('이태민', NULL, NULL, NULL, NULL, false, NULL, NULL);
