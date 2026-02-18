package com.example.talentoftime.support;


import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.mysql.MySQLContainer;

// ApplicationContextInitializer 구현하여, Spring 컨텍스트가 초기화 되기 전에 TestContainer로 MySQL 띄우고
// 그 접속 정보를 spring.datasource.* 프로퍼티로 주입
// static 으로 컨테이너를 띄워서 JVM 내에서 재사용
public class MySQLTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final MySQLContainer CONTAINER = new MySQLContainer("mysql:8.0");

    static {
        CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + CONTAINER.getUsername(),
                "spring.datasource.password=" + CONTAINER.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
