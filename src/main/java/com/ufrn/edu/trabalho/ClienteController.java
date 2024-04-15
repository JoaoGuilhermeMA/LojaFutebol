package com.ufrn.edu.trabalho;

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
                htmlWriter.write("<a href='/adicionaCarrinho?id='"+produto.getId_produto()+"><button>Adicionar ao Carrinho</button></a>");
                htmlWriter.write("</li>");
                htmlWriter.write("<br>");
            }
            htmlWriter.write("</ul>");
        }

        htmlWriter.write("<a href='/seuCarrinho'><button>Ver carrinho</button></a>");
        htmlReader.close();
        htmlWriter.close();
    }

    @RequestMapping(value = "/adicionaCarrinho", method = RequestMethod.POST)
    public void itemNoCarrinho(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException{
        String idProd = request.getParameter("id"); // id do elemento que foi clicado para adicionar ao carrinho

        // Criar busca no banco pelo id e adicionar ao array

        //implementar a lógica do array com os itens do carrinho
        response.sendRedirect("/lojaFutebol");
    }

    @RequestMapping(value = "/seuCarrinho", method = RequestMethod.GET)
    public String carrinho(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException{
        return "tela do carrinho";
    }
}
