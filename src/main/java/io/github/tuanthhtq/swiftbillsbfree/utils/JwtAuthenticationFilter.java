package io.github.tuanthhtq.swiftbillsbfree.utils;

import io.github.tuanthhtq.swiftbillsbfree.configs.implement.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author io.github.tuanthhtq
 */


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailServiceImpl uds;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		final String header = request.getHeader(AUTHORIZATION);
		final String username;
		final String token;

		if (header == null || !header.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}

		token = header.substring(7);
		username = JwtProvider.getSubjectFromToken(token);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails ud = uds.loadUserByUsername(username);

			if (JwtProvider.validateToken(token, ud)) {
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}