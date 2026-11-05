package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Týždenný jedálniček
 */
public class Jedalnicek {
    private Long id;
    private String nazov;
    private LocalDate datumOd;
    private List<JedalnicekPolozka> polozky;

    public Jedalnicek() {
        this.polozky = new ArrayList<>();
    }

    public Jedalnicek(Long id, String nazov, LocalDate datumOd) {
        this.id = id;
        this.nazov = nazov;
        this.datumOd = datumOd;
        this.polozky = new ArrayList<>();
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

    public LocalDate getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(LocalDate datumOd) {
        this.datumOd = datumOd;
    }

    public List<JedalnicekPolozka> getPolozky() {
        return new ArrayList<>(polozky);
    }

    public void setPolozky(List<JedalnicekPolozka> polozky) {
        this.polozky = new ArrayList<>(polozky);
    }

    public void addPolozka(JedalnicekPolozka polozka) {
        this.polozky.add(polozka);
    }

    public void removePolozka(JedalnicekPolozka polozka) {
        this.polozky.remove(polozka);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jedalnicek that = (Jedalnicek) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Jedalnicek{id=" + id + ", nazov='" + nazov + "', datumOd=" + datumOd + "}";
    }
}
