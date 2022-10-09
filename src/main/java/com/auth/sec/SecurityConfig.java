/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth.sec;

import com.auth.entities.AppUser;
import com.auth.service.AuthService;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author riyaro
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    UserDetailsServiceImpl userDetailsServiceImpl;
    
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl){
        this.userDetailsServiceImpl=userDetailsServiceImpl;
    }
        
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeHttpRequests().antMatchers("/h2-console/**","/refreshToken/**","/login/**").permitAll();
        // http.formLogin();
//       on peut autoriser directement les ressources aux roles
//        http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority("ADMIN", "CUSTOMER_MANAGER");
//        http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("USER");

        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
//    } 

}
