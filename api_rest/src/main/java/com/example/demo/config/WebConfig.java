package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedMethods("GET", "POST", "PUT", "DELETE")
		.allowedHeaders("*")
		.allowedOrigins("*")
		;
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	configurer.favorParameter(true)
	.parameterName("mediaType").ignoreAcceptHeader(false)
	.useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON)
	.mediaType("json", MediaType.APPLICATION_JSON)
	.mediaType("xml", MediaType.APPLICATION_XML);
	}
	

}
