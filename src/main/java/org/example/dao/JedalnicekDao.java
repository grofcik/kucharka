package org.example.dao;

import org.example.model.Jedalnicek;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s jedálničkami
 */
public interface JedalnicekDao {

    /**
     * Vytvorí nový jedálniček
     * @param jedalnicek Jedálniček na vytvorenie
     * @return Vytvorený jedálniček s ID
     */
    Jedalnicek create(Jedalnicek jedalnicek);

    /**
     * Načítanie jedálnička podľa ID
     * @param id ID jedálnička
     * @return Optional s jedálničkom alebo prázdny
     */
    Optional<Jedalnicek> findById(Long id);

    /**
     * Načítanie jedálničkov podľa dátumu
     * @param datumOd Dátum od
     * @return Zoznam jedálničkov
     */
    List<Jedalnicek> findByDatumOd(LocalDate datumOd);

    /**
     * Načítanie všetkých jedálničkov
     * @return Zoznam všetkých jedálničkov
     */
    List<Jedalnicek> findAll();

    /**
     * Aktualizácia jedálnička
     * @param jedalnicek Jedálniček na aktualizáciu
     * @return Aktualizovaný jedálniček
     */
    Jedalnicek update(Jedalnicek jedalnicek);

    /**
     * Zmazanie jedálnička podľa ID
     * @param id ID jedálnička
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long id);

    /**
     * Získanie počtu jedálničkov
     * @return Počet jedálničkov
     */
    int count();
}
