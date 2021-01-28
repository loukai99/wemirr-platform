package com.wemirr.platform.bury;

import com.wemirr.framework.boot.config.EnableGlobalException;
import com.wemirr.framework.security.client.annotation.EnableOauth2ClientResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@EnableGlobalException
@EnableOauth2ClientResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.wemirr.**.mapper", annotationClass = Repository.class)
public class BuryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuryApplication.class, args);
    }

}
