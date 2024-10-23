package br.com.pupposoft.poc.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = { 
		UserDetailsServiceAutoConfiguration.class, 
		ReactiveUserDetailsServiceAutoConfiguration.class})
public class ServiceApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApiGatewayApplication.class, args);
	}

}
