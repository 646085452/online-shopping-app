package com.cjq.onlineshoppingapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;

    @Autowired
    public JwtFilter(final JwtProvider jwtProvider) { this.jwtProvider = jwtProvider; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<AuthUserDetail> authUserDetailOptional = jwtProvider.resolveToken(request);

        if (authUserDetailOptional.isPresent()) {
            AuthUserDetail authUserDetail = authUserDetailOptional.get();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authUserDetail.getUsername(),
                    null,
                    authUserDetail.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken); // put authentication object in securityContext
        }

        filterChain.doFilter(request, response);
    }
}
