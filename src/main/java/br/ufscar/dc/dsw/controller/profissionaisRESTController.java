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
import br.ufscar.dc.dsw.domain.Profissional;

@RestController
public class profissionaisRESTController {
    @Autowired
    daoProfissional daoProfissional;

    @Autowired
    daoConsulta daoConsulta;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void parse(Profissional profissional, JSONObject json) throws ParseException {

        Map<String, Object> map = (Map<String, Object>) json.get("profissional");

        Object cpf = map.get("cpf");
        if (cpf instanceof String) {
            profissional.setCpf(cpf.toString());
        } else {
            profissional.setCpf(cpf.toString());
        }
        profissional.setEmail((String) map.get("email"));
        String str_nascimento = map.get("nascimento").toString().replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(str_nascimento);
        profissional.setNascimento(nascimento);
        profissional.setNome((String) map.get("nome"));
        profissional.setPapel((String) map.get("papel"));
        profissional.setSenha((String) map.get("senha"));
        profissional.setArea((String) map.get("area"));
        profissional.setBio((String) map.get("bio"));
    }

    @GetMapping(path = "/profissionais")
    public ResponseEntity<List<Profissional>> lista() {
        List<Profissional> lista = daoProfissional.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/profissionais/{cpf}")
    public ResponseEntity<Profissional> lista(@PathVariable("cpf") String cpf) {
        Profissional profissional = daoProfissional.findByCpf(cpf);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profissional);
    }

    @PostMapping(path = "/profissionais")
    @ResponseBody
    public ResponseEntity<Profissional> cria(@RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Profissional profissional = new Profissional();
                parse(profissional, json);
                daoProfissional.save(profissional);
                return ResponseEntity.ok(profissional);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @GetMapping(path = "/profissionais/especialidade/{nome}")
    public ResponseEntity<List<Profissional>> getByEspecialidade(@PathVariable("nome") String nome,
            @RequestBody JSONObject json) {
        List<Profissional> lista = daoProfissional.getWithFilter_e(nome);
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @RequestMapping(path = "/profissionais/especialidades/{}", produces = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Profissional> atualiza(@PathVariable("cpf") String cpf, @RequestBody JSONObject json) {
        try {
            if (isJSONValid(json.toString())) {
                Profissional profissional = daoProfissional.findByCpf(cpf);
                if (profissional == null) {
                    return ResponseEntity.notFound().build();
                } else {
                    parse(profissional, json);
                    daoProfissional.save(profissional);
                    return ResponseEntity.ok(profissional);
                }
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    @RequestMapping(path = "/profissionais/{cpf}", produces = "application/json", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Boolean> remove(@PathVariable("cpf") String cpf) {
        Profissional profissional = daoProfissional.findByCpf(cpf);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        } else {
            daoProfissional.delete(profissional);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
    }

}
