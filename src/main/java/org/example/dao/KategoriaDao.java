package org.example.dao;

import org.example.model.Kategoria;

import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s kategóriami
 */
public interface KategoriaDao {

    /**
     * Vytvorí novú kategóriu
     * @param kategoria Kategória na vytvorenie
     * @return Vytvorená kategória s ID
     */
    Kategoria create(Kategoria kategoria);

    /**
     * Načítanie kategórie podľa ID
     * @param id ID kategórie
     * @return Optional s kategóriou alebo prázdny
     */
    Optional<Kategoria> findById(Long id);

    /**
     * Načítanie kategórie podľa názvu
     * @param nazov Názov kategórie
     * @return Optional s kategóriou alebo prázdny
     */
    Optional<Kategoria> findByNazov(String nazov);

    /**
     * Načítanie všetkých kategórií
     * @return Zoznam všetkých kategórií
     */
    List<Kategoria> findAll();

    /**
     * Aktualizácia kategórie
     * @param kategoria Kategória na aktualizáciu
     * @return Aktualizovaná kategória
     */
    Kategoria update(Kategoria kategoria);

    /**
     * Zmazanie kategórie podľa ID
     * @param id ID kategórie
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long id);

    /**
     * Získanie počtu kategórií
     * @return Počet kategórií
     */
    int count();
}
