package com.project.foodx;

import com.project.foodx.auth.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;


@SpringBootApplication
public class FoodxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodxApplication.class, args);
	}

	//Access-Control-Allow-Credentials: true
	///Access-Control-Allow-Headers: Content-Type, Accept, X-Requested-With, remember-me
	//Access-Control-Allow-Methods: POST, GET, OPTIONS, DELETE
	//Access-Control-Allow-Origin: http://localhost:3000
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("https://generic-matrix.github.io");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("*");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("*");
		return registrationBean;
	}

}
