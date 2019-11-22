package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.dao;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Officer;
import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Rank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class JdbcOfficerDaoTest {
    @Autowired
    private OfficerDao jdbcOfficerDao;

    @Test
    void save() {
        Officer officer = new Officer(Rank.ENSIGN, "Pavel", "Chekov");
        officer = jdbcOfficerDao.save(officer);
        log.info("officer={}", officer);
        assertNotNull(officer.getId());
    }

    @Test
    void existsById() {
        IntStream.rangeClosed(1, 5)
                 .forEach(id -> assertTrue(jdbcOfficerDao.existsById(id)));
    }

    @Test
    void findAll() {
        List<String> lastNames = jdbcOfficerDao.findAll().stream()
                                               .map(Officer::getLast)
                                               .collect(Collectors.toList());
        MatcherAssert.assertThat(lastNames, containsInAnyOrder("Kirk",
                                                               "Picard",
                                                               "Sisko",
                                                               "Janeway",
                                                               "Archer"));
    }

    @Test
    void count() {
        assertEquals(5, jdbcOfficerDao.count());
    }

    @Test
    void findByIdThatDoesNotExist() {
        Optional<Officer> officer = jdbcOfficerDao.findById(999);
        assertFalse(officer.isPresent());
    }

    @Test
    void findById() {
        Officer expectedOfficer = new Officer(1, Rank.CAPTAIN, "James", "Kirk");
        Optional<Officer> officer = jdbcOfficerDao.findById(1);
        assertTrue(officer.isPresent());
        assertEquals(expectedOfficer, officer.get());
    }

    @Test
    void delete() {
        IntStream.rangeClosed(1, 5)
                 .forEach(id -> {
                     Optional<Officer> officer = jdbcOfficerDao.findById(id);
                     assertTrue(officer.isPresent());
                     jdbcOfficerDao.delete(officer.get());
                 });
        assertEquals(0, jdbcOfficerDao.count());
    }
}