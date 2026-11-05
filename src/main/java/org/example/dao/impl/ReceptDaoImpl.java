package org.example.dao.impl;

import org.example.dao.ReceptDao;
import org.example.model.Kategoria;
import org.example.model.Recept;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReceptDaoImpl implements ReceptDao {

    private final JdbcTemplate jdbcTemplate;
    private final KategoriaDaoImpl kategoriaDao;

    private static final String SQL_INSERT = 
        "INSERT INTO recept (nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id) " +
        "VALUES (?, ?, ?, ?, ?) RETURNING id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id FROM recept WHERE id = ?";
    private static final String SQL_SELECT_BY_KATEGORIA = 
        "SELECT id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id FROM recept WHERE kategoria_id = ? ORDER BY nazov";
    private static final String SQL_SELECT_BY_NAZOV = 
        "SELECT id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id FROM recept WHERE LOWER(nazov) LIKE LOWER(?)";
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id FROM recept ORDER BY nazov";
    private static final String SQL_UPDATE = 
        "UPDATE recept SET nazov = ?, postup = ?, cas_pripravy_min = ?, pocet_porcii = ?, kategoria_id = ? " +
        "WHERE id = ? RETURNING id, nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id";
    private static final String SQL_DELETE = 
        "DELETE FROM recept WHERE id = ?";
    private static final String SQL_COUNT = 
        "SELECT COUNT(*) FROM recept";

    private final RowMapper<Recept> rowMapper = new RowMapper<Recept>() {
        @Override
        public Recept mapRow(ResultSet rs, int rowNum) throws SQLException {
            Recept recept = new Recept();
            recept.setId(rs.getLong("id"));
            recept.setNazov(rs.getString("nazov"));
            recept.setPostup(rs.getString("postup"));
            
            Integer cas = rs.getInt("cas_pripravy_min");
            if (!rs.wasNull()) {
                recept.setCasPripravyMin(cas);
            }
            
            recept.setPocetPorcii(rs.getInt("pocet_porcii"));
            
            Long kategoriaId = rs.getLong("kategoria_id");
            if (!rs.wasNull()) {
                Optional<Kategoria> kategoria = kategoriaDao.findById(kategoriaId);
                kategoria.ifPresent(recept::setKategoria);
            }
            
            return recept;
        }
    };

    public ReceptDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.kategoriaDao = new KategoriaDaoImpl(jdbcTemplate);
    }

    @Override
    public Recept create(Recept recept) {
        Long kategoriaId = recept.getKategoria() != null ? recept.getKategoria().getId() : null;
        
        return jdbcTemplate.queryForObject(
            SQL_INSERT,
            new Object[]{
                recept.getNazov(),
                recept.getPostup(),
                recept.getCasPripravyMin(),
                recept.getPocetPorcii(),
                kategoriaId
            },
            rowMapper
        );
    }

    @Override
    public Optional<Recept> findById(Long id) {
        List<Recept> results = jdbcTemplate.query(
            SQL_SELECT_BY_ID,
            new Object[]{id},
            rowMapper
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Recept> findByKategoria(Kategoria kategoria) {
        if (kategoria == null || kategoria.getId() == null) {
            return List.of();
        }
        return jdbcTemplate.query(
            SQL_SELECT_BY_KATEGORIA,
            new Object[]{kategoria.getId()},
            rowMapper
        );
    }

    @Override
    public List<Recept> findByNazovContaining(String nazov) {
        return jdbcTemplate.query(
            SQL_SELECT_BY_NAZOV,
            new Object[]{"%" + nazov + "%"},
            rowMapper
        );
    }

    @Override
    public List<Recept> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public Recept update(Recept recept) {
        Long kategoriaId = recept.getKategoria() != null ? recept.getKategoria().getId() : null;
        
        return jdbcTemplate.queryForObject(
            SQL_UPDATE,
            new Object[]{
                recept.getNazov(),
                recept.getPostup(),
                recept.getCasPripravyMin(),
                recept.getPocetPorcii(),
                kategoriaId,
                recept.getId()
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
