/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.service;

import com.auth.entities.AppRole;
import com.auth.entities.AppUser;
import com.auth.repo.RoleRepository;
import com.auth.repo.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author riyaro
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser saveUser(AppUser appUser) {
        String pwd=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pwd));
        return userRepository.save(appUser);
    }

    @Override
    public AppRole saveRole(AppRole appRole) {
        return roleRepository.save(appRole);
    }

    @Override
    public void addUserToRole(String username, String roleName) {
        AppUser user = userRepository.findByUsername(username);
        AppRole role = roleRepository.findByRoleName(roleName);
        user.getAppRoles().add(role);
    }

    @Override
    public List<AppUser> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
