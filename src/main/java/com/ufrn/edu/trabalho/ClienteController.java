package com.ufrn.edu.trabalho;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URISyntaxException;
import java.sql.SQLException;

@Controller
public class ClienteController {

    @RequestMapping(value = "/registrar", method = RequestMethod.GET)
    public String exibirFormularioRegistro() {
        return "cadastroUsuario.html";
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public void cadastrarCliente(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException {
        var nome = request.getParameter("nome");
        System.out.println(request.getParameter("nome"));
        var sobrenome = request.getParameter("sobrenome");
        System.out.println(request.getParameter("sobrenome"));
        var mail = request.getParameter("email");
        var senha = request.getParameter("senha");
        var cliente = new Usuario(nome, sobrenome, mail, senha);
        Conexao conexao = new Conexao();
        var clienteDao = new UsuarioDAO(conexao.getConexao());
        clienteDao.inserirUsuario(cliente);
    }

}
