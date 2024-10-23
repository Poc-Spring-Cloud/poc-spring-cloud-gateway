package br.com.pupposoft.poc.springcloud.gateway.token;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenGateway implements TokenGateway {

	@Value("${api.security.token.secret}")
	private String secretKey;
	
	@Override
	public Object getInfoFromToken(String key, String token) {
		return getBodyfromToken(token).get(key);
	}
	
	private Claims getBodyfromToken(String token) {	 
		byte[] secretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		return Jwts.parserBuilder()
	        .setSigningKey(secretBytes)
	        .build()
	        .parseClaimsJws(token)
	        .getBody();
	}

}
