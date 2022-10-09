/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth.sec;

import com.auth.JWTUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author riyaro
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refreshToken")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationToken = request.getHeader(JWTUtils.AUTH_HEADER);
            if (authorizationToken != null && authorizationToken.startsWith(JWTUtils.FREFIX)) {
                try {
                    String jwt = authorizationToken.substring(JWTUtils.FREFIX.length());
                    Algorithm algorithm = Algorithm.HMAC256(JWTUtils.SECRET_KEY);
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                    String username = decodedJWT.getSubject();
//            on recupère les role en convertissant en liste de string 
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authoritys = new ArrayList<>();
                    for (String r : roles) {
                        authoritys.add(new SimpleGrantedAuthority(r));
                    }
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(username, null, authoritys);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (JWTVerificationException | IOException | IllegalArgumentException | ServletException e) {
                    response.setHeader("error-message", e.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
