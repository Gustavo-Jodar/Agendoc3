package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.daoCliente;
import br.ufscar.dc.dsw.dao.daoProfissional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Profissional;
import br.ufscar.dc.dsw.domain.User;
import br.ufscar.dc.dsw.util.Formata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admins")
public class adminController {

    @Autowired
    daoCliente daoCliente;

    @Autowired
    daoProfissional daoProfissional;

    @GetMapping("/")
    public String index(Model model) {
        return "/admin/index.html";
    }

    @GetMapping("showPaginaAdmin")
    public String showPaginaAdmin(Model model) {
        return "/admin/index.html";
    }

    @GetMapping("/listaClientes")
    public String listaClientes(ModelMap model) {
        List<Cliente> listaClientes = new ArrayList<>();
        listaClientes = daoCliente.findAll();
        model.addAttribute("listaClientes", listaClientes);

        return "/admin/listaClientes.html";
    }

    @GetMapping("/listaProfissionais")
    public String listaProfissionais(Model model) {
        List<Profissional> lista_profissionais = daoProfissional.findAll();

        model.addAttribute("listaProfissionais", lista_profissionais);
        return "/admin/listaProfissionais.html";
    }

    @GetMapping("/apresentaAdicionarCliente")
    public String apresentaAdicionarCliente(Model model) {
        return "/admin/addCliente.html";
    }

    @GetMapping("/apresentaEdicaoCliente")
    public String apresentaEdicaoCliente(Model model, @RequestParam("email") String email) throws ParseException {
        Cliente cliente = daoCliente.findByEmail(email);

        model.addAttribute("clienteEdit", cliente);

        return "/admin/editCliente.html";
    }

    @PostMapping("/editaCliente")
    public String editarCliente(Model model, Cliente cliente, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento) throws ParseException {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/admins/listaClientes";
        }

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

        cliente.setNascimento(nascimento);
        cliente.setPapel("CLIENTE");

        daoCliente.save(cliente);

        return "redirect:/admins/listaClientes";
    }

    @GetMapping("/removerCliente")
    public String removerCliente(Model model, @RequestParam("cpf") String cpf) {

        daoCliente.delete(daoCliente.findByCpf(cpf));

        return "redirect:/admins/listaClientes";
    }

    @GetMapping("/apresentaAdicionarProfissional")
    public String apresentaAdicionarProfissional(Model model) {
        return "/admin/addProfissional.html";
    }

    @PostMapping("/editaProfissional")
    public String editarProfissional(Model model, Profissional profissional, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento) throws ParseException {

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/admins/listaProfissionais";
        }

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

        profissional.setNascimento(nascimento);
        profissional.setPapel("PROFISSIONAL");

        daoProfissional.save(profissional);

        return "redirect:/admins/listaProfissionais";
    }

    @GetMapping("/removerProfissional")
    public String removerProfissional(Model model, @RequestParam("cpf") String cpf) {

        daoProfissional.delete(daoProfissional.findByCpf(cpf));

        return "redirect:/admins/listaProfissionais";
    }

    @GetMapping("/apresentaEdicaoProfissional")
    public String apresentaEdicaoProfissional(Model model, @RequestParam("email") String email) {
        Profissional profissional = daoProfissional.findByEmail(email);

        model.addAttribute("profissionalEdit", profissional);
        return "/admin/editProfissional.html";
    }

    @PostMapping("/saveProfissional")
    public String salvar(Model model, Profissional profissional, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento) throws ParseException {
        if (result.hasErrors()) {
            return "redirect:/admins/apresentaAdicionarProfissional";
        }

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

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
        profissional.setNascimento(nascimento);
        profissional.setPapel("PROFISSIONAL");

        daoProfissional.save(profissional);

        return "redirect:/admins/listaProfissionais";
    }

    @PostMapping("/saveCliente")
    public String salvar(Model model, Cliente cliente, BindingResult result,
            @RequestParam("nascimento") String startDateStrNascimento) throws ParseException {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "redirect:/apresentaAdicionarCliente";
        }

        startDateStrNascimento = startDateStrNascimento.replace('/', '-');
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nascimento = sdf.parse(startDateStrNascimento);

        cliente.setNascimento(nascimento);
        cliente.setPapel("CLIENTE");

        daoCliente.save(cliente);

        return "redirect:/admins/listaClientes";
    }
}
