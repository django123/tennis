package org.django.tennis.security;

import org.django.tennis.entities.RoleEntity;
import org.django.tennis.entities.UserEntity;
import org.django.tennis.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DymaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DymaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findOneWithRolesByLoginIgnoreCase(login)
                .map(this::createSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " could not be found."));
    }

    private org.springframework.security.core.userdetails.User createSecurityUser(UserEntity user) {
        List<SimpleGrantedAuthority> grantedRoles = user
                .getRoles()
                .stream()
                .map(RoleEntity::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedRoles);
    }
}
