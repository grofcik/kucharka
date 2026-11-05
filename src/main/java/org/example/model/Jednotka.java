package org.example.model;

/**
 * Merná jednotka pre ingrediencie
 */
public enum Jednotka {
    G("g"),
    ML("ml"),
    KS("ks"),
    LYZICA("lyžica"),
    STIPKA("štipka");

    private final String displayName;

    Jednotka(String displayName) {
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
    public static Jednotka fromString(String text) {
        for (Jednotka jednotka : Jednotka.values()) {
            if (jednotka.name().equalsIgnoreCase(text) || 
                jednotka.displayName.equalsIgnoreCase(text)) {
                return jednotka;
            }
        }
        return G; // default
    }
}
