package com.unigrade.app.Model;

public class Subject {
    private String codigoMateria;
    private String nomeMateria;

    public Subject(String codigoMateria, String nomeMateria) {
        this.codigoMateria = codigoMateria;
        this.nomeMateria = nomeMateria;
    }

    public String getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(String codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }


}
