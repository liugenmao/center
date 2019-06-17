package com.xiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CenterWebApplication extends ServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CenterWebApplication.class, args);
    }

}
