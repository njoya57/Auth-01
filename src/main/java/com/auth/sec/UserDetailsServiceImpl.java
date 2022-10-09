/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.auth.sec;

import com.auth.entities.AppUser;
import com.auth.service.AuthService;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author riyaro
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthService authService;

    public UserDetailsServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = authService.loadUserByUsername(username);
        Collection<GrantedAuthority> grantedAuthoritys = new ArrayList<>();
        user.getAppRoles().forEach(r -> {
            grantedAuthoritys.add(new SimpleGrantedAuthority(r.getRoleName()));
        });
        return new User(user.getUsername(), user.getPassword(), grantedAuthoritys);
    }

}
