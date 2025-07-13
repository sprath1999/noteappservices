package in.noteapp.spring.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;

@Component
public class JwtUtil {
	
	private static final long EXPIRATION_TIME = 86400000;
	
	private final Key key=Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key)
				.compact();
		
	}
	
	public String extractUsername(String token) {
		return parseClaims(token).getSubject();
	}
	
	  public boolean validateToken(String token) {
	        try {
	            parseClaims(token);
	            return true;
	        } catch (JwtException | IllegalArgumentException e) {
	            return false;
	        }
	    }
	    
	    private Claims parseClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
}
