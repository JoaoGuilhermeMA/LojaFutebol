package com.ufrn.edu.trabalho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private Connection conexao;

    public ProdutoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome_produtos, descricao, preco, quantidade_estoque, tipo_produto, url_imagem) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, produto.getNome_produto());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setFloat(3, produto.getPreco());
            pstmt.setInt(4, produto.getQuantidade());
            pstmt.setString(5, produto.getTipo_produto());
            pstmt.setString(6, produto.getUrl_img());
            pstmt.executeUpdate();
        }
    }

    public Produto buscarProduto(String tipo) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE tipo_produto = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Produto(rs.getInt("id_produto"), rs.getString("nome_produto"), rs.getString("descricao"),
                            rs.getFloat("preco"), rs.getInt("quantidade_estoque"), rs.getString("tipo_produto"), rs.getString("url_imagem"));
                }
            }
        }
        return null;
    }

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("id_produto"), rs.getString("nome_produto"), rs.getString("descricao"),
                        rs.getFloat("preco"), rs.getInt("quantidade_estoque"), rs.getString("tipo_produto"), rs.getString("url_imagem")));
            }
        }
        return produtos;
    }

    public void excluirProduto(int idProduto) throws SQLException{
        String sql = "DELETE FROM Produtos WHERE id_produto = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, idProduto);
            pstmt.executeUpdate();
        }
    }
}
