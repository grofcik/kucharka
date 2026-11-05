package org.example.dao.impl;

import org.example.dao.ZasobaDao;
import org.example.model.Ingrediencia;
import org.example.model.Zasoba;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ZasobaDaoImpl implements ZasobaDao {

    private final JdbcTemplate jdbcTemplate;
    private final IngredienciaDaoImpl ingredienciaDao;

    private static final String SQL_INSERT = 
        "INSERT INTO zasoba (ingrediencia_id, mnozstvo) VALUES (?, ?) RETURNING ingrediencia_id, mnozstvo";
    private static final String SQL_SELECT_BY_INGREDIENCIA = 
        "SELECT ingrediencia_id, mnozstvo FROM zasoba WHERE ingrediencia_id = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT ingrediencia_id, mnozstvo FROM zasoba ORDER BY ingrediencia_id";
    private static final String SQL_UPDATE = 
        "UPDATE zasoba SET mnozstvo = ? WHERE ingrediencia_id = ? RETURNING ingrediencia_id, mnozstvo";
    private static final String SQL_DELETE = 
        "DELETE FROM zasoba WHERE ingrediencia_id = ?";
    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM zasoba";
    private static final String SQL_UPDATE_MNOZSTVO = 
        "UPDATE zasoba SET mnozstvo = ? WHERE ingrediencia_id = ?";

    private final RowMapper<Zasoba> rowMapper = new RowMapper<Zasoba>() {
        @Override
        public Zasoba mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long ingredienciaId = rs.getLong("ingrediencia_id");
            BigDecimal mnozstvo = rs.getBigDecimal("mnozstvo");
            
            Optional<Ingrediencia> ingrediencia = ingredienciaDao.findById(ingredienciaId);
            
            Zasoba zasoba = new Zasoba();
            ingrediencia.ifPresent(zasoba::setIngrediencia);
            zasoba.setMnozstvo(mnozstvo);
            
            return zasoba;
        }
    };

    public ZasobaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.ingredienciaDao = new IngredienciaDaoImpl(jdbcTemplate);
    }

    @Override
    public Zasoba create(Zasoba zasoba) {
        if (zasoba.getIngrediencia() == null) {
            throw new IllegalArgumentException("Ingrediencia must not be null");
        }
        
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                zasoba.getIngrediencia().getId(),
                zasoba.getMnozstvo()
            },
            rowMapper
        );
    }

    @Override
    public Optional<Zasoba> findByIngrediencia(Long ingredienciaId) {
        List<Zasoba> results = jdbcTemplate.query(
            SQL_SELECT_BY_INGREDIENCIA,
            new Object[]{ingredienciaId},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Zasoba> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public Zasoba update(Zasoba zasoba) {
        if (zasoba.getIngrediencia() == null) {
            throw new IllegalArgumentException("Ingrediencia must not be null");
        }
        
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{
                zasoba.getMnozstvo(),
                zasoba.getIngrediencia().getId()
            },
            rowMapper
        );
    }

    @Override
    public boolean delete(Long ingredienciaId) {
        int updated = jdbcTemplate.update(SQL_DELETE, ingredienciaId);
        return updated > 0;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject(SQL_COUNT, Integer.class);
    }

    @Override
    public boolean updateMnozstvo(Long ingredienciaId, BigDecimal mnozstvo) {
        int updated = jdbcTemplate.update(
            SQL_UPDATE_MNOZSTVO,
            mnozstvo, ingredienciaId
        );
        return updated > 0;
    }
}
