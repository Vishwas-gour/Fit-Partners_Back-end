package com.fitPartner.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MyOncePerRequest extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final UserDetailsService service;

    public MyOncePerRequest(JwtUtil jwtUtil, UserDetailsService service) {
        System.out.println ("OncePerRequestFilter constructore");
        this.jwtUtil = jwtUtil;
        this.service = service;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI ().startsWith("/auth")) {
            filterChain.doFilter(request, response); // skip filter
            return;
        }
        String header = request.getHeader ("Authorization");
        String name = null;
        String token = null;

        if (header != null && header.startsWith ("Bearer ")) {
            token = header.substring (7);
            name = jwtUtil.extractName (token);
        }

        if (name != null && SecurityContextHolder.getContext ().getAuthentication () == null) {
            UserDetails details = service.loadUserByUsername (name);
            if (jwtUtil.isTokenValid (token, details)) {
                UsernamePasswordAuthenticationToken authenticate
                        = new UsernamePasswordAuthenticationToken (details, null, details.getAuthorities ());
                SecurityContextHolder.getContext ().setAuthentication (authenticate);
            }
        }
        filterChain.doFilter (request, response);

    }
}
