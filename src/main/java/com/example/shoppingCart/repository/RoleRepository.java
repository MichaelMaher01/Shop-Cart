package com.example.shoppingCart.repository;

import com.example.shoppingCart.model.Role;
import com.example.shoppingCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional findByName(String role);

//    void save(Role role);
}
