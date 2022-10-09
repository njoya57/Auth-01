/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth.sec;

import com.auth.JWTUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author riyaro
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    AuthenticationManager authManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("ATTEMPT AUTHENTICATION");
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");
        System.out.println("username : " + username);
        System.out.println("password : " + pwd);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, pwd);
        return authManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("SUCCESSFUL AUTHENTICATION");
        User user = (User) authResult.getPrincipal();
//        générer jwt
        Algorithm algorithm = Algorithm.HMAC256(JWTUtils.SECRET_KEY);
        String jwtAccessToken = JWT.create()
                .withSubject(user.getUsername())
                //////            on definit la date d'expiration, ici 5 minutes'
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtils.EXPIRE_ACCESS_TOKEN))
                //            nom de l'application qui genère le token. url de la requete, converti en string
                .withIssuer(request.getRequestURL().toString())
                //            recupère la liste des roles et convertis en liste de String
                .withClaim("roles", user.getAuthorities().stream().map(au -> au.getAuthority()).collect(Collectors.toList()))
                //            puis on signe
                .sign(algorithm);

        String jwtRefreshToken = JWT.create()
                .withSubject(user.getUsername())
                //////            on definit la date d'expiration, ici 5 minutes'
                .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtils.EXPIRE_REFRESH_TOKEN))
                //            nom de l'application qui genère le token. url de la requete, converti en string
                .withIssuer(request.getRequestURL().toString())
                //            recupère la liste des roles et convertis en liste de String
                //            puis on signe
                .sign(algorithm);

//    on envoi au client, ici on envoi dans un header
//        response.setHeader("Authorization", jwtAccessToken);
//    ici on envoi dans le corps de la reponse
        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", jwtAccessToken);
        idToken.put("refresh-token", jwtRefreshToken);
//        //ici on precise que le contenu contient des données json
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }

}
