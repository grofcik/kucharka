package org.example.dao.impl;

import org.example.dao.JedalnicekPolozkaDao;
import org.example.model.Jedalnicek;
import org.example.model.JedalnicekPolozka;
import org.example.model.Recept;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public class JedalnicekPolozkaDaoImpl implements JedalnicekPolozkaDao {

    private final JdbcTemplate jdbcTemplate;
    private final JedalnicekDaoImpl jedalnicekDao;
    private final ReceptDaoImpl receptDao;

    private static final String SQL_INSERT = 
        "INSERT INTO jedalnicek_polozka (jedalnicek_id, recept_id, den, pocet_porcii) " +
        "VALUES (?, ?, ?, ?) RETURNING id, jedalnicek_id, recept_id, den, pocet_porcii";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, jedalnicek_id, recept_id, den, pocet_porcii FROM jedalnicek_polozka WHERE id = ?";
    private static final String SQL_SELECT_BY_JEDALNICEK = 
        "SELECT id, jedalnicek_id, recept_id, den, pocet_porcii FROM jedalnicek_polozka " +
        "WHERE jedalnicek_id = ? ORDER BY den, recept_id";
    private static final String SQL_SELECT_BY_JEDALNICEK_AND_DEN = 
        "SELECT id, jedalnicek_id, recept_id, den, pocet_porcii FROM jedalnicek_polozka " +
        "WHERE jedalnicek_id = ? AND den = ? ORDER BY recept_id";
    private static final String SQL_SELECT_BY_RECEPT = 
        "SELECT id, jedalnicek_id, recept_id, den, pocet_porcii FROM jedalnicek_polozka " +
        "WHERE recept_id = ? ORDER BY jedalnicek_id, den";
    private static final String SQL_UPDATE = 
        "UPDATE jedalnicek_polozka SET jedalnicek_id = ?, recept_id = ?, den = ?, pocet_porcii = ? " +
        "WHERE id = ? RETURNING id, jedalnicek_id, recept_id, den, pocet_porcii";
    private static final String SQL_DELETE = 
        "DELETE FROM jedalnicek_polozka WHERE id = ?";
    private static final String SQL_DELETE_BY_JEDALNICEK = 
        "DELETE FROM jedalnicek_polozka WHERE jedalnicek_id = ?";

    private final RowMapper<JedalnicekPolozka> rowMapper = new RowMapper<JedalnicekPolozka>() {
        @Override
        public JedalnicekPolozka mapRow(ResultSet rs, int rowNum) throws SQLException {
            JedalnicekPolozka polozka = new JedalnicekPolozka();
            polozka.setId(rs.getLong("id"));
            
            Long jedalnicekId = rs.getLong("jedalnicek_id");
            jedalnicekDao.findById(jedalnicekId).ifPresent(polozka::setJedalnicek);
            
            Long receptId = rs.getLong("recept_id");
            receptDao.findById(receptId).ifPresent(polozka::setRecept);
            
            polozka.setDen(JedalnicekPolozka.fromDen(rs.getInt("den")));
            polozka.setPocetPorcii(rs.getInt("pocet_porcii"));
            
            return polozka;
        }
    };

    public JedalnicekPolozkaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jedalnicekDao = new JedalnicekDaoImpl(jdbcTemplate);
        this.receptDao = new ReceptDaoImpl(jdbcTemplate);
    }

    @Override
    public JedalnicekPolozka create(JedalnicekPolozka polozka) {
        if (polozka.getJedalnicek() == null || polozka.getRecept() == null) {
            throw new IllegalArgumentException("Jedalnicek and Recept must not be null");
        }
        
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                polozka.getJedalnicek().getId(),
                polozka.getRecept().getId(),
                JedalnicekPolozka.toDen(polozka.getDen()),
                polozka.getPocetPorcii()
            },
            rowMapper
        );
    }

    @Override
    public Optional<JedalnicekPolozka> findById(Long id) {
        List<JedalnicekPolozka> results = jdbcTemplate.query(
            SQL_SELECT_BY_ID,
            new Object[]{id},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<JedalnicekPolozka> findByJedalnicek(Long jedalnicekId) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_JEDALNICEK,
            new Object[]{jedalnicekId},
            rowMapper
        );
    }

    @Override
    public List<JedalnicekPolozka> findByJedalnicekAndDen(Long jedalnicekId, DayOfWeek den) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_JEDALNICEK_AND_DEN,
            new Object[]{jedalnicekId, JedalnicekPolozka.toDen(den)},
            rowMapper
        );
    }

    @Override
    public List<JedalnicekPolozka> findByRecept(Long receptId) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_RECEPT,
            new Object[]{receptId},
            rowMapper
        );
    }

    @Override
    public JedalnicekPolozka update(JedalnicekPolozka polozka) {
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{
                polozka.getJedalnicek().getId(),
                polozka.getRecept().getId(),
                JedalnicekPolozka.toDen(polozka.getDen()),
                polozka.getPocetPorcii(),
                polozka.getId()
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
    public int deleteByJedalnicek(Long jedalnicekId) {
        return jdbcTemplate.update(SQL_DELETE_BY_JEDALNICEK, jedalnicekId);
    }
}
