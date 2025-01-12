package com.cjq.onlineshoppingapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("$security.jwt.token.key")
    private String key;

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("permissions", userDetails.getAuthorities());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Optional<AuthUserDetail> resolveToken(HttpServletRequest request) {
        String prefixedToken = request.getHeader("Authorization");
        System.out.println("prefixedToken: " + prefixedToken);

        if (prefixedToken == null || !prefixedToken.startsWith("Bearer ")) {
            return Optional.empty();
        }

        String token = prefixedToken.substring(7);

        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody(); // decode
        String username = claims.getSubject();
        List<LinkedHashMap<String, String>> permissions = (List<LinkedHashMap<String, String>>) claims.get("permissions");
        List<GrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.get("authority")))
                .collect(Collectors.toList());

        return Optional.of(AuthUserDetail.builder()
                .username(username)
                .authorities(authorities)
                .build());
    }
}
