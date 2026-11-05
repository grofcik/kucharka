package org.example.dao;

import org.example.model.Ingrediencia;
import org.example.model.Zasoba;

import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu zo zásobami (špajza)
 */
public interface ZasobaDao {

    /**
     * Vytvorí novú zásobu
     * @param zasoba Zásoba na vytvorenie
     * @return Vytvorená zásoba
     */
    Zasoba create(Zasoba zasoba);

    /**
     * Načítanie zásoby podľa ingrediencie
     * @param ingredienciaId ID ingrediencie
     * @return Optional s zásobou alebo prázdny
     */
    Optional<Zasoba> findByIngrediencia(Long ingredienciaId);

    /**
     * Načítanie všetkých zásob
     * @return Zoznam všetkých zásob
     */
    List<Zasoba> findAll();

    /**
     * Aktualizácia zásoby
     * @param zasoba Zásoba na aktualizáciu
     * @return Aktualizovaná zásoba
     */
    Zasoba update(Zasoba zasoba);

    /**
     * Zmazanie zásoby podľa ingrediencie
     * @param ingredienciaId ID ingrediencie
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long ingredienciaId);

    /**
     * Získanie počtu zásob
     * @return Počet zásob
     */
    int count();

    /**
     * Aktualizácia množstva pre danú ingredienciu
     * @param ingredienciaId ID ingrediencie
     * @param mnozstvo Nové množstvo
     * @return true ak sa podarilo aktualizovať
     */
    boolean updateMnozstvo(Long ingredienciaId, java.math.BigDecimal mnozstvo);
}
