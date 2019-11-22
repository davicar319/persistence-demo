package com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.dao;

import com.bixbysdoghouse.bootfunamentals.module3.persistencedemo.entities.Officer;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@SuppressWarnings("JpaQlInspection")
@Repository
public class JpaOfficerDao implements OfficerDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Officer save(Officer officer) {
        entityManager.persist(officer);
        return officer;
    }

    @Override
    public Optional<Officer> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Officer.class, id));
    }

    @Override
    public List<Officer> findAll() {
        TypedQuery<Officer> findAllOfficersQuery = entityManager
                .createQuery("select o from Officer o", Officer.class);
        return findAllOfficersQuery.getResultList();
    }

    @Override
    public long count() {
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(o.id) from Officer o", Long.class);
        return countQuery.getSingleResult();
    }

    @Override
    public void delete(Officer officer) {
        entityManager.remove(officer);
    }

    @Override
    public boolean existsById(Integer id) {
        TypedQuery<Long> existsQuery = entityManager.createQuery(
                "select count(o.id) from Officer o where o.id = :id",
                Long.class)
                .setParameter("id", id);
        return existsQuery.getSingleResult() > 0;
    }
}
