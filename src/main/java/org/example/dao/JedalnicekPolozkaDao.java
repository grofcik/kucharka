package org.example.dao;

import org.example.model.JedalnicekPolozka;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s položkami jedálnička
 */
public interface JedalnicekPolozkaDao {

    /**
     * Vytvorí novú položku jedálnička
     * @param polozka Polžka na vytvorenie
     * @return Vytvorená položka s ID
     */
    JedalnicekPolozka create(JedalnicekPolozka polozka);

    /**
     * Načítanie položky podľa ID
     * @param id ID položky
     * @return Optional s položkou alebo prázdny
     */
    Optional<JedalnicekPolozka> findById(Long id);

    /**
     * Načítanie všetkých položiek pre daný jedálniček
     * @param jedalnicekId ID jedálnička
     * @return Zoznam položiek
     */
    List<JedalnicekPolozka> findByJedalnicek(Long jedalnicekId);

    /**
     * Načítanie položiek podľa dňa
     * @param jedalnicekId ID jedálnička
     * @param den Deň v týždni
     * @return Zoznam položiek pre daný deň
     */
    List<JedalnicekPolozka> findByJedalnicekAndDen(Long jedalnicekId, DayOfWeek den);

    /**
     * Načítanie položiek podľa receptu
     * @param receptId ID receptu
     * @return Zoznam položiek
     */
    List<JedalnicekPolozka> findByRecept(Long receptId);

    /**
     * Aktualizácia položky
     * @param polozka Polžka na aktualizáciu
     * @return Aktualizovaná položka
     */
    JedalnicekPolozka update(JedalnicekPolozka polozka);

    /**
     * Zmazanie položky podľa ID
     * @param id ID položky
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long id);

    /**
     * Zmazanie všetkých položiek pre daný jedálniček
     * @param jedalnicekId ID jedálnička
     * @return Počet zmazaných položiek
     */
    int deleteByJedalnicek(Long jedalnicekId);
}
