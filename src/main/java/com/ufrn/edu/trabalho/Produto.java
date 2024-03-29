package com.ufrn.edu.trabalho;

public class Produto {

    private int id_produto;
    private String nome_produto;
    private String descricao;
    private Float preco;
    private int quantidade;
    private String tipo_produto;
    private String url_img;

    public Produto(int idProduto, String nomeProduto, String descricao, float preco, int quantidadeEstoque, String tipoProduto, String urlImagem) {
        this.id_produto = idProduto;
        this.nome_produto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidadeEstoque;
        this.tipo_produto = tipoProduto;
        this.url_img = urlImagem;
    }

    public int getId_produto() {
        return id_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo_produto() {
        return tipo_produto;
    }

    public void setTipo_produto(String tipo_produto) {
        this.tipo_produto = tipo_produto;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

}
