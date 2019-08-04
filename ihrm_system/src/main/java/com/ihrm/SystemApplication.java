package com.ihrm;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * @Author: 846602483
 * @Date: 2019-8-4 15:15
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = "com.ihrm")
@EntityScan("com.ihrm.system.domain")
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }
}
