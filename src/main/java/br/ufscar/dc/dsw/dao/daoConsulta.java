package br.ufscar.dc.dsw.dao;

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Consulta;

@SuppressWarnings("unchecked")
@Transactional
public interface daoConsulta extends CrudRepository<Consulta, String> {

        @Override
        @Query("SELECT c FROM Consulta c")
        List<Consulta> findAll();

        @Query("SELECT c FROM Consulta c")
        List<Consulta> getAll();

        @Query("SELECT c FROM Consulta c WHERE c.cpf_profissional = :cpf_profissional")
        List<Consulta> get_by_cpf_profissional(@Param("cpf_profissional") String cpf_profissional);

        @Query("SELECT c FROM Consulta c WHERE c.cpf_cliente = :cpf_cliente")
        List<Consulta> get_by_cpf_cliente(@Param("cpf_cliente") String cpf_cliente);

        @Query("SELECT c FROM Consulta c WHERE c.cpf_cliente = :cpf_cliente AND c.data_consulta=:data_consulta")
        List<Consulta> get_by_cpf_cliente_data_consulta(@Param("cpf_cliente") String cpf_cliente,
                        @Param("data_consulta") Date data_consulta);

        @Query("SELECT c FROM Consulta c WHERE c.cpf_profissional = :cpf_profissional AND c.data_consulta=:data_consulta")
        List<Consulta> get_by_cpf_profissional_data_consulta(@Param("cpf_profissional") String cpf_profissional,
                        @Param("data_consulta") Date data_consulta);

        void delete(Consulta consulta);

        Consulta findById(Integer id);

        @Modifying
        @Query("UPDATE Consulta SET link_meet = :link_meet WHERE id =:id")
        void setLink(@Param("link_meet") String link_meet, @Param("id") Integer id);

        Consulta save(Consulta consulta);

}