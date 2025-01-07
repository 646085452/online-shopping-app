package com.cjq.onlineshoppingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAspectJAutoProxy
public class OnlineShoppingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingAppApplication.class, args);
    }

}
