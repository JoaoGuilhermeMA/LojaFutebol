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
                htmlWriter.write("<li>" + produto.getUrl_img() +"<br>"+ produto.getNome_produto()+"<br>" + produto.getTipo_produto()+"<br>" + produto.getPreco()+"<br>" + produto.getDescricao()+"<br>");
                htmlWriter.write("<a href='doExcluir?id=" + produto.getId_produto() + "'>Excluir</a>");
                htmlWriter.write("</li>");
            }
            htmlWriter.write("</ul>");
        }

        htmlReader.close();
        htmlWriter.close();
    }
}
