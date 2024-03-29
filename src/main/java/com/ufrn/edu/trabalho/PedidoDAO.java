package com.ufrn.edu.trabalho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    private Connection conexao;

    public PedidoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (id_usuario, hora_pedido, status_pedido) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, pedido.getId_usuario());
            pstmt.setDate(2, new java.sql.Date(pedido.getHora_pedido().getTime()));
            pstmt.setString(3, pedido.getStatus_pedido());
            pstmt.executeUpdate();
        }
    }

    public List<Pedido> listarTodosPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId_pedido(rs.getInt("id_pedido"));
                pedido.setId_usuario(rs.getInt("id_usuario"));
                pedido.setHora_pedido(rs.getDate("hora_pedido"));
                pedido.setStatus_pedido(rs.getString("status_pedido"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }
}
