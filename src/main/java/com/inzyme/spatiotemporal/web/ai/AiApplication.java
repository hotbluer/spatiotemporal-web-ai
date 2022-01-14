package com.inzyme.spatiotemporal.web.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableTransactionManagement

@SpringBootApplication(scanBasePackages={"com.inzyme.spatiotemporal.core,com.inzyme.spatiotemporal.web.core,com.inzyme.spatiotemporal.web.ai"},
//@SpringBootApplication(scanBasePackages={"com.inzyme.spatiotemporal.web.ai"},
                       exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@MapperScan("com.inzyme.spatiotemporal.web.ai.dao")
public class AiApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication app = new  SpringApplication(AiApplication.class);
		//app.addListeners(new ApplicationStartup());
		ApplicationContext context = app.run( args);
		String serverPort = context.getEnvironment().getProperty("server.port");
        log.info("启动成功! Swagger2: http://127.0.0.1:" + serverPort+"/swagger-ui.html");

	}

}
