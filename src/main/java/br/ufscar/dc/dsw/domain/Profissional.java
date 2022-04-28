package br.ufscar.dc.dsw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import java.util.Date;

//objeto do tipo profissional que herda atributos do tipo user
@Entity
@Table(name = "profissionais")
public class Profissional extends User {

    @Column(nullable = false, length = 500)
    private String bio;

    @Column(nullable = false, length = 50)
    private String area;

    @Column(nullable = false, length = 100)
    private String especialidade;

    @Column(length = 50)
    private String curriculo;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(String curriculo) {
        this.curriculo = curriculo;
    }

    public Date get_today() {
        return new Date();
    }
}
