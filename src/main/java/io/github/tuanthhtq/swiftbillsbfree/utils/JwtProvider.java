package io.github.tuanthhtq.swiftbillsbfree.utils;

import io.github.tuanthhtq.swiftbillsbfree.configs.implement.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

/**
 * @author io.github.tuanthhtq
 */

@Component
public class JwtProvider {
	private static final String SECRET = "dolor amet, cursus nec consectetur ipsum Aenean tristique, ante Lorem eu, mollis nunc sit id nunc. lacus elit. tortor Fusce adipiscing";

	public static String create(UserDetailsImpl userDetails) {
		Instant expire = Instant.now().plus(30, ChronoUnit.DAYS);
		return "Bearer " + Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("authorities", userDetails.getAuthorities())
				.setExpiration(Date.from(expire))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
	}

	public static Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody();
	}

	public static <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
		Claims claim = getAllClaimsFromToken(token);
		return resolver.apply(claim);
	}

	public static String getSubjectFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public static Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public static Boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).before(new Date());
	}

	public static Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getSubjectFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}