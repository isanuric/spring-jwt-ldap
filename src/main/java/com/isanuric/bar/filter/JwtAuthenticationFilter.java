package com.isanuric.bar.filter;

import com.isanuric.bar.service.JwtService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private JwtService jwtService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    /**
     * If authentication is successful, create a JWS token and add it to AUTHORIZATION header.
     * The user should use this token to get access in next GET request.
     *
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth) {

        String token = jwtService.createJwsToken(auth.getName());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        logger.debug("authenticationException: {}", failed.toString());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
