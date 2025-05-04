package com.bookstore.users.security;

import com.bookstore.users.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final String AUTH_TYPE = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        final String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null || !jwtToken.startsWith(AUTH_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = jwtToken.substring(AUTH_TYPE.length()).trim();

        if (!jwtUtil.isVerified(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username = jwtUtil.getUsername(token);
        final String[] authorities = jwtUtil.getAuthorities(token);

        final List<SimpleGrantedAuthority> grantedAuthorities = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
