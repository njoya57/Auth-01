/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.service;

import com.auth.entities.AppRole;
import com.auth.entities.AppUser;
import java.util.List;

/**
 *
 * @author riyaro
 */
public interface AuthService {

    AppUser saveUser(AppUser appUser);

    AppRole saveRole(AppRole appRole);

    void addUserToRole(String username, String roleName);

    public List<AppUser> getAllUser();
    
    AppUser loadUserByUsername(String username);
}
