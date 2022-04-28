package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.daoCliente;
import br.ufscar.dc.dsw.dao.daoUser;
import br.ufscar.dc.dsw.domain.User;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.security.UsuarioDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetailsService;

@Controller
@RequestMapping("/aux")
public class auxController {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    daoUser daoUser;

    @GetMapping("/getRole")
    public String login(Model model, Principal principal) {
        // @RequestParam("email") String email, @RequestParam("senha") String senha
        // User user = daoUser.findByEmail(email);

        User user = daoUser.getUserByUsername(principal.getName());

        String papel = user.getRole();

        if (papel.replaceAll("\\P{L}+", "").equals("ADMIN"))
            return "redirect:/admins";
        if (papel.replaceAll("\\P{L}+", "").equals("CLIENTE"))
            return "redirect:/clientes/showIndex";
        if (papel.replaceAll("\\P{L}+", "").equals("PROFISSIONAL"))
            return "redirect:/profissionais/showIndex";

        return "ue";
    }

}
