package com.tcdt.qlnvhang.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;

@NoRepositoryBean
public class BaseRepositoryImpl<T, PK extends Serializable> extends SimpleJpaRepository<T, PK> implements BaseRepository<T, PK> {
    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public Integer findMaxSo(String maDvi, Integer nam) {

        String jpql = "SELECT MAX(e.so) FROM " + getDomainClass().getName() +
                " e WHERE e.maDvi = :maDvi AND e.nam = :nam";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        query.setParameter("maDvi", maDvi);
        query.setParameter("nam", nam);
        return query.getSingleResult();
    }
}
