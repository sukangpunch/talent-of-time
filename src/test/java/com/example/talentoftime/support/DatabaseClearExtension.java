package com.example.talentoftime.support;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// JUnit 5의 BeforeEachCallback 을 구현하여 매 테스트 메서드 실행 전 DatabaseCleaner.clear() 호출
// SpringExtension을 통해 ApplicationContext에서 DatabaseCleaner 빈을 직접 꺼내 사용
public class DatabaseClearExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        DatabaseCleaner databaseCleaner = getDataCleaner(context);
        databaseCleaner.clear();
    }

    private DatabaseCleaner getDataCleaner(ExtensionContext extensionContext) {
        return SpringExtension.getApplicationContext(extensionContext)
                .getBean(DatabaseCleaner.class);
    }
}
