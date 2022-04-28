package br.ufscar.dc.dsw.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "consultas")
public class Consulta implements Serializable {

    @Id
    private Integer id;

    @Column(name = "cpf_profissional", length = 16, nullable = false)
    private String cpf_profissional;

    @Column(name = "cpf_cliente", length = 16, nullable = false)
    private String cpf_cliente;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data_consulta;

    @Column(nullable = false)
    private int horario;

    @Column(length = 150)
    private String link_meet;

    @Column(length = 60, nullable = false)
    private String nome_profissional;

    @Column(length = 60, nullable = false)
    private String nome_cliente;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpf_cliente() {
        return cpf_cliente;
    }

    public String getCpf_profissional() {
        return cpf_profissional;
    }

    public Date getData_consulta() {
        return data_consulta;
    }

    public int getHorario() {
        return horario;
    }

    public String getLink_meet() {
        return link_meet;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public String getNome_profissional() {
        return nome_profissional;
    }

    public void setCpf_cliente(String cpf_cliente) {
        this.cpf_cliente = cpf_cliente;
    }

    public void setCpf_profissional(String cpf_profissional) {
        this.cpf_profissional = cpf_profissional;
    }

    public void setData_consulta(Date data_consulta) {
        this.data_consulta = data_consulta;
    }

    public void setHorario(int horario) {
        this.horario = horario;
    }

    public void setLink_meet(String link_meet) {
        this.link_meet = link_meet;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public void setNome_profissional(String nome_profissional) {
        this.nome_profissional = nome_profissional;
    }

}
