package com.ufrn.edu.trabalho;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClienteController {

    @RequestMapping(value = "/registrar", method = RequestMethod.GET)
    public String exibirFormularioRegistro() {
        return "cadastroUsuario.html";
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public void cadastrarCliente(HttpServletRequest request, HttpServletResponse response){

    }

}
