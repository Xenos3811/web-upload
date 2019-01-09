package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableJpaRepositories("com.example.repository")
public class UploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(UploadApplication.class, args);
	}

	/**
	 * 设置上传文件大小，配置文件属性设置无效
	 *
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory config = new MultipartConfigFactory();
		config.setMaxFileSize("9000MB");
		config.setMaxRequestSize("9000MB");
		return config.createMultipartConfig();
	}

}
