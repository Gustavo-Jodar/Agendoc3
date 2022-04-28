package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.daoCliente;
import br.ufscar.dc.dsw.dao.daoConsulta;
import br.ufscar.dc.dsw.dao.daoProfissional;
import br.ufscar.dc.dsw.dao.daoUser;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Consulta;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.util.Formata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/clientes")
public class clienteController {

    @Autowired
    daoProfissional daoProfissional;

    @Autowired
    daoCliente daoCliente;

    @Autowired
    daoUser daoUser;

    @Autowired
    daoConsulta daoConsulta;

    @Autowired
    JavaMailSender mailSender;

    private boolean isJSONValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @GetMapping("/showIndex")
    public String index(Model model, Principal principal) {
        User user = daoUser.getUserByUsername(principal.getName());

        String cpf_cliente = user.getCpf();
        Cliente cliente = daoCliente.findByCpf(cpf_cliente.replaceAll("\\s+", ""));

        List<Consulta> consultas = daoConsulta.get_by_cpf_cliente(cpf_cliente.replaceAll("\\s+", ""));

        model.addAttribute("usuarioLogado", cliente);
        model.addAttribute("consultas", consultas);

        return "cliente/index";
    }

    @GetMapping("/apresentaConsulta")
    public String apresentaConsulta(Model model, Principal principal,
            @RequestParam("id") Integer id) {

        Consulta consulta = daoConsulta.findById(id);

        Profissional profissionalEscolhido = daoProfissional
                .findByCpf(consulta.getCpf_profissional().replaceAll("\\s+", ""));

        model.addAttribute("consulta", consulta);
        model.addAttribute("profissionalEscolhido", profissionalEscolhido);

        return "cliente/editAppointment.html";
    }

    @GetMapping("/edit_link")
    public String editarLink(Model model, @RequestParam("link_meet") String link_meet,
            @RequestParam("id") Integer id) {

        daoConsulta.setLink(link_meet, id);

        return "redirect:showIndex";
    }

    @GetMapping("/cancelaConsulta")
    public String cancelarConsulta(Model model, @RequestParam("id") Integer id) {
        Consulta consulta = daoConsulta.findById(id);
        daoConsulta.delete(consulta);

        return "redirect:showIndex";
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
        return "/cliente/seeProf.html";
    }

    @GetMapping("/apresentaMarcarConsulta1")
    public String apresentaMarcarConsulta1(Model model, @RequestParam("cpf_profissional") String cpf_profissional,
            @RequestParam(value = "invalidDate", required = false) String invDate) {

        if (invDate != null) {
            model.addAttribute("invDate", true);
        }

        Profissional profissionalEscolhido = daoProfissional
                .findByCpf(cpf_profissional.replaceAll("\\s+", ""));

        model.addAttribute("profissionalEscolhido", profissionalEscolhido);

        return "/cliente/appointment.html";
    }

    @PostMapping("/apresentaMarcarConsulta2")
    public String apresentaMarcarConsulta1(Model model, Principal principal,
            @RequestParam("cpf_profissional") String cpf_profissional,
            @RequestParam("data_consulta") String str_data_consulta) throws ParseException {

        model.addAttribute("data_nao_formatada", str_data_consulta);

        User user = daoUser.getUserByUsername(principal.getName());
        String cpf_cliente = user.getCpf().replaceAll("\\s+", "");
        model.addAttribute("cliente", daoCliente.findByCpf(cpf_cliente));

        Profissional profissionalEscolhido = daoProfissional
                .findByCpf(cpf_profissional.replaceAll("\\s+", ""));
        model.addAttribute("profissionalEscolhido", profissionalEscolhido);

        str_data_consulta = str_data_consulta.replace('/', '-');
        Date data_consulta = Date.valueOf(str_data_consulta);

        List<Consulta> consultas_marcadas_cliente = daoConsulta.get_by_cpf_cliente_data_consulta(cpf_cliente,
                data_consulta);
        List<Consulta> consultas_marcadas_profissional = daoConsulta.get_by_cpf_profissional_data_consulta(
                cpf_profissional,
                data_consulta);

        if (data_consulta.before(profissionalEscolhido.get_today()) ||
                data_consulta.equals(profissionalEscolhido.get_today())) {
            return "redirect:/clientes/apresentaMarcarConsulta1?cpf_profissional="
                    + cpf_profissional.replaceAll("\\s+", "") + "&invalidDate=true";

        }

        List<Consulta> consultasNoMesmoDia = new ArrayList<>();
        consultasNoMesmoDia.addAll(consultas_marcadas_cliente);
        consultasNoMesmoDia.addAll(consultas_marcadas_profissional);

        List<Integer> horariosDisponiveis = new ArrayList<>();
        for (int i = 8; i <= 17; i++) {
            boolean horarioLivre = true;
            for (Consulta c : consultasNoMesmoDia) {
                if (c.getHorario() == i) {
                    horarioLivre = false;
                    break;
                }
            }
            if (horarioLivre == true) {
                horariosDisponiveis.add(i);
            }
        }

        model.addAttribute("horariosLivres", horariosDisponiveis);
        model.addAttribute("consulta_aux", true);

        return "/cliente/appointment.html";
    }

    @PostMapping("/cadastraConsulta")
    public String cadastraConsulta(Model model, Consulta consulta, Principal principal,
            @Param("horario") Integer horario) {
        List<Consulta> todas_consultas = daoConsulta.findAll();
        Integer maior = 0;
        if (todas_consultas.size() != 0) {
            maior = todas_consultas.get(0).getId();
            for (Consulta c : todas_consultas) {
                if (c.getId() > maior) {
                    maior = c.getId();
                }
            }
        }
        consulta.setCpf_cliente(consulta.getCpf_cliente().replaceAll("\\s+", ""));
        consulta.setCpf_profissional(consulta.getCpf_profissional().replaceAll("\\s+", ""));

        consulta.setId(maior + 1);
        daoConsulta.save(consulta);

        SimpleMailMessage message = new SimpleMailMessage();
        String emailProfissional = daoProfissional.findByCpf(consulta.getCpf_profissional()).getEmail();
        System.out.println(emailProfissional);
        String emailCliente = principal.getName();
        // String toUser = emailProfissional + ", " + emailCliente;
        message.setTo(emailProfissional, emailCliente);

        message.setSubject("Sua consulta do agendoc foi marcada!");
        message.setText("Olá " + consulta.getNome_cliente() + "\n"
                + "Seguem as informacoes a respeito de sua consulta: \n Profissional: "
                + consulta.getNome_profissional() + "\n Data e hora de sua consulta: "
                + consulta.getData_consulta() + " " + consulta.getHorario() + "h00min"
                + "\n Na data e hora da consulta entre neste link:"
                + consulta.getLink_meet() + "\n \n Qualquer coisa estamos aqui! \n Atenciosamente Agendoc.");

        mailSender.send(message);

        return "redirect:/clientes/showIndex";

    }

}