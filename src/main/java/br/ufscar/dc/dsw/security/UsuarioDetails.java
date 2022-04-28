package br.ufscar.dc.dsw.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.domain.User;

@SuppressWarnings("serial")
public class UsuarioDetails implements UserDetails {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    public UsuarioDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                "ROLE_" + user.getRole().replaceAll("\\P{L}+", ""));

        return Arrays.asList(authority);
    }

    @Override
    public String getPassword() {
        // achava que o negócio tava decodificando uma coisa nao codificada por isso
        // codifiquei aqui
        passwordEncoder = encoder();
        return passwordEncoder.encode(user.getSenha());
        // return user.getSenha();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUsuario() {
        return user;
    }
}