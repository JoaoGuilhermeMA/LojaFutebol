package com.ufrn.edu.trabalho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {
    private Connection conexao;

    public ItemPedidoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirItemPedido(ItemPedido itemPedido) throws SQLException {
        String sql = "INSERT INTO itens_pedido (id_pedido, id_produto, quantidade, preco_unitario, preco_total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, itemPedido.getIdPedido());
            pstmt.setInt(2, itemPedido.getIdProduto());
            pstmt.setInt(3, itemPedido.getQuantidade());
            pstmt.setDouble(4, itemPedido.getPrecoUnitario());
            pstmt.setDouble(5, itemPedido.getPrecoTotal());
            pstmt.executeUpdate();
        }
    }

    public List<ItemPedido> buscarItensPorPedido(int idPedido) throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = "SELECT * FROM itens_pedido WHERE id_pedido = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, idPedido);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setIdItemPedido(rs.getInt("id_item"));
                    itemPedido.setIdPedido(rs.getInt("id_pedido"));
                    itemPedido.setIdProduto(rs.getInt("id_produto"));
                    itemPedido.setQuantidade(rs.getInt("quantidade"));
                    itemPedido.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    itemPedido.setPrecoTotal(rs.getDouble("preco_total"));
                    itensPedido.add(itemPedido);
                }
            }
        }
        return itensPedido;
    }

    public void excluirItemPedido(int idItemPedido) throws SQLException {
        String sql = "DELETE FROM itens_pedido WHERE id_item = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, idItemPedido);
            pstmt.executeUpdate();
        }
    }

}
