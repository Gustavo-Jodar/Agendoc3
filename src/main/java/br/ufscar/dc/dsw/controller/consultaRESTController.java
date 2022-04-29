package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.ufscar.dc.dsw.dao.*;
import br.ufscar.dc.dsw.domain.*;

@RestController
public class consultaRESTController {
    @Autowired
    daoCliente daoCliente;

    @Autowired
    daoConsulta daoConsulta;

    @Autowired
    daoProfissional daoProfissional;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @GetMapping(path = "/consultas")
    public ResponseEntity<List<Consulta>> lista() {
        List<Consulta> lista = new ArrayList<>();
        lista = daoConsulta.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/consultas/{id}")
    public ResponseEntity<Consulta> lista(@PathVariable("id") Integer id) {
        Consulta consulta = daoConsulta.findById(id);
        if (consulta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(consulta);
    }

    @GetMapping(path = "/consultas/clientes/{cpf_cliente}")
    public ResponseEntity<List<Consulta>> listaConsultasClientes(@PathVariable("cpf_cliente") String cpf_cliente) {
        List<Consulta> consultas = daoConsulta.get_by_cpf_cliente(cpf_cliente.replaceAll("\\s+", ""));
        if (consultas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(consultas);
    }

    @GetMapping(path = "/consultas/profissionais/{cpf_profissional}")
    public ResponseEntity<List<Consulta>> listaConsultasProfissionais(
            @PathVariable("cpf_profissional") String cpf_profissional) {
        List<Consulta> consultas = daoConsulta.get_by_cpf_profissional(cpf_profissional.replaceAll("\\s+", ""));
        if (consultas == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(consultas);
    }

}
