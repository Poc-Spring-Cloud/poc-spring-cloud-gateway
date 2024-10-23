package br.com.pupposoft.poc.springcloud.gateway.token;

public interface TokenGateway {

	Object getInfoFromToken(String key, String token);

}
