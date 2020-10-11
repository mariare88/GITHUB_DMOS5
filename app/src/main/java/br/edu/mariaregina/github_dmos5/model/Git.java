package br.edu.mariaregina.github_dmos5.model;

import java.io.Serializable;

public class Git implements Serializable {
 private  String nome;
 private String id;

 public Git(String nome,String id){
     this.nome = nome;
     this.id = id;
 }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;

    }
}


