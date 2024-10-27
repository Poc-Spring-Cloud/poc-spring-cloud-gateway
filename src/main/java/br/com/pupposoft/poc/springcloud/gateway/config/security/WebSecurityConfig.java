package br.com.pupposoft.poc.springcloud.gateway.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

import br.com.pupposoft.poc.springcloud.gateway.token.TokenGateway;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class WebSecurityConfig {

	private TokenGateway tokenGateway;
	
	@Bean
	public WebFilter tokenInterceptorFilter() {
		return new TokenInterceptorFilter();
	}

	@Bean
	@DependsOn({"tokenInterceptorFilter", "tokenInterceptorFilter"})
	public SecurityWebFilterChain filterChain(ServerHttpSecurity serverHttpSecurity, TokenInterceptorFilter tokenInterceptorFilter) {
		
		SecurityContext securityContext = getSecurityContext();
		ServerSecurityContextRepository securityContextRepository = getSecurityContextRepository(securityContext);
		
		serverHttpSecurity.csrf(CsrfSpec::disable);
		serverHttpSecurity.securityContextRepository(securityContextRepository);
		tokenInterceptorFilter.setSecurityContext(securityContext);
		tokenInterceptorFilter.setTokenGateway(tokenGateway);

		return serverHttpSecurity.authorizeExchange(
				exchanges -> exchanges
				//.anyExchange().permitAll()
				.pathMatchers(HttpMethod.POST, "/carrinho-de-compra/**").permitAll()
				.pathMatchers(HttpMethod.POST, "/usuario/usuarios").permitAll()//FIXME:
				.pathMatchers(HttpMethod.POST, "/usuario/usuarios/login").permitAll()
//				.pathMatchers(HttpMethod.GET, "/produtos").permitAll()
//				.pathMatchers(HttpMethod.GET, "/produtos/*").permitAll()
//				.pathMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
//				.pathMatchers(HttpMethod.PUT, "/produtos/*").hasRole("ADMIN")
//				.pathMatchers(HttpMethod.DELETE, "/produtos/*").hasRole("ADMIN")
//				.pathMatchers("/estoques/atualizar").hasRole("ADMIN")
//				.pathMatchers("/carrinhos/*/*").hasRole("USER")
//				.pathMatchers("/carrinhos/*").hasRole("USER")
//				.pathMatchers(HttpMethod.POST, "/pedidos").hasRole("USER")
//				.pathMatchers(HttpMethod.POST, "/pagamentos/processar").permitAll()//Na pratica deve ser protegido (configurar token especifico da plataforma de pagamento)
				)
				.addFilterAt(tokenInterceptorFilter, SecurityWebFiltersOrder.AUTHORIZATION)
				.build();
	}
	
	private SecurityContext getSecurityContext() {
		return new SecurityContext() {
			private static final long serialVersionUID = 3309645652112048124L;
			
			private Authentication authentication;
			
			@Override
			public void setAuthentication(Authentication authentication) {
				this.authentication = authentication;
				
			}
			
			@Override
			public Authentication getAuthentication() {
				return authentication;
			}
		};
	}
	
	private ServerSecurityContextRepository getSecurityContextRepository(SecurityContext securityContext) {
		return new ServerSecurityContextRepository() {
			
			@Override
			public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
				return Mono.empty();
			}
			
			@Override
			public Mono<SecurityContext> load(ServerWebExchange exchange) {
				return Mono.just(securityContext);
			}
		};
	}	
}
