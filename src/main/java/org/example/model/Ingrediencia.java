package org.example.model;

import java.util.Objects;

/**
 * Číselník surovín - všetko, čo môže byť v recepte
 */
public class Ingrediencia {
    private Long id;
    private String nazov;
    private Jednotka jednotka;
    private Oddelenie oddelenie;

    public Ingrediencia() {}

    public Ingrediencia(Long id, String nazov, Jednotka jednotka, Oddelenie oddelenie) {
        this.id = id;
        this.nazov = nazov;
        this.jednotka = jednotka;
        this.oddelenie = oddelenie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public Jednotka getJednotka() {
        return jednotka;
    }

    public void setJednotka(Jednotka jednotka) {
        this.jednotka = jednotka;
    }

    public Oddelenie getOddelenie() {
        return oddelenie;
    }

    public void setOddelenie(Oddelenie oddelenie) {
        this.oddelenie = oddelenie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediencia that = (Ingrediencia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ingrediencia{id=" + id + ", nazov='" + nazov + "', jednotka=" + jednotka + ", oddelenie=" + oddelenie + "}";
    }
}
