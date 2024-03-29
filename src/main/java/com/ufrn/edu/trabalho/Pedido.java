package com.ufrn.edu.trabalho;

import java.util.Date;

public class Pedido {

    private int id_pedido;
    private int id_usuario;
    private Date hora_pedido;
    private String status_pedido;

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getHora_pedido() {
        return hora_pedido;
    }

    public void setHora_pedido(Date hora_pedido) {
        this.hora_pedido = hora_pedido;
    }

    public String getStatus_pedido() {
        return status_pedido;
    }

    public void setStatus_pedido(String status_pedido) {
        this.status_pedido = status_pedido;
    }
}
