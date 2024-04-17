package com.ufrn.edu.trabalho;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.aop.support.DelegatePerTargetObjectIntroductionInterceptor;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

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

    @RequestMapping(value = "/lojaFutebol", method = RequestMethod.GET)
    public void mostraProdutos(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String role = (String) session.getAttribute("role");
            if (!role.equals("cliente")) {
                response.sendRedirect("index.html?msg=voce nao pode acessar a pagina");
                return;
            }
        } else {
            response.sendRedirect("index.html?msg=voce nao pode acessar a pagina");
            return;
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
                // Verifica se a quantidade do produto é maior que zero antes de adicioná-lo ao HTML
                if (produto.getQuantidade() > 0) {
                    htmlWriter.write("<li>" + produto.getNome_produto() + "<br>" + produto.getTipo_produto() + "<br>" + produto.getPreco() + "<br>" + produto.getDescricao() + "<br>");
                    htmlWriter.write("<a href='/adicionaCarrinho?id=" + produto.getId_produto() + "'><button>Adicionar ao Carrinho</button></a>");
                    htmlWriter.write("</li><br><br>");
                }
            }
            htmlWriter.write("</ul>");
        }

        htmlWriter.write("<a href='/seuCarrinho'><button>Ver carrinho</button></a>");
        htmlWriter.write("<br><br><a href='/logout'>Logout</a>");
        htmlReader.close();
        htmlWriter.close();
    }

    @RequestMapping(value = "/adicionaCarrinho", method = {RequestMethod.POST, RequestMethod.GET})
    public void itemNoCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String idProd = request.getParameter("id");
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute("idUser");

        String nomeCookie = "carrinho_" + idUser;

        Cookie[] cookies = request.getCookies();
        Cookie carrinhoCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(nomeCookie)) {
                    carrinhoCookie = cookie;
                    break;
                }
            }
        }

        if (carrinhoCookie == null) {
            carrinhoCookie = new Cookie(nomeCookie, idProd + ":1");
        } else {
            String valorCookie = carrinhoCookie.getValue();
            if (valorCookie.contains(idProd + ":")) { // Verifica se o ID do produto está presente
                // Encontra a posição do ID do produto no cookie
                int pos = valorCookie.indexOf(idProd + ":");
                // Encontra o fim do valor da quantidade
                int endPos = valorCookie.indexOf("-", pos);
                if (endPos == -1) { // Se não houver outro hífen, considera o fim do valor do cookie
                    endPos = valorCookie.length();
                }
                // Extrai o valor da quantidade do cookie
                String quantidadeStr = valorCookie.substring(pos + idProd.length() + 1, endPos);
                int quantidade = Integer.parseInt(quantidadeStr) + 1;
                // Substitui o valor da quantidade no cookie
                valorCookie = valorCookie.substring(0, pos + idProd.length() + 1) + quantidade + valorCookie.substring(endPos);
            } else {
                valorCookie += "-" + idProd + ":1";
            }
            carrinhoCookie.setValue(valorCookie);
        }


        carrinhoCookie.setMaxAge(48 * 60 * 60);
        response.addCookie(carrinhoCookie);

        response.sendRedirect("/lojaFutebol");
    }

    @RequestMapping(value = "/seuCarrinho", method = RequestMethod.GET)
    public void carrinho(HttpServletRequest request, HttpServletResponse response) throws SQLException, URISyntaxException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        BufferedReader htmlReader = new BufferedReader(new FileReader("src/main/resources/static/carrinho.html"));
        BufferedWriter htmlWriter = new BufferedWriter(writer);

        String line;
        while ((line = htmlReader.readLine()) != null) {
            htmlWriter.write(line + "\n");
        }
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute("idUser");

        String nomeCookie = "carrinho_" + idUser;

        Cookie[] cookies = request.getCookies();
        float totalCompra = 0.0F;
        if (cookies != null) {
            Conexao conexao = new Conexao();
            var prod = new ProdutoDAO(conexao.getConexao());
            for (Cookie cok : cookies) {
                if (cok.getName().equals(nomeCookie)) {
                    //split remove a parte da string que seja '-' e divide a string até antes do caracter
                    String[] items = cok.getValue().split("-");
                    htmlWriter.write("<ul>");
                    for (String item : items) {
                        String[] parts = item.split(":");
                        if (parts.length == 2 && !parts[1].isEmpty()) { // Verifica se há dois elementos e o segundo não está vazio
                            int idProduto = Integer.parseInt(parts[0]);
                            int quantidade = Integer.parseInt(parts[1]);
                            Produto prodAux = prod.buscarProdutoId(idProduto);
                            float precoUnitario = prodAux.getPreco();
                            totalCompra += precoUnitario * quantidade; // Adicione o preço total deste item ao total da compra
                            htmlWriter.write("<li>Produto: " + prodAux.getNome_produto() + "</li><br><li>Descrição:" + prodAux.getDescricao() +
                                    "</li><br><li>Preço unitário: " + prodAux.getPreco() + "</li><br><li>Quantidade: " + quantidade + "</li>" +
                                    "<a href='/tirarItem?id=" + idProduto + "'>Diminuir Quantidade</a><br>");
                        }
                    }
                    htmlWriter.write("</ul>");
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String totalFormato = decimalFormat.format(totalCompra);
        htmlWriter.write("<br><br>Total: " + totalFormato);
        htmlWriter.write("<br><br><a href='finalizarCompra'><button>Finalizar Compra</button></a>");
        htmlReader.close();
        htmlWriter.close();
    }


    @RequestMapping(value = "/tirarItem")
    public void removeQuantidade(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute("idUser");
        String idProd = request.getParameter("id"); // Obtenha o ID do produto a ser removido

        String nomeCookie = "carrinho_" + idUser;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cok : cookies) {
                if (cok.getName().equals(nomeCookie)) {
                    String valorCookie = cok.getValue();
                    if (valorCookie != null && !valorCookie.isEmpty()) { // Verifica se o cookie não está vazio
                        StringBuilder novoValorCookie = new StringBuilder();

                        String[] items = valorCookie.split("-");
                        for (String item : items) {
                            String[] parts = item.split(":");
                            if (parts.length == 2 && parts[0].equals(idProd)) { // Verifica se o item possui o formato esperado e se corresponde ao ID do produto
                                int quantidade = Integer.parseInt(parts[1]) - 1; // Remova uma quantidade
                                if (quantidade > 0) {
                                    novoValorCookie.append(parts[0]).append(":").append(quantidade).append("-");
                                }
                            } else {
                                novoValorCookie.append(item).append("-");
                            }
                        }

                        // Defina o novo valor do cookie sem o item removido
                        if (novoValorCookie.length() > 0) {
                            novoValorCookie.setLength(novoValorCookie.length() - 1); // Remova o último hífen
                        }
                        cok.setValue(novoValorCookie.toString());
                        response.addCookie(cok);
                        break;
                    }
                }
            }
        }
        response.sendRedirect("/seuCarrinho");
    }


    @RequestMapping(value = "/finalizarCompra")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, URISyntaxException {
        HttpSession session = request.getSession();
        Integer idUser = (Integer) session.getAttribute("idUser");

        String nomeCookie = "carrinho_" + idUser;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Conexao conexao = new Conexao();
            var produtoDAO = new ProdutoDAO(conexao.getConexao());
            StringBuilder mensagemErro = new StringBuilder("Erro na compra dos seguintes produtos:<br>");

            for (Cookie cok : cookies) {
                if (cok.getName().equals(nomeCookie)) {
                    String valorCookie = cok.getValue();
                    if (valorCookie != null && !valorCookie.isEmpty()) { // Verifica se o cookie não está vazio
                        String[] items = valorCookie.split("-");

                        for (String item : items) {
                            String[] parts = item.split(":");
                            if (parts.length == 2) { // Verifica se o item possui o formato esperado
                                int idProduto = Integer.parseInt(parts[0]);
                                int quantidade = Integer.parseInt(parts[1]);

                                Produto produto = produtoDAO.buscarProdutoId(idProduto);
                                if (produto != null) {
                                    if (produto.getQuantidade() >= quantidade) {
                                        produto.setQuantidade(produto.getQuantidade() - quantidade);
                                        produtoDAO.atualizaQuantidade(produto);
                                        cok.setMaxAge(0);
                                        response.addCookie(cok);
                                    } else {
                                        mensagemErro.append(produto.getNome_produto()).append(": Estoque insuficiente (").append(produto.getQuantidade()).append(" disponíveis)<br>");
                                    }
                                } else {
                                    mensagemErro.append("Produto não encontrado<br>");
                                }
                            }
                        }


                    }
                }
            }

            if (mensagemErro.length() > "Erro na compra dos seguintes produtos:<br>".length()) {
                // Se houver algum erro na compra, notificar o cliente
                PrintWriter writer = response.getWriter();
                writer.println("<html><body>");
                writer.println("<h1>" + mensagemErro.toString() + "</h1>");
                writer.println("<a href='/seuCarrinho'>Voltar ao Carrinho</a>");
                writer.println("</body></html>");
            } else {
                // Se a compra for bem-sucedida, redirecionar para uma página de confirmação ou outra página desejada
                response.sendRedirect("/compraConfirmada");
            }
        }
    }
    @RequestMapping(value = "/compraConfirmada", method = RequestMethod.GET)
    public String mensagemCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "compraConfirmada.html";
    }
}
