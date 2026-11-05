package org.example.service;

import org.example.model.Ingrediencia;
import org.example.model.Oddelenie;

import java.math.BigDecimal;

/**
 * Polozka v nákupnom zozname
 */
public class PolozkaNakupu {
    private final Ingrediencia ingrediencia;
    private final BigDecimal mnozstvo;
    private final String jednotka;

    public PolozkaNakupu(Ingrediencia ingrediencia, BigDecimal mnozstvo) {
        this.ingrediencia = ingrediencia;
        this.mnozstvo = mnozstvo;
        this.jednotka = ingrediencia != null ? ingrediencia.getJednotka().getDisplayName() : "";
    }

    public Ingrediencia getIngrediencia() {
        return ingrediencia;
    }

    public BigDecimal getMnozstvo() {
        return mnozstvo;
    }

    public String getJednotka() {
        return jednotka;
    }

    public Oddelenie getOddelenie() {
        return ingrediencia != null ? ingrediencia.getOddelenie() : Oddelenie.INE;
    }

    public String getNazov() {
        return ingrediencia != null ? ingrediencia.getNazov() : "Neznáma ingrediencia";
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f %s", getNazov(), mnozstvo, jednotka);
    }
}
