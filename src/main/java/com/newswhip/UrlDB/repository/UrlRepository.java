package com.newswhip.UrlDB.repository;

import com.newswhip.UrlDB.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Integer> {
    @Transactional
    Integer deleteByUrl(String url);
}
