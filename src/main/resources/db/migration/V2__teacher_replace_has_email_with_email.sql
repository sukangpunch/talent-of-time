-- ============================================================
-- V2: Teacher 테이블 has_email(BOOLEAN) → email(VARCHAR) 변경
-- ============================================================

ALTER TABLE teacher
    ADD COLUMN email VARCHAR(255),
    DROP COLUMN has_email;
