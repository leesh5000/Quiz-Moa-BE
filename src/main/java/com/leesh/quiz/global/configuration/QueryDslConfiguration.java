package com.leesh.quiz.global.configuration;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QueryDslConfiguration {

    private final EntityManager entityManager;

    // JPAQueryFactory의 동시성 문제는 EntityManager에 의존한다.
    // 스프링은 EntityManager에 프록시 객체를 주입받고, 트랜잭션 단위로 각자 바인딩되도록 라우팅 해주기 때문에 멀티스레드 환경에서 안전하다.
    // 도서 ORM 표준 JPA 13.1 트랜잭션 범위의 영속성 컨텍스트 참고
    @Bean
    public JPAQueryFactory jpaQueryFactory() {

        // Hibernate 6.x에서 HibernateHandler 대신 DefaultQueryHandler를 사용하는 현상 때문에 결과 집합 쿼리 Transfrom이 에러가 나는 현상이 있음
        // 따라서, JPAQueryFactory를 직접 생성해서 사용해야함
        // https://github.com/querydsl/querydsl/issues/3428 참고
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

}
