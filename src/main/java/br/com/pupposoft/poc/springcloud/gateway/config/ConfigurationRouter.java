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
	    		
	    		.route("produto-service-route", r -> r.path("/produtos/**").uri("lb://PRODUTO-SERVICE"))
	    		.route("carrinho-service-route", r -> r.path("/carrinhos/**").uri("lb://CARRINHO-SERVICE"))
	    		.route("pedido-service-route", r -> r.path("/pedidos/**").uri("lb://PEDIDO-SERVICE"))
	    		.route("pagamento-service-route", r -> r.path("/pagamentos/**").uri("lb://PAGAMENTO-SERVICE"))
	    		.route("usuario-service-route", r -> r.path("/usuarios/**").uri("lb://USUARIO-SERVICE"))
	    		.route("estoque-service-route", r -> r.path("/estoques/**").uri("lb://ESTOQUE-SERVICE"))
//	            .route(p -> p
//	                    .path("/get")
//	                    //.filters(f -> f.addRequestHeader("Hello", "World"))
//	                    .uri("http://httpbin.org:80"))
	    		.build();
	}
	
}
