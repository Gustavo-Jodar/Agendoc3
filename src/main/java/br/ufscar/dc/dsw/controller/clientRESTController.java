package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import br.ufscar.dc.dsw.domain.Cliente;

@RestController
public class clientRESTController {
    @Autowired
    daoCliente daoCliente;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void parse(Cliente cliente, JSONObject json) throws ParseException {

        Map<String, Object> map = (Map<String, Object>) json.get("cliente");
        System.out.println("5");
        Object cpf = map.get("cpf");
        if (cpf instanceof String) {
            cliente.setCpf(cpf.toString());
        } else {
            cliente.setCpf(cpf.toString());
        }
        cliente.setEmail((String) map.get("email"));
        String str_nascimento = map.get("nascimento").toString().replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(str_nascimento);
        cliente.setNascimento(nascimento);
        cliente.setNome((String) map.get("nome"));
        cliente.setPapel((String) map.get("papel"));
        cliente.setSenha((String) map.get("senha"));
        cliente.setSexo((String) map.get("sexo"));
        cliente.setTelefone((String) map.get("telefone"));
    }

    @GetMapping(path = "/clientes")
    public ResponseEntity<List<Cliente>> lista() {
        List<Cliente> lista = daoCliente.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/clientes/{cpf}")
    public ResponseEntity<Cliente> lista(@PathVariable("cpf") String cpf) {
        Cliente cliente = daoCliente.findByCpf(cpf);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PostMapping(path = "/clientes")
    @ResponseBody
    public ResponseEntity<Cliente> cria(@RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Cliente cliente = new Cliente();
                parse(cliente, json);
                daoCliente.save(cliente);
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @RequestMapping(path = "/clientes/{cpf}", produces = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Cliente> atualiza(@PathVariable("cpf") String cpf, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Cliente cliente = daoCliente.findByCpf(cpf);
                if (cliente == null) {
                    return ResponseEntity.notFound().build();
                } else {
                    parse(cliente, json);
                    daoCliente.save(cliente);
                    return ResponseEntity.ok(cliente);
                }
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @RequestMapping(path = "/clientes/{cpf}", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Boolean> remove(@PathVariable("cpf") String cpf) {
        Cliente cliente = daoCliente.findByCpf(cpf);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        } else {
            daoCliente.delete(cliente);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }

}
