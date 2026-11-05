package org.example.model;

/**
 * Oddelenie v obchode - slúži na zoskupenie nákupného zoznamu
 */
public enum Oddelenie {
    ZELENINA_OVOCIE("Zelenina a ovocie"),
    PECIVO("Pečivo"),
    MLIECNE("Mliečne výrobky"),
    MASO("Mäso a mäsité výrobky"),
    KORENENIE("Korenie"),
    OLEJE_TUKY("Olej a tuky"),
    SYPKINY("Sypkiny"),
    NAPOJE("Nápoje"),
    INE("Iné");

    private final String displayName;

    Oddelenie(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Konvertuje string na enum
     */
    public static Oddelenie fromString(String text) {
        for (Oddelenie oddelenie : Oddelenie.values()) {
            if (oddelenie.name().equalsIgnoreCase(text)) {
                return oddelenie;
            }
        }
        return INE; // default
    }
}
