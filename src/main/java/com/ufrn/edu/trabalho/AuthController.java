package com.ufrn.edu.trabalho;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Controller
public class AuthController {

    @RequestMapping(value = "/logar", method = RequestMethod.GET)
    public String exibeFormularioLogar() {
        return "index.html";
    }

    @RequestMapping(value = "/logar", method = RequestMethod.POST)
    public void doLogar(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException, ServletException {
        String email = request.getParameter("username");
        String senha = request.getParameter("senha");
        System.out.println(email);
        System.out.println(senha);
        Conexao con = new Conexao();
        UsuarioDAO dao = new UsuarioDAO(con.getConexao());
        Usuario usuario = dao.usuarioCadastrado(email, senha);
        System.out.println(usuario.getEmail());
        System.out.println(usuario.getSenha());
        if(usuario != null){
            System.out.println(usuario.getEmail());
            System.out.println(usuario.getSenha());
            HttpSession session = request.getSession();
            session.setAttribute("logado", true);
            session.setAttribute("email", email);
            System.out.println(session);
            if(usuario.getTipoUsuario() == "cliente"){
                request.getRequestDispatcher("/").forward(request, response);
            }else{
                request.getRequestDispatcher("/pageLoja").forward(request, response);
            }
        } else {
            response.sendRedirect("index.html?msg=Login Falhou");
        }
    }

    @RequestMapping(value = "/logout")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate(); // Invalida a sessão do usuário
        response.sendRedirect("/"); // Redireciona para a página principal
    }
}
