package br.com.pupposoft.poc.springcloud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationRouter {

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	    		
	    		//TODO: não utilizar coringa (*), ou seja mapear somente as rotas que deve ser expostas
	    		
	    		.route("carrinho-compra-service-route", r -> r.path("/carrinho-de-compra/**").uri("lb://POC-SPRING-CLOUD-CARRINHO-COMPRA-SERVICE"))
	    		.route("usuario-service-route", r -> r.path("/usuario/**").uri("lb://POC-SPRING-CLOUD-USUARIO-SERVICE"))
//	            .route(p -> p
//	                    .path("/get")
//	                    //.filters(f -> f.addRequestHeader("Hello", "World"))
//	                    .uri("http://httpbin.org:80"))
	    		.build();
	}
	
}
