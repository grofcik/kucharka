package org.example.model;

import java.util.Objects;

/**
 * Kategória receptov (napr. Polievka, Hlavné jedlo, Dezert)
 */
public class Kategoria {
    private Long id;
    private String nazov;

    public Kategoria() {}

    public Kategoria(Long id, String nazov) {
        this.id = id;
        this.nazov = nazov;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kategoria kategoria = (Kategoria) o;
        return Objects.equals(id, kategoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Kategoria{id=" + id + ", nazov='" + nazov + "'}";
    }
}
