package com.xiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CenterWebApplication extends ServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CenterWebApplication.class, args);
    }

}
