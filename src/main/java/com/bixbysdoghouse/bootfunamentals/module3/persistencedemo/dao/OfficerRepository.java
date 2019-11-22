package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.dao;

import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Officer;
import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Rank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findAllByLast(String last);

    List<Officer> findAllByRankAndLastLike(Rank rank, String lastLike);
}

