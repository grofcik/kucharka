package org.example.dao.impl;

import org.example.dao.KategoriaDao;
import org.example.model.Kategoria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class KategoriaDaoImpl implements KategoriaDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = 
        "INSERT INTO kategoria (nazov) VALUES (?) RETURNING id, nazov";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nazov FROM kategoria WHERE id = ?";
    private static final String SQL_SELECT_BY_NAZOV = 
        "SELECT id, nazov FROM kategoria WHERE nazov = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nazov FROM kategoria ORDER BY nazov";
    private static final String SQL_UPDATE = 
        "UPDATE kategoria SET nazov = ? WHERE id = ? RETURNING id, nazov";
    private static final String SQL_DELETE = 
        "DELETE FROM kategoria WHERE id = ?";
    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM kategoria";

    private final RowMapper<Kategoria> rowMapper = new RowMapper<Kategoria>() {
        @Override
        public Kategoria mapRow(ResultSet rs, int rowNum) throws SQLException {
            Kategoria kategoria = new Kategoria();
            kategoria.setId(rs.getLong("id"));
            kategoria.setNazov(rs.getString("nazov"));
            return kategoria;
        }
    };

    public KategoriaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Kategoria create(Kategoria kategoria) {
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{kategoria.getNazov()},
            rowMapper
        );
    }

    @Override
    public Optional<Kategoria> findById(Long id) {
        List<Kategoria> results = jdbcTemplate.query(
            SQL_SELECT_BY_ID,
            new Object[]{id},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Optional<Kategoria> findByNazov(String nazov) {
        List<Kategoria> results = jdbcTemplate.query(
            SQL_SELECT_BY_NAZOV,
            new Object[]{nazov},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Kategoria> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public Kategoria update(Kategoria kategoria) {
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{kategoria.getNazov(), kategoria.getId()},
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
