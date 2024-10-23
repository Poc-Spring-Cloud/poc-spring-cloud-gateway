package br.com.pupposoft.poc.springcloud.gateway.config.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import br.com.pupposoft.poc.springcloud.gateway.token.TokenGateway;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class TokenInterceptorFilter implements WebFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USUARIO_ID = "userId";
    private static final String USUARIO_PERFIL = "userRole";
    
    @Setter
    private TokenGateway tokenGateway;

    @Setter
    private SecurityContext securityContext;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
		
		ServerHttpRequest request = exchange.getRequest();
		
		if( HttpMethod.OPTIONS.equals(request.getMethod()) || request.getURI().getPath().contains("actuator")) {
			return filterChain.filter(exchange);
		}
		
		HttpHeaders headers = request.getHeaders();
		HttpServletRequest requestWrapper = new HeaderMapRequestWrapper(headers);
		
		try {
			List<String> authorizations = headers.get(HttpHeaders.AUTHORIZATION);
			if(authorizations == null || authorizations.isEmpty()) {
				createInvalidAutentication();
				return filterChain.filter(exchange);
			}
			final String baererToken = authorizations.get(0);
			if(isValidToken(baererToken)) {
				
				final String token = baererToken.substring(7, baererToken.length());
				
				final Map<String, String> headersInfos = getInfos(token);

				addTokenInfosIntoHeader((HeaderMapRequestWrapper) requestWrapper, headersInfos);
				
				createAutentication(headersInfos);
				return filterChain.filter(exchange);
			}
			createInvalidAutentication();
			return filterChain.filter(exchange);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			createInvalidAutentication();
			return filterChain.filter(exchange);
		} 
	} 

	
	private void createAutentication(final Map<String, String> headersInfos) {
		String role = headersInfos.get(USUARIO_PERFIL);
		Collection<? extends GrantedAuthority> authorities = role == null ? null : Arrays.asList(() -> "ROLE_" + role);
		Authentication auth = new UsernamePasswordAuthenticationToken("admin", null, authorities);
		securityContext.setAuthentication(auth);
	}
	
	private void createInvalidAutentication() {
		Collection<? extends GrantedAuthority> authorities = Arrays.asList(() -> "");
		Authentication auth = new UsernamePasswordAuthenticationToken("", null, authorities);
		securityContext.setAuthentication(auth);
	}

	
	private boolean isValidToken(String token) {
		return token != null && !token.isEmpty() && token.startsWith(TOKEN_PREFIX);
	}

	
	private void addTokenInfosIntoHeader(final HeaderMapRequestWrapper requestWrapper, final Map<String, String> headersInfos) {	
		requestWrapper.addHeader(USUARIO_ID, headersInfos.get(USUARIO_ID));
		requestWrapper.addHeader(USUARIO_PERFIL, headersInfos.get(USUARIO_PERFIL));
	}
	
	private Map<String, String> getInfos(final String token){
		final Map<String, String> loggedInfos = new HashMap<>();
		loggedInfos.put(USUARIO_ID, tokenGateway.getInfoFromToken(USUARIO_ID, token).toString());
		loggedInfos.put(USUARIO_PERFIL, tokenGateway.getInfoFromToken(USUARIO_PERFIL, token).toString());

		return loggedInfos;
	}
}
