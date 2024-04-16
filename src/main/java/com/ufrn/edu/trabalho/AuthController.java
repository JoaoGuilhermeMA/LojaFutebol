package com.ufrn.edu.trabalho;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        Conexao con = new Conexao();
        UsuarioDAO dao = new UsuarioDAO(con.getConexao());
        Usuario usuario = dao.usuarioCadastrado(email, senha);
        if(usuario != null){
            HttpSession session = request.getSession();
            session.setAttribute("logado", true);
            session.setAttribute("email", email);
            session.setAttribute("role", usuario.getTipoUsuario());
            if(usuario.getEmail().equals("admin@gmail.com")){
                response.sendRedirect("/pageLoja");
            }else{
                response.sendRedirect("/lojaFutebol");
            }
        } else {
            response.sendRedirect("index.html?msg=Login Falhou");
        }
    }

    @RequestMapping(value = "/logout")
    public void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect("/logar");
    }
}
