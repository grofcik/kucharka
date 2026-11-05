package org.example.dao.impl;

import org.example.dao.ReceptIngredienciaDao;
import org.example.model.Ingrediencia;
import org.example.model.Recept;
import org.example.model.ReceptIngrediencia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReceptIngredienciaDaoImpl implements ReceptIngredienciaDao {

    private final JdbcTemplate jdbcTemplate;
    private final ReceptDaoImpl receptDao;
    private final IngredienciaDaoImpl ingredienciaDao;

    private static final String SQL_INSERT = 
        "INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) " +
        "VALUES (?, ?, ?) RETURNING recept_id, ingrediencia_id, mnozstvo";
    private static final String SQL_SELECT_BY_RECEPT_INGREDIENCIA = 
        "SELECT recept_id, ingrediencia_id, mnozstvo FROM recept_ingrediencia " +
        "WHERE recept_id = ? AND ingrediencia_id = ?";
    private static final String SQL_SELECT_BY_RECEPT = 
        "SELECT recept_id, ingrediencia_id, mnozstvo FROM recept_ingrediencia " +
        "WHERE recept_id = ? ORDER BY ingrediencia_id";
    private static final String SQL_SELECT_BY_INGREDIENCIA = 
        "SELECT recept_id, ingrediencia_id, mnozstvo FROM recept_ingrediencia " +
        "WHERE ingrediencia_id = ? ORDER BY recept_id";
    private static final String SQL_DELETE = 
        "DELETE FROM recept_ingrediencia WHERE recept_id = ? AND ingrediencia_id = ?";
    private static final String SQL_DELETE_BY_RECEPT = 
        "DELETE FROM recept_ingrediencia WHERE recept_id = ?";
    private static final String SQL_UPDATE_MNOZSTVO = 
        "UPDATE recept_ingrediencia SET mnozstvo = ? WHERE recept_id = ? AND ingrediencia_id = ?";

    private final RowMapper<ReceptIngrediencia> rowMapper = new RowMapper<ReceptIngrediencia>() {
        @Override
        public ReceptIngrediencia mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long receptId = rs.getLong("recept_id");
            Long ingredienciaId = rs.getLong("ingrediencia_id");
            BigDecimal mnozstvo = rs.getBigDecimal("mnozstvo");
            
            Optional<Recept> recept = receptDao.findById(receptId);
            Optional<Ingrediencia> ingrediencia = ingredienciaDao.findById(ingredienciaId);
            
            ReceptIngrediencia ri = new ReceptIngrediencia();
            recept.ifPresent(ri::setRecept);
            ingrediencia.ifPresent(ri::setIngrediencia);
            ri.setMnozstvo(mnozstvo);
            
            return ri;
        }
    };

    public ReceptIngredienciaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.receptDao = new ReceptDaoImpl(jdbcTemplate);
        this.ingredienciaDao = new IngredienciaDaoImpl(jdbcTemplate);
    }

    @Override
    public ReceptIngrediencia create(ReceptIngrediencia receptIngrediencia) {
        if (receptIngrediencia.getRecept() == null || receptIngrediencia.getIngrediencia() == null) {
            throw new IllegalArgumentException("Recept and Ingrediencia must not be null");
        }
        
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                receptIngrediencia.getRecept().getId(),
                receptIngrediencia.getIngrediencia().getId(),
                receptIngrediencia.getMnozstvo()
            },
            rowMapper
        );
    }

    @Override
    public Optional<ReceptIngrediencia> findByReceptAndIngrediencia(Long receptId, Long ingredienciaId) {
        List<ReceptIngrediencia> results = jdbcTemplate.query(
            SQL_SELECT_BY_RECEPT_INGREDIENCIA,
            new Object[]{receptId, ingredienciaId},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<ReceptIngrediencia> findByRecept(Long receptId) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_RECEPT,
            new Object[]{receptId},
            rowMapper
        );
    }

    @Override
    public List<ReceptIngrediencia> findByIngrediencia(Long ingredienciaId) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_INGREDIENCIA,
            new Object[]{ingredienciaId},
            rowMapper
        );
    }

    @Override
    public boolean delete(Long receptId, Long ingredienciaId) {
        int updated = jdbcTemplate.update(SQL_DELETE, receptId, ingredienciaId);
        return updated > 0;
    }

    @Override
    public int deleteByRecept(Long receptId) {
        return jdbcTemplate.update(SQL_DELETE_BY_RECEPT, receptId);
    }

    @Override
    public boolean updateMnozstvo(Long receptId, Long ingredienciaId, BigDecimal mnozstvo) {
        int updated = jdbcTemplate.update(
            SQL_UPDATE_MNOZSTVO,
            mnozstvo, receptId, ingredienciaId
        );
        return updated > 0;
    }
}
