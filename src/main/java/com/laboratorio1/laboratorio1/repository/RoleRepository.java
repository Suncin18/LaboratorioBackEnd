package com.laboratorio1.laboratorio1.repository;

import com.laboratorio1.laboratorio1.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByNameIn(Collection<String> names);
}