package com.laboratorio1.laboratorio1.security;

import com.laboratorio1.laboratorio1.model.Role;
import com.laboratorio1.laboratorio1.repository.RoleRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Traduce el JWT de Keycloak a un Authentication de Spring Security.
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String clientId;
    private final RoleRepository roleRepository;

    public KeycloakJwtConverter(String clientId, RoleRepository roleRepository)
    {
        this.clientId = clientId;
        this.roleRepository = roleRepository;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<String> tokenRoles = roles(jwt);

        Set<String> registeredRoles = roleRepository
                .findAllByNameIn(tokenRoles)
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Collection<GrantedAuthority> authorities = Stream.concat(
                tokenRoles.stream()
                        .filter(registeredRoles::contains)
                        .map(role -> "ROLE_" + role),
                permisos(jwt).stream()
        ).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("preferred_username"));
    }

    private List<String> roles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        return extraerRoles(realmAccess);
    }

    @SuppressWarnings("unchecked")
    private List<String> permisos(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null) {
            return List.of();
        }
        return extraerRoles((Map<String, Object>) resourceAccess.get(clientId));
    }

    @SuppressWarnings("unchecked")
    private List<String> extraerRoles(Map<String, Object> claim) {
        if (claim == null || !(claim.get("roles") instanceof List<?> roles)) {
            return List.of();
        }
        return (List<String>) roles;
    }
}