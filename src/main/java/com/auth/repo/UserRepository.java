/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.repo;

import com.auth.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author riyaro
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
}
