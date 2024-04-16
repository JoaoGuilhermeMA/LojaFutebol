package com.ufrn.edu.trabalho;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ClienteController {

    @RequestMapping(value = "/registrar", method = RequestMethod.GET)
    public String exibirFormularioRegistro() {
        return "cadastroUsuario.html";
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public void cadastrarCliente(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException {
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
        response.sendRedirect("/logar");
    }

    @RequestMapping(value = "/lojaFutebol", method =  RequestMethod.GET)
    public void mostraProdutos(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException  {
        HttpSession session = request.getSession(false);
        if (session != null){
            String role = (String) session.getAttribute("role");
            if (!role.equals("cliente")){
                response.sendRedirect("index.html?msg=voce nao pode acessar a pagina");
            }
        }else {
            response.sendRedirect("index.html?msg=voce nao pode acessar a pagina");
        }

        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        BufferedReader htmlReader = new BufferedReader(new FileReader("src/main/resources/static/pageAdmin.html"));
        BufferedWriter htmlWriter = new BufferedWriter(writer);

        String line;
        while ((line = htmlReader.readLine()) != null) {
            htmlWriter.write(line + "\n");
        }

        Conexao conexao = new Conexao();
        var dao = new ProdutoDAO(conexao.getConexao());

        List<Produto> produtos = dao.listarProdutos();
        if (produtos != null) {
            htmlWriter.write("<ul>");
            for (Produto produto : produtos) {
                htmlWriter.write("<li>"+ produto.getNome_produto() + "<br>" + produto.getTipo_produto()+"<br>" + produto.getPreco()+"<br>" + produto.getDescricao()+"<br>");
                htmlWriter.write("<form action='/adicionaCarrinho' method='post'>");
                htmlWriter.write("<input type='hidden' name='id' value='" + produto.getId_produto() + "'>");
                htmlWriter.write("<button type='submit'>Adicionar ao Carrinho</button>");
                htmlWriter.write("</form>");
                htmlWriter.write("</li>");
                htmlWriter.write("<br>");
            }
            htmlWriter.write("</ul>");
        }


        htmlWriter.write("<a href='/seuCarrinho'><button>Ver carrinho</button></a>");
        htmlWriter.write("<br><br><a href='/logout'>Logout</a>");
        htmlReader.close();
        htmlWriter.close();
    }

    @RequestMapping(value = "/adicionaCarrinho", method = RequestMethod.POST)
    public void itemNoCarrinho(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException{
        String idProd = request.getParameter("id"); // id do elemento que foi clicado para adicionar ao carrinho

        Cookie[] cookies = request.getCookies();
        Cookie carrinhoCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("carrinho")) {
                    carrinhoCookie = cookie;
                    break;
                }
            }
        }

        // Se não houver um cookie existente para o carrinho, criar um novo cookie
        if (carrinhoCookie == null) {
            carrinhoCookie = new Cookie("carrinho", idProd);
        } else {
            // Se já houver um cookie para o carrinho, adicionar o ID do produto ao valor do cookie
            String valorCookie = carrinhoCookie.getValue();
            valorCookie += "-" + idProd;
            carrinhoCookie.setValue(valorCookie);
        }


        // Definir a duração do cookie (opcional)
        carrinhoCookie.setMaxAge(48 * 60 * 60); // Define a duração do cookie para 24 horas (em segundos)
        // Adicionar o cookie à resposta
        response.addCookie(carrinhoCookie);



        response.sendRedirect("/lojaFutebol");
    }

    @RequestMapping(value = "/seuCarrinho", method = RequestMethod.GET)
    public void carrinho(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException{
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        BufferedReader htmlReader = new BufferedReader(new FileReader("src/main/resources/static/carrinho.html"));
        BufferedWriter htmlWriter = new BufferedWriter(writer);

        String line;
        while ((line = htmlReader.readLine()) != null) {
            htmlWriter.write(line + "\n");
        }

        htmlWriter.write("<br <br>>Seu carrinho");

        htmlReader.close();
        htmlWriter.close();
    }
}
