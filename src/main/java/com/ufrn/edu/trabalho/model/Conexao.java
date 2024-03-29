package com.ufrn.edu.trabalho.model;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    public static Connection getConexao() throws SQLException, URISyntaxException {
        String dbUri = System.getenv("DATABASE_URL");
        String dbUsername = System.getenv("DATABASE_USERNAME");
        String dbSenha = System.getenv("DATABASE_SENHA");

        return DriverManager.getConnection(dbUri, dbUsername, dbSenha);
    }
}
