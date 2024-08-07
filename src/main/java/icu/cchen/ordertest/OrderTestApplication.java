package icu.cchen.ordertest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("icu.cchen.ordertest.mapper")
public class OrderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderTestApplication.class, args);
    }
}
