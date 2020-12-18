package com.modules.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.modules.api.dao")
@EnableSwagger2
public class ApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class,args);
  }
}
