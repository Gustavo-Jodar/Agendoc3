package br.ufscar.dc.dsw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.ufscar.dc.dsw.dao.daoUser;
import br.ufscar.dc.dsw.domain.User;

public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private daoUser dao;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = dao.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new UsuarioDetails(user);
    }

}