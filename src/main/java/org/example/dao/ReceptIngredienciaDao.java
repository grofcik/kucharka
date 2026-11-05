package org.example.dao;

import org.example.model.Recept;
import org.example.model.ReceptIngrediencia;

import java.util.List;
import java.util.Optional;

/**
 * DAO rozhranie pre prácu s recept_ingrediencia (M:N vzťah)
 */
public interface ReceptIngredienciaDao {

    /**
     * Vytvorí nový záznam recept_ingrediencia
     * @param receptIngrediencia Záznam na vytvorenie
     * @return Vytvorený záznam
     */
    ReceptIngrediencia create(ReceptIngrediencia receptIngrediencia);

    /**
     * Načítanie záznamu podľa receptu a ingrediencie
     * @param receptId ID receptu
     * @param ingredienciaId ID ingrediencie
     * @return Optional s záznamom alebo prázdny
     */
    Optional<ReceptIngrediencia> findByReceptAndIngrediencia(Long receptId, Long ingredienciaId);

    /**
     * Načítanie všetkých ingrediencií pre daný recept
     * @param receptId ID receptu
     * @return Zoznam ingrediencií pre recept
     */
    List<ReceptIngrediencia> findByRecept(Long receptId);

    /**
     * Načítanie všetkých receptov, ktoré obsahujú danú ingredienciu
     * @param ingredienciaId ID ingrediencie
     * @return Zoznam záznamov
     */
    List<ReceptIngrediencia> findByIngrediencia(Long ingredienciaId);

    /**
     * Zmazanie záznamu podľa receptu a ingrediencie
     * @param receptId ID receptu
     * @param ingredienciaId ID ingrediencie
     * @return true ak sa podarilo zmazať
     */
    boolean delete(Long receptId, Long ingredienciaId);

    /**
     * Zmazanie všetkých ingrediencií pre daný recept
     * @param receptId ID receptu
     * @return Počet zmazaných záznamov
     */
    int deleteByRecept(Long receptId);

    /**
     * Aktualizácia množstva
     * @param receptId ID receptu
     * @param ingredienciaId ID ingrediencie
     * @param mnozstvo Nové množstvo
     * @return true ak sa podarilo aktualizovať
     */
    boolean updateMnozstvo(Long receptId, Long ingredienciaId, java.math.BigDecimal mnozstvo);
}
