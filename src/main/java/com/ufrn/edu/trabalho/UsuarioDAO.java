package com.ufrn.edu.trabalho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuarios (nome, sobrenome, email, senha_hash, tipo_usuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getSobrenome());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getSenha());
            pstmt.setString(5, usuario.getTipoUsuario());
            pstmt.executeUpdate();
        }
    }

    public List<Usuario> listarTodosUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(rs.getString("nome"), rs.getString("sobrenome"),
                        rs.getString("email"), rs.getString("senha_hash")));
            }
        }
        return usuarios;
    }

    public Usuario usuarioCadastrado(String username, String senha) throws SQLException{
        String sql = "SELECT * FROM Usuarios WHERE email = ? AND senha_hash = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1,username);
            pstmt.setString(2, senha);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()){
                    if (rs.getString("email").equals("admin@gmail.com")){
                        return new Usuario(rs.getString("email"), rs.getString("senha_hash"));
                    }
                    return new Usuario(rs.getString("nome"), rs.getString("sobrenome"),
                            rs.getString("email"), rs.getString("senha_hash"));
                }
            }
        }
        return null;
    }
}
