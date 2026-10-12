package com.laboratorio1.laboratorio1;

import com.laboratorio1.laboratorio1.model.Role;
import com.laboratorio1.laboratorio1.model.User;
import com.laboratorio1.laboratorio1.repository.RoleRepository;
import com.laboratorio1.laboratorio1.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args){
        Map<String, Role> roles = roleRepository
                .findAllByNameIn(Set.of("SUPER-ADMIN-ROLE", "USER"))
                .stream()
                .collect(Collectors.toMap(Role::getName, role -> role));

        Role adminRole = roles.get("SUPER-ADMIN-ROLE");
        Role userRole = roles.get("USER");

        if(adminRole == null || userRole == null){
            throw new IllegalStateException(
                    "Roles requeridos no encontrados en la base de datos"
            );
        }

        createUserIfAbsent(
                "admin",
                "admin@laboratorio.local",
                "Admin",
                "User",
                "admin",
                adminRole
        );

        createUserIfAbsent(
                "user",
                "user@laboratorio.local",
                "Regular",
                "User",
                "user",
                userRole
        );
    }

    private void createUserIfAbsent(
            String username,
            String email,
            String firstName,
            String lastName,
            String password,
            Role role){
        if(userRepository.findByUsername(username).isPresent()){
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setPassword(passwordEncoder.encode((password)));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }
}