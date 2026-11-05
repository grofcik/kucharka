package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Spojovacia trieda pre M:N vzťah Recept - Ingrediencia
 * Jedna surovina v jednom recepte s množstvom
 */
public class ReceptIngrediencia {
    private Recept recept;
    private Ingrediencia ingrediencia;
    private BigDecimal mnozstvo;

    public ReceptIngrediencia() {}

    public ReceptIngrediencia(Recept recept, Ingrediencia ingrediencia, BigDecimal mnozstvo) {
        this.recept = recept;
        this.ingrediencia = ingrediencia;
        this.mnozstvo = mnozstvo;
    }

    public Recept getRecept() {
        return recept;
    }

    public void setRecept(Recept recept) {
        this.recept = recept;
    }

    public Ingrediencia getIngrediencia() {
        return ingrediencia;
    }

    public void setIngrediencia(Ingrediencia ingrediencia) {
        this.ingrediencia = ingrediencia;
    }

    public BigDecimal getMnozstvo() {
        return mnozstvo;
    }

    public void setMnozstvo(BigDecimal mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    /**
     * Vráti prepočítané množstvo podľa zadaného počtu porcií
     * @param pocetPorcii Žiadany počet porcií
     * @return Prepočítané množstvo
     */
    public BigDecimal getMnozstvoPrePorcie(int pocetPorcii) {
        if (recept == null || recept.getPocetPorcii() == null || recept.getPocetPorcii() == 0) {
            return mnozstvo;
        }
        return mnozstvo.multiply(BigDecimal.valueOf(pocetPorcii)) 
                      .divide(BigDecimal.valueOf(recept.getPocetPorcii()), 4, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceptIngrediencia that = (ReceptIngrediencia) o;
        return Objects.equals(recept, that.recept) && 
               Objects.equals(ingrediencia, that.ingrediencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recept, ingrediencia);
    }

    @Override
    public String toString() {
        return "ReceptIngrediencia{recept=" + (recept != null ? recept.getNazov() : "null") + 
               ", ingrediencia=" + (ingrediencia != null ? ingrediencia.getNazov() : "null") + 
               ", mnozstvo=" + mnozstvo + " " + (ingrediencia != null ? ingrediencia.getJednotka() : "") + "}";
    }
}
