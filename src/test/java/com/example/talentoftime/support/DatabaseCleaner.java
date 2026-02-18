package com.example.talentoftime.support;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 각 테스트 실행 전 db 상태롤 초기화 하는 컴포넌트
// INFORMATION_SCHEMA에서 현재 DB의 모든 테이블을 조회한 뒤 TRUNCATE를 실행
// FOREIGN_KEY_CHECKS = 0으로 외래키 제약을 임시 해제하여 테이블 간 의존성 없이 순서와 무관하게 truncate
@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void clear() {
        em.clear();
        truncate();
    }

    private void truncate() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        getTruncateQueries().forEach(query -> em.createNativeQuery(query).executeUpdate());
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @SuppressWarnings("unchecked")
    private List<String> getTruncateQueries() {
        String sql = """
                     SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') AS q
                     FROM INFORMATION_SCHEMA.TABLES
                     WHERE TABLE_SCHEMA = (SELECT DATABASE())
                     AND TABLE_TYPE = 'BASE TABLE'
                     """;

        return em.createNativeQuery(sql).getResultList();
    }
}
