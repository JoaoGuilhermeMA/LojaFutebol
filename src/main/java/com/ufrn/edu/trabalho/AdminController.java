package com.ufrn.edu.trabalho;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Controller
public class AdminController {

    @RequestMapping(value = "/pageLoja", method = RequestMethod.GET)
    public void pageLoja(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, URISyntaxException {
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
                htmlWriter.write("<li>" + produto.getId_produto() + "<br>" + produto.getNome_produto() + "<br>" + produto.getTipo_produto()+"<br>" + produto.getPreco()+"<br>" + produto.getDescricao()+"<br>");
                htmlWriter.write("<a href='/doExcluir?id=" + produto.getId_produto() + "'><button>Excluir</button></a>");
                htmlWriter.write("</li>");
            }
            htmlWriter.write("</ul>");
        }

        htmlWriter.write("<a href='/pageCadastro.html'><button>Novo produto</button></a>");
        htmlReader.close();
        htmlWriter.close();
    }

    @RequestMapping(value = "/pageCadastro", method = RequestMethod.POST)
    public void cadastrarProd(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException {
        String nomeprod = request.getParameter("nome");
        String descricaoprod = request.getParameter("descricao");
        String tipoprod = request.getParameter("tipo");
        String quantidade = request.getParameter("quantidade");
        String preco = request.getParameter("preco");

        Produto produto = new Produto(nomeprod, descricaoprod, Float.parseFloat(preco), Integer.parseInt(quantidade), tipoprod);
        Conexao conexao = new Conexao();
        var dao = new ProdutoDAO(conexao.getConexao());
        dao.inserirProduto(produto);

        response.sendRedirect("pageCadastro.html");
    }

    @RequestMapping(value = "/doExcluir", method = RequestMethod.GET)
    public void deletarProduto(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException {
        String idParam = request.getParameter("id");
        Conexao conexao = new Conexao();
        var dao = new ProdutoDAO(conexao.getConexao());
        dao.excluirProduto(Integer.parseInt(idParam));
        response.sendRedirect("/pageLoja");
    }
}
