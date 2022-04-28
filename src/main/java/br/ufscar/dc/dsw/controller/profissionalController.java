package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.daoProfissional;
import br.ufscar.dc.dsw.dao.daoCliente;
import br.ufscar.dc.dsw.dao.daoConsulta;
import br.ufscar.dc.dsw.dao.daoUser;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.domain.Consulta;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.util.Formata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profissionais")
public class profissionalController {

    @Autowired
    daoProfissional daoProfissional;
    @Autowired
    daoConsulta daoConsulta;

    @Autowired
    daoUser daoUser;

    @GetMapping("/showIndex")
    public String index(Model model, Principal principal) {
        User user = daoUser.getUserByUsername(principal.getName());

        String cpf_profissional = user.getCpf();
        Profissional profissional = daoProfissional.findByCpf(cpf_profissional);

        daoConsulta.get_by_cpf_profissional(cpf_profissional.replaceAll("\\P{L}+", ""));
        List<Consulta> consultas = daoConsulta.findAll();

        model.addAttribute("consultas", consultas);
        model.addAttribute("usuarioLogado", profissional);

        return "profissional/index";
    }

    @GetMapping("/apresentaConsulta")
    public String apresentaConsulta(Model model, Principal principal,
            @RequestParam("id") Integer id) {

        Consulta consulta = daoConsulta.findById(id);

        model.addAttribute("consulta", consulta);

        return "profissional/editAppointment.html";
    }

    @GetMapping("/cancelaConsulta")
    public String cancelarConsulta(Model model, @RequestParam("id") Integer id) {
        Consulta consulta = daoConsulta.findById(id);
        daoConsulta.delete(consulta);

        return "redirect:showIndex";
    }

    @GetMapping("/edit_link")
    public String editarLink(Model model, @RequestParam("link_meet") String link_meet,
            @RequestParam("id") Integer id) {

        daoConsulta.setLink(link_meet, id);

        return "redirect:showIndex";
    }

}
