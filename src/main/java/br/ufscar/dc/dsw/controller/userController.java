package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.daoCliente;
import br.ufscar.dc.dsw.dao.daoProfissional;
import br.ufscar.dc.dsw.dao.daoUser;

import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.security.UsuarioDetailsServiceImpl;

import java.util.Date;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.util.Formata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/users")
public class userController {

    @Autowired
    daoCliente daoCliente;

    @Autowired
    daoProfissional daoProfissional;

    @Autowired
    daoUser daoUser;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/retornaIndex")
    public String retornaIndex(Model model) {
        return "index.html";
    }

    @GetMapping("/showProfissionais")
    public String showProfissionais(Model model, @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "especialidade", required = false) String especialidade) {

        List<Profissional> lista_profissionais = new ArrayList<>();

        if ((area == null && especialidade == null) || (area == "" && especialidade == "")) {
            lista_profissionais = daoProfissional.findAll();
        } else {
            Formata f = new Formata();
            especialidade = f.formataString(especialidade);
            area = f.formataString(area);
            if (especialidade == null || especialidade == "") {
                lista_profissionais = daoProfissional.getWithFilter_a(area);
            } else if (area == null || area == "") {
                lista_profissionais = daoProfissional.getWithFilter_e(especialidade);
            } else {
                lista_profissionais = daoProfissional.getWithFilter_a_e(especialidade, area);
            }
        }
        model.addAttribute("listaProfissionais", lista_profissionais);
        return "seeProf.html";
    }

    @GetMapping("/showTypes")
    public String showTypes(Model model) {
        return "/user/userType.html";
    }

    @GetMapping("showCadastroCliente")
    public String showCadastroCliente(Model model) {
        return "/cliente/cadastro.html";
    }

    @PostMapping("/salvarCliente")
    public String salvar(Model model, Cliente cliente, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento) throws ParseException {
        if (result.hasErrors()) {
            return "redirect:/showCadastro";
        }

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

        cliente.setNascimento(nascimento);
        cliente.setPapel("CLIENTE");

        daoCliente.save(cliente);

        return "redirect:/users/showLogin";
    }

    @PostMapping("/salvarProfissional")
    public String salvar(Model model, Profissional profissional, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento,
            @RequestParam("file") MultipartFile file) throws ParseException {
        String fileName = file.getOriginalFilename();

        if (result.hasErrors()) {
            return "redirect:/users/showCadastroProfissional";
        }

        try {
            file.transferTo(
                    new File("/home/gustavo/Documentos/facul/WEB1/T2/Agendoc2/src/main/resources/uploads/" + fileName));
        } catch (Exception e) {
            return "redirect:users/salvarProfissional";
        }

        String area = profissional.getArea();
        if (area.substring(0, 1).equals("1"))
            area = "MEDICINA";
        if (area.substring(0, 1).equals("2"))
            area = "ADVOCACIA";
        if (area.substring(0, 1).equals("3"))
            area = "PSICOLOGIA";
        if (area.substring(0, 1).equals("4"))
            area = "EDUCACAO";
        if (area.substring(0, 1).equals("5"))
            area = "NUTRICAO";
        if (area.substring(0, 1).equals("6"))
            area = "TERAPIA";
        profissional.setArea(area);

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

        profissional.setNascimento(nascimento);
        profissional.setPapel("PROFISSIONAL");

        daoProfissional.save(profissional);

        return "redirect:/users/showLogin";
    }

    @GetMapping("showCadastroProfissional")
    public String showCadastroProfissional(Model model) {
        return "/profissional/cadastro.html";
    }

    @GetMapping("/showLogin")
    public String apresentaFormLogin(Model model) {
        return "/user/login.html";
    }
}
