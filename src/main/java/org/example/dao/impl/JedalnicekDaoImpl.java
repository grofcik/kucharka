package org.example.dao.impl;

import org.example.dao.JedalnicekDao;
import org.example.model.Jedalnicek;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JedalnicekDaoImpl implements JedalnicekDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT = 
        "INSERT INTO jedalnicek (nazov, datum_od) VALUES (?, ?) RETURNING id, nazov, datum_od";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nazov, datum_od FROM jedalnicek WHERE id = ?";
    private static final String SQL_SELECT_BY_DATUM = 
        "SELECT id, nazov, datum_od FROM jedalnicek WHERE datum_od = ? ORDER BY nazov";
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nazov, datum_od FROM jedalnicek ORDER BY datum_od DESC, nazov";
    private static final String SQL_UPDATE = 
        "UPDATE jedalnicek SET nazov = ?, datum_od = ? WHERE id = ? RETURNING id, nazov, datum_od";
    private static final String SQL_DELETE = 
        "DELETE FROM jedalnicek WHERE id = ?";
    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM jedalnicek";

    private final RowMapper<Jedalnicek> rowMapper = new RowMapper<Jedalnicek>() {
        @Override
        public Jedalnicek mapRow(ResultSet rs, int rowNum) throws SQLException {
            Jedalnicek jedalnicek = new Jedalnicek();
            jedalnicek.setId(rs.getLong("id"));
            jedalnicek.setNazov(rs.getString("nazov"));
            jedalnicek.setDatumOd(rs.getDate("datum_od").toLocalDate());
            return jedalnicek;
        }
    };

    public JedalnicekDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Jedalnicek create(Jedalnicek jedalnicek) {
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                jedalnicek.getNazov(),
                jedalnicek.getDatumOd()
            },
            rowMapper
        );
    }

    @Override
    public Optional<Jedalnicek> findById(Long id) {
        List<Jedalnicek> results = jdbcTemplate.query(
            SQL_SELECT_BY_ID,
            new Object[]{id},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Jedalnicek> findByDatumOd(LocalDate datumOd) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_DATUM,
            new Object[]{datumOd},
            rowMapper
        );
    }

    @Override
    public List<Jedalnicek> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public Jedalnicek update(Jedalnicek jedalnicek) {
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{
                jedalnicek.getNazov(),
                jedalnicek.getDatumOd(),
                jedalnicek.getId()
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
