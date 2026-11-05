package org.example.model;

import java.time.DayOfWeek;
import java.util.Objects;

/**
 * Jedna položka v jedálničku: deň + recept + počet porcií
 */
public class JedalnicekPolozka {
    private Long id;
    private Jedalnicek jedalnicek;
    private Recept recept;
    private DayOfWeek den;
    private int pocetPorcii;

    public JedalnicekPolozka() {}

    public JedalnicekPolozka(Long id, Jedalnicek jedalnicek, Recept recept, DayOfWeek den, int pocetPorcii) {
        this.id = id;
        this.jedalnicek = jedalnicek;
        this.recept = recept;
        this.den = den;
        this.pocetPorcii = pocetPorcii;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jedalnicek getJedalnicek() {
        return jedalnicek;
    }

    public void setJedalnicek(Jedalnicek jedalnicek) {
        this.jedalnicek = jedalnicek;
    }

    public Recept getRecept() {
        return recept;
    }

    public void setRecept(Recept recept) {
        this.recept = recept;
    }

    public DayOfWeek getDen() {
        return den;
    }

    public void setDen(DayOfWeek den) {
        this.den = den;
    }

    public int getPocetPorcii() {
        return pocetPorcii;
    }

    public void setPocetPorcii(int pocetPorcii) {
        this.pocetPorcii = pocetPorcii;
    }

    /**
     * Konvertuje číslo dňa (1-7) na DayOfWeek
     */
    public static DayOfWeek fromDen(int den) {
        return switch (den) {
            case 1 -> DayOfWeek.MONDAY;
            case 2 -> DayOfWeek.TUESDAY;
            case 3 -> DayOfWeek.WEDNESDAY;
            case 4 -> DayOfWeek.THURSDAY;
            case 5 -> DayOfWeek.FRIDAY;
            case 6 -> DayOfWeek.SATURDAY;
            case 7 -> DayOfWeek.SUNDAY;
            default -> DayOfWeek.MONDAY;
        };
    }

    /**
     * Konvertuje DayOfWeek na číslo dňa (1-7)
     */
    public static int toDen(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> 1;
            case TUESDAY -> 2;
            case WEDNESDAY -> 3;
            case THURSDAY -> 4;
            case FRIDAY -> 5;
            case SATURDAY -> 6;
            case SUNDAY -> 7;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JedalnicekPolozka that = (JedalnicekPolozka) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "JedalnicekPolozka{id=" + id + ", den=" + den + ", recept=" + 
               (recept != null ? recept.getNazov() : "null") + ", pocetPorcii=" + pocetPorcii + "}";
    }
}
