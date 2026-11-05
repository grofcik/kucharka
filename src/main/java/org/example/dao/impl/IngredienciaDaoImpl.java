package org.example.dao.impl;

import org.example.dao.IngredienciaDao;
import org.example.model.Ingrediencia;
import org.example.model.Jednotka;
import org.example.model.Oddelenie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class IngredienciaDaoImpl implements IngredienciaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = 
        "INSERT INTO ingrediencia (nazov, jednotka, oddelenie) VALUES (?, ?, ?) RETURNING id, nazov, jednotka, oddelenie";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nazov, jednotka, oddelenie FROM ingrediencia WHERE id = ?";
    private static final String SQL_SELECT_BY_NAZOV = 
        "SELECT id, nazov, jednotka, oddelenie FROM ingrediencia WHERE nazov = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nazov, jednotka, oddelenie FROM ingrediencia ORDER BY nazov";
    private static final String SQL_SELECT_BY_ODDELENIE = 
        "SELECT id, nazov, jednotka, oddelenie FROM ingrediencia WHERE oddelenie = ? ORDER BY nazov";
    private static final String SQL_UPDATE = 
        "UPDATE ingrediencia SET nazov = ?, jednotka = ?, oddelenie = ? WHERE id = ? RETURNING id, nazov, jednotka, oddelenie";
    private static final String SQL_DELETE = 
        "DELETE FROM ingrediencia WHERE id = ?";
    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM ingrediencia";

    private final RowMapper<Ingrediencia> rowMapper = new RowMapper<Ingrediencia>() {
        @Override
        public Ingrediencia mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ingrediencia ingrediencia = new Ingrediencia();
            ingrediencia.setId(rs.getLong("id"));
            ingrediencia.setNazov(rs.getString("nazov"));
            ingrediencia.setJednotka(Jednotka.fromString(rs.getString("jednotka")));
            ingrediencia.setOddelenie(Oddelenie.fromString(rs.getString("oddelenie")));
            return ingrediencia;
        }
    };

    public IngredienciaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Ingrediencia create(Ingrediencia ingrediencia) {
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                ingrediencia.getNazov(),
                ingrediencia.getJednotka().name(),
                ingrediencia.getOddelenie().name()
            },
            rowMapper
        );
    }

    @Override
    public Optional<Ingrediencia> findById(Long id) {
        List<Ingrediencia> results = jdbcTemplate.query(
            SQL_SELECT_BY_ID,
            new Object[]{id},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<Ingrediencia> findByNazov(String nazov) {
        List<Ingrediencia> results = jdbcTemplate.query(
            SQL_SELECT_BY_NAZOV,
            new Object[]{nazov},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Ingrediencia> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public List<Ingrediencia> findByOddelenie(Oddelenie oddelenie) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_ODDELENIE,
            new Object[]{oddelenie.name()},
            rowMapper
        );
    }

    @Override
    public Ingrediencia update(Ingrediencia ingrediencia) {
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{
                ingrediencia.getNazov(),
                ingrediencia.getJednotka().name(),
                ingrediencia.getOddelenie().name(),
                ingrediencia.getId()
            },
            rowMapper
        );
    }

    @Override
    public boolean delete(Long id) {
        int updated = jdbcTemplate.update(SQL_DELETE, id);
        return updated > 0;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(SQL_COUNT, Integer.class);
    }
}
