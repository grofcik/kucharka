package org.example.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Recept - hlavička receptu + postup prípravy
 */
public class Recept {
    private Long id;
    private String nazov;
    private String postup;
    private Integer casPripravyMin;
    private Integer pocetPorcii;
    private Kategoria kategoria;
    private List<ReceptIngrediencia> ingrediencie;

    public Recept() {
        this.ingrediencie = new ArrayList<>();
    }

    public Recept(Long id, String nazov, String postup, Integer casPripravyMin, 
                  Integer pocetPorcii, Kategoria kategoria) {
        this.id = id;
        this.nazov = nazov;
        this.postup = postup;
        this.casPripravyMin = casPripravyMin;
        this.pocetPorcii = pocetPorcii;
        this.kategoria = kategoria;
        this.ingrediencie = new ArrayList<>();
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

    public String getPostup() {
        return postup;
    }

    public void setPostup(String postup) {
        this.postup = postup;
    }

    public Integer getCasPripravyMin() {
        return casPripravyMin;
    }

    public void setCasPripravyMin(Integer casPripravyMin) {
        this.casPripravyMin = casPripravyMin;
    }

    public Integer getPocetPorcii() {
        return pocetPorcii;
    }

    public void setPocetPorcii(Integer pocetPorcii) {
        this.pocetPorcii = pocetPorcii;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public List<ReceptIngrediencia> getIngrediencie() {
        return new ArrayList<>(ingrediencie);
    }

    public void setIngrediencie(List<ReceptIngrediencia> ingrediencie) {
        this.ingrediencie = new ArrayList<>(ingrediencie);
    }

    public void addIngrediencia(ReceptIngrediencia ingrediencia) {
        this.ingrediencie.add(ingrediencia);
    }

    public void removeIngrediencia(ReceptIngrediencia ingrediencia) {
        this.ingrediencie.remove(ingrediencia);
    }

    /**
     * Vráti čas prípravy ako Duration
     */
    public Duration getCasPripravy() {
        return casPripravyMin != null ? Duration.ofMinutes(casPripravyMin) : Duration.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recept recept = (Recept) o;
        return Objects.equals(id, recept.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recept{id=" + id + ", nazov='" + nazov + "', casPripravyMin=" + casPripravyMin + 
               ", pocetPorcii=" + pocetPorcii + ", kategoria=" + (kategoria != null ? kategoria.getNazov() : "null") + "}";
    }
}
