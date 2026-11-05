package org.example.service;

import org.example.dao.JedalnicekPolozkaDao;
import org.example.dao.ReceptIngredienciaDao;
import org.example.dao.ZasobaDao;
import org.example.model.JedalnicekPolozka;
import org.example.model.Oddelenie;
import org.example.model.ReceptIngrediencia;
import org.example.model.Zasoba;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service trieda pre výpočet nákupného zoznamu
 * 
 * Logika:
 * 1. Pre každú položku jedálnička sa množstvo ingrediencie prepočíta podľa porcií
 * 2. Rovnaké ingrediencie sa sčítajú
 * 3. Odpočíta sa zásoba (što máme doma)
 * 4. Výsledok sa zoskupí podľa oddelenia v obchode
 */
public class NakupnyZoznamService {

    private final JedalnicekPolozkaDao jedalnicekPolozkaDao;
    private final ReceptIngredienciaDao receptIngredienciaDao;
    private final ZasobaDao zasobaDao;

    public NakupnyZoznamService(JedalnicekPolozkaDao jedalnicekPolozkaDao,
                                ReceptIngredienciaDao receptIngredienciaDao,
                                ZasobaDao zasobaDao) {
        this.jedalnicekPolozkaDao = jedalnicekPolozkaDao;
        this.receptIngredienciaDao = receptIngredienciaDao;
        this.zasobaDao = zasobaDao;
    }

    /**
     * Vypočíta nákupný zoznam pre daný jedálniček
     * @param jedalnicekId ID jedálnička
     * @return Zoznam poloziek na nákup, zoskupený podľa oddelenia
     */
    public Map<Oddelenie, List<PolozkaNakupu>> vypocitajNakupnyZoznam(Long jedalnicekId) {
        // 1. Načítanie všetkých položiek jedálnička
        List<JedalnicekPolozka> polozky = jedalnicekPolozkaDao.findByJedalnicek(jedalnicekId);
        
        // 2. Sčítanie potreby ingrediencií (prepočítané podľa porcií)
        Map<Long, BigDecimal> ingredienciaPotreba = new HashMap<>();
        
        for (JedalnicekPolozka polozka : polozky) {
            Long receptId = polozka.getRecept().getId();
            int pocetPorcii = polozka.getPocetPorcii();
            
            // Načítanie ingrediencií pre recept
            List<ReceptIngrediencia> receptIngrediencie = receptIngredienciaDao.findByRecept(receptId);
            
            for (ReceptIngrediencia ri : receptIngrediencie) {
                BigDecimal mnozstvo = ri.getMnozstvoPrePorcie(pocetPorcii);
                Long ingredienciaId = ri.getIngrediencia().getId();
                
                ingredienciaPotreba.merge(
                    ingredienciaId,
                    mnozstvo,
                    BigDecimal::add
                );
            }
        }
        
        // 3. Odpočítanie zásoby
        Map<Long, BigDecimal> nakupneMnozstvo = new HashMap<>();
        
        for (Map.Entry<Long, BigDecimal> entry : ingredienciaPotreba.entrySet()) {
            Long ingredienciaId = entry.getKey();
            BigDecimal potreba = entry.getValue();
            
            // Načítanie zásoby pre ingredienciu
            Zasoba zasoba = zasobaDao.findByIngrediencia(ingredienciaId).orElse(null);
            BigDecimal mnozstvoDoma = zasoba != null ? zasoba.getMnozstvo() : BigDecimal.ZERO;
            
            // Vypočítanie chýbajúceho množstva
            BigDecimal chybajuce = potreba.subtract(mnozstvoDoma);
            
            if (chybajuce.compareTo(BigDecimal.ZERO) > 0) {
                nakupneMnozstvo.put(ingredienciaId, chybajuce);
            }
        }
        
        // 4. Konverzia na PolozkaNakupu a zoskupenie podľa oddelenia
        return konvertujNaPolozky(nakupneMnozstvo);
    }

    /**
     * Konvertuje mapu ingredienciaId -> mnozstvo na zoznam poloziek zoskupený podľa oddelenia
     */
    private Map<Oddelenie, List<PolozkaNakupu>> konvertujNaPolozky(Map<Long, BigDecimal> nakupneMnozstvo) {
        Map<Oddelenie, List<PolozkaNakupu>> result = new HashMap<>();
        
        // Získať všetky ingrediencie (pre konverziu ID -> Ingrediencia)
        // Zjednodušenie: V reálnej implementácii by sme použili IngredienciaDao
        // Tu použijeme len ID a predpokladáme, že máme Ingrediencia
        
        // Vytvoríme dočasnú mapu pre testovanie
        // V praxi by sme použili: ingredienciaDao.findById(id)
        
        // Zoradenie podľa oddelenia
        for (Map.Entry<Long, BigDecimal> entry : nakupneMnozstvo.entrySet()) {
            Long ingredienciaId = entry.getKey();
            BigDecimal mnozstvo = entry.getValue();
            
            // Načítanie ingrediencie - zatiaľ len placeholder
            // V reálnej implementácii: ingredienciaDao.findById(ingredienciaId)
            // Tu vytvoríme dočasnú ingredienciu
            org.example.model.Ingrediencia ingrediencia = new org.example.model.Ingrediencia();
            ingrediencia.setId(ingredienciaId);
            ingrediencia.setNazov("Neznáma " + ingredienciaId);
            ingrediencia.setJednotka(org.example.model.Jednotka.G);
            ingrediencia.setOddelenie(Oddelenie.INE);
            
            // Vytvorenie položky
            PolozkaNakupu polozka = new PolozkaNakupu(ingrediencia, mnozstvo);
            Oddelenie oddelenie = polozka.getOddelenie();
            
            result.computeIfAbsent(oddelenie, k -> new ArrayList<>()).add(polozka);
        }
        
        // Zoradenie položiek v každom oddelení podľa názvu
        for (List<PolozkaNakupu> polozky : result.values()) {
            polozky.sort(Comparator.comparing(PolozkaNakupu::getNazov));
        }
        
        return result;
    }

    /**
     * Vráti plochý zoznam všetkých poloziek na nákup
     */
    public List<PolozkaNakupu> vypocitajNakupnyZoznamFlat(Long jedalnicekId) {
        Map<Oddelenie, List<PolozkaNakupu>> zoznam = vypocitajNakupnyZoznam(jedalnicekId);
        
        return zoznam.values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(PolozkaNakupu::getNazov))
                .collect(Collectors.toList());
    }

    /**
     * Vráti nákupný zoznam zoskupený podľa oddelenia (pre UI)
     */
    public Map<String, List<PolozkaNakupu>> vypocitajNakupnyZoznamZoskupeny(Long jedalnicekId) {
        Map<Oddelenie, List<PolozkaNakupu>> zoznam = vypocitajNakupnyZoznam(jedalnicekId);
        
        Map<String, List<PolozkaNakupu>> result = new HashMap<>();
        
        for (Map.Entry<Oddelenie, List<PolozkaNakupu>> entry : zoznam.entrySet()) {
            String nazovOddelenia = entry.getKey().getDisplayName();
            result.put(nazovOddelenia, entry.getValue());
        }
        
        return result;
    }
}
