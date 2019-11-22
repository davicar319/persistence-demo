package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Officer;
import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Rank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class JpaOfficerDaoTest {
    @Autowired
    private OfficerDao jpaOfficerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Integer> idMapper = (rs, num) -> rs.getInt("id");

    @Test
    void testSave() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = jpaOfficerDao.save(officer);
        assertNotNull(officer.getId());
    }

    @Test
    void findOneThatExists() {
        jdbcTemplate.query("select id from officers", idMapper)
                    .forEach(id -> {
                        Optional<Officer> officer = jpaOfficerDao.findById(id);
                        assertTrue(officer.isPresent());
                        assertEquals(id, officer.get().getId());
                    });
    }

    @Test
    void findOneThatDoesNotExist() {
        Optional<Officer> officer = jpaOfficerDao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    void findAll() {
        List<String> names = jpaOfficerDao.findAll().stream()
                                          .map(Officer::getLast)
                                          .collect(Collectors.toList());
        MatcherAssert.assertThat(names, Matchers.containsInAnyOrder("Archer",
                                                                    "Janeway",
                                                                    "Kirk",
                                                                    "Picard",
                                                                    "Sisko"));
    }

    @Test
    void count() {
        assertEquals(5, jpaOfficerDao.count());
    }

    @Test
    void delete() {
        jdbcTemplate.query("select id from officers", idMapper)
                    .forEach(id -> {
                        Optional<Officer> officer = jpaOfficerDao.findById(id);
                        assertTrue(officer.isPresent());
                        jpaOfficerDao.delete(officer.get());
                    });
        assertEquals(0, jpaOfficerDao.count());
    }

    @Test
    void existsById() {
        jdbcTemplate.query("select id from officers", idMapper)
                    .forEach(id -> assertTrue(jpaOfficerDao.existsById(id)));
    }

    @Test
    void doesNotExist() {
        assertFalse(jpaOfficerDao.existsById(90210));
    }
}