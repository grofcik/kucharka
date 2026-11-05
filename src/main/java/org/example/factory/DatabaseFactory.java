package org.example.factory;

import org.example.dao.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Factory trieda pre vytváranie DAO implementácií
 * Používa Spring JdbcTemplate pre prístup k databáze
 */
public class DatabaseFactory {

    private static DatabaseFactory instance;
    private final JdbcTemplate jdbcTemplate;

    private DatabaseFactory(String url, String username, String password) {
        DataSource dataSource = createDataSource(url, username, password);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Vytvorí novú inštanciu factory
     * @param url JDBC URL
     * @param username Používateľské meno
     * @param password Heslo
     * @return Nová inštancia DatabaseFactory
     */
    public static synchronized DatabaseFactory createInstance(String url, String username, String password) {
        if (instance != null) {
            throw new IllegalStateException("DatabaseFactory already initialized. Use getInstance()");
        }
        instance = new DatabaseFactory(url, username, password);
        return instance;
    }

    /**
     * Vráti existujúcu inštanciu
     * @return Existujúca inštancia
     */
    public static synchronized DatabaseFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DatabaseFactory not initialized. Call createInstance() first");
        }
        return instance;
    }

    /**
     * Vytvorí DataSource
     */
    private DataSource createDataSource(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * Vráti JdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Factory metódy pre DAO

    public KategoriaDao getKategoriaDao() {
        return new org.example.dao.impl.KategoriaDaoImpl(jdbcTemplate);
    }

    public IngredienciaDao getIngredienciaDao() {
        return new org.example.dao.impl.IngredienciaDaoImpl(jdbcTemplate);
    }

    public ReceptDao getReceptDao() {
        return new org.example.dao.impl.ReceptDaoImpl(jdbcTemplate);
    }

    public ReceptIngredienciaDao getReceptIngredienciaDao() {
        return new org.example.dao.impl.ReceptIngredienciaDaoImpl(jdbcTemplate);
    }

    public JedalnicekDao getJedalnicekDao() {
        return new org.example.dao.impl.JedalnicekDaoImpl(jdbcTemplate);
    }

    public JedalnicekPolozkaDao getJedalnicekPolozkaDao() {
        return new org.example.dao.impl.JedalnicekPolozkaDaoImpl(jdbcTemplate);
    }

    public ZasobaDao getZasobaDao() {
        return new org.example.dao.impl.ZasobaDaoImpl(jdbcTemplate);
    }

    /**
     * Zavrie factory a vyčistí inštanciu
     */
    public static void close() {
        instance = null;
    }

    /**
     * Inicializácia factory s defaultnými nastaveniami
     * (localhost:5433, používateľ: kucharka, heslo: kucharka)
     */
    public static DatabaseFactory initializeWithDefaults() {
        return createInstance(
            "jdbc:postgresql://localhost:5433/kucharka",
            "kucharka",
            "kucharka"
        );
    }
}
