package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.dao;

import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Officer;
import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Rank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOfficerDao implements OfficerDao {
    private static final String OFFICERS_TABLE_NAME = "officers";
    private static final String KEY_COLUMN_NAME = "id";
    private static final String RANK_COLUMN_NAME = "rank";
    private static final String FIRST_NAME_COLUMN_NAME = "first_name";
    private static final String LAST_NAME_COLUMN_NAME = "last_name";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertOfficer;
    private RowMapper<Officer> officerRowMapper = (rs, rowNum) -> new Officer(
            rs.getInt(KEY_COLUMN_NAME),
            Rank.valueOf(rs.getString(RANK_COLUMN_NAME)),
            rs.getString(FIRST_NAME_COLUMN_NAME),
            rs.getString(LAST_NAME_COLUMN_NAME));

    @Autowired
    public JdbcOfficerDao(@NonNull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertOfficer = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(OFFICERS_TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public Officer save(Officer officer) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(RANK_COLUMN_NAME, officer.getRank());
        parameters.put(FIRST_NAME_COLUMN_NAME, officer.getFirst());
        parameters.put(LAST_NAME_COLUMN_NAME, officer.getLast());
        Integer id = insertOfficer.executeAndReturnKey(parameters).intValue();
        officer.setId(id);
        return officer;
    }

    @Override
    public Optional<Officer> findById(Integer id) {
        Optional<Officer> officer;
        if (this.existsById(id)) {
            String sql = String.format("SELECT * FROM %s WHERE %s=?", OFFICERS_TABLE_NAME,
                                       KEY_COLUMN_NAME);
            officer = Optional.ofNullable(jdbcTemplate.queryForObject(sql, officerRowMapper, id));
        } else {
            officer = Optional.empty();
        }
        return officer;
    }

    @Override
    public List<Officer> findAll() {
        String sql = String.format("SELECT * FROM %s", OFFICERS_TABLE_NAME);
        return jdbcTemplate.query(sql, officerRowMapper);
    }

    @Override
    public long count() {
        String sql = String.format("SELECT count(*) from %s", OFFICERS_TABLE_NAME);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return Objects.requireNonNullElse(count, 0).longValue();
    }

    @Override
    public void delete(Officer officer) {
        String sql = String.format("DELETE FROM %s WHERE %s=?", OFFICERS_TABLE_NAME,
                                   KEY_COLUMN_NAME);
        jdbcTemplate.update(sql, officer.getId());
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = String.format("SELECT EXISTS (SELECT 1 FROM %s WHERE %s=?)",
                                   OFFICERS_TABLE_NAME, KEY_COLUMN_NAME);
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, id);
        return Objects.requireNonNullElse(result, false);

    }
}
