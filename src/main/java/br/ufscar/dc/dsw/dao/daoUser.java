package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.User;

@SuppressWarnings("unchecked")
public interface daoUser extends CrudRepository<User, String> {
    User findByEmail(String email);

    void deleteByCpf(String cpf);

    @Query("SELECT u FROM User u WHERE u.email = :username")
    public User getUserByUsername(@Param("username") String username);

}
