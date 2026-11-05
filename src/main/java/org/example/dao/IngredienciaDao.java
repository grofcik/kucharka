package org.example.dao;

import org.example.model.Ingrediencia;
import org.example.model.Oddelenie;

import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s ingredienciami
 */
public interface IngredienciaDao {

    /**
     * Vytvorí novú ingredienciu
     * @param ingrediencia Ingrediencia na vytvorenie
     * @return Vytvorená ingrediencia s ID
     */
    Ingrediencia create(Ingrediencia ingrediencia);

    /**
     * Načítanie ingrediencie podľa ID
     * @param id ID ingrediencie
     * @return Optional s ingredienciou alebo prázdny
     */
    Optional<Ingrediencia> findById(Long id);

    /**
     * Načítanie ingrediencie podľa názvu
     * @param nazov Názov ingrediencie
     * @return Optional s ingredienciou alebo prázdny
     */
    Optional<Ingrediencia> findByNazov(String nazov);

    /**
     * Načítanie všetkých ingrediencií
     * @return Zoznam všetkých ingrediencií
     */
    List<Ingrediencia> findAll();

    /**
     * Načítanie ingrediencií podľa oddelenia
     * @param oddelenie Oddelenie v obchode
     * @return Zoznam ingrediencií z daného oddelenia
     */
    List<Ingrediencia> findByOddelenie(Oddelenie oddelenie);

    /**
     * Aktualizácia ingrediencie
     * @param ingrediencia Ingrediencia na aktualizáciu
     * @return Aktualizovaná ingrediencia
     */
    Ingrediencia update(Ingrediencia ingrediencia);

    /**
     * Zmazanie ingrediencie podľa ID
     * @param id ID ingrediencie
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long id);

    /**
     * Získanie počtu ingrediencií
     * @return Počet ingrediencií
     */
    int count();
}
