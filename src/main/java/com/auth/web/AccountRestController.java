/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth.web;

import com.auth.JWTUtils;
import com.auth.entities.AppRole;
import com.auth.entities.AppUser;
import com.auth.sec.JWTAuthenticationFilter;
import com.auth.service.AuthService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author riyaro
 */
@Controller
@RestController
public class AccountRestController {

    private AuthService authService;

    public AccountRestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> getAllUser() {
        return authService.getAllUser();
    }

    @PostMapping("/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser addUser(@RequestBody AppUser user) {
        return authService.saveUser(user);
    }

    @PostMapping("/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole addRole(@RequestBody AppRole role) {
        return authService.saveRole(role);
    }

    @PostMapping("addUserToRole")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void addUserToRole(@RequestBody UserRoleForm userRoleForm) {
        authService.addUserToRole(userRoleForm.getRoleName(), userRoleForm.getUsername());
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = request.getHeader(JWTUtils.AUTH_HEADER);

        if (authToken != null && authToken.startsWith(JWTUtils.FREFIX)) {
            try {
                String refreshToken = authToken.substring(JWTUtils.FREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtils.SECRET_KEY);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user = authService.loadUserByUsername(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTUtils.EXPIRE_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAppRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();
                idToken.put("access-token", accessToken);
                idToken.put("refresh-token", refreshToken);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);

            } catch (JWTCreationException | JWTVerificationException | IOException | IllegalArgumentException e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh token required");
        }
    }

    @GetMapping("/profile")
    public AppUser profile(Principal principal) {
        return authService.loadUserByUsername(principal.getName());
    }
}

@Data
class UserRoleForm {

    String username;
    String roleName;
}
