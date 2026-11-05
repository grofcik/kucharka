package org.example.dao;

import org.example.model.Kategoria;
import org.example.model.Recept;

import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s receptami
 */
public interface ReceptDao {

    /**
     * Vytvorí nový recept
     * @param recept Recept na vytvorenie
     * @return Vytvorený recept s ID
     */
    Recept create(Recept recept);

    /**
     * Načítanie receptu podľa ID
     * @param id ID receptu
     * @return Optional s receptom alebo prázdny
     */
    Optional<Recept> findById(Long id);

    /**
     * Načítanie receptov podľa kategórie
     * @param kategoria Kategória
     * @return Zoznam receptov z danej kategórie
     */
    List<Recept> findByKategoria(Kategoria kategoria);

    /**
     * Načítanie receptov podľa názvu (vyhľadávanie)
     * @param nazov Časť názvu
     * @return Zoznam receptov s daným názvom
     */
    List<Recept> findByNazovContaining(String nazov);

    /**
     * Načítanie všetkých receptov
     * @return Zoznam všetkých receptov
     */
    List<Recept> findAll();

    /**
     * Aktualizácia receptu
     * @param recept Recept na aktualizáciu
     * @return Aktualizovaný recept
     */
    Recept update(Recept recept);

    /**
     * Zmazanie receptu podľa ID
     * @param id ID receptu
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long id);

    /**
     * Získanie počtu receptov
     * @return Počet receptov
     */
    int count();
}
