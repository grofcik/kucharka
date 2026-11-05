package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Špajza - čo mám doma a koľko
 * Vzťah 1:0..1 s Ingredienciou
 */
public class Zasoba {
    private Ingrediencia ingrediencia;
    private BigDecimal mnozstvo;

    public Zasoba() {}

    public Zasoba(Ingrediencia ingrediencia, BigDecimal mnozstvo) {
        this.ingrediencia = ingrediencia;
        this.mnozstvo = mnozstvo;
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
     * Skontroluje, či je dostatočné množstvo na zadanú požiadavku
     */
    public boolean jeDostatocne(BigDecimal pozadovaneMnozstvo) {
        return mnozstvo != null && mnozstvo.compareTo(pozadovaneMnozstvo) >= 0;
    }

    /**
     * Vráti chýbajúce množstvo
     */
    public BigDecimal getChybajuceMnozstvo(BigDecimal pozadovaneMnozstvo) {
        if (mnozstvo == null) {
            return pozadovaneMnozstvo;
        }
        BigDecimal chybajuce = pozadovaneMnozstvo.subtract(mnozstvo);
        return chybajuce.compareTo(BigDecimal.ZERO) > 0 ? chybajuce : BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zasoba zasoba = (Zasoba) o;
        return Objects.equals(ingrediencia, zasoba.ingrediencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingrediencia);
    }

    @Override
    public String toString() {
        return "Zasoba{ingrediencia=" + (ingrediencia != null ? ingrediencia.getNazov() : "null") + 
               ", mnozstvo=" + mnozstvo + " " + (ingrediencia != null ? ingrediencia.getJednotka() : "") + "}";
    }
}
