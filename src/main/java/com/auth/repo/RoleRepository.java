/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.repo;

import com.auth.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author riyaro
 */
public interface RoleRepository extends JpaRepository<AppRole, Long> {

    AppRole findByRoleName(String roleName);
}
