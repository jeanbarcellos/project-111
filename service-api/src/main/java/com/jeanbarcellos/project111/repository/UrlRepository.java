package com.jeanbarcellos.project111.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeanbarcellos.project111.entity.Url;

public interface UrlRepository extends JpaRepository<Url, String> {

    boolean existsByTargetUrl(String targetUrl);

    Optional<Url> findByTargetUrl(String targetUrl);

    @Query("""
            SELECT
              CASE WHEN COUNT(u)> 0 THEN true ELSE false END
            FROM Url u
            WHERE u.user.id=:userId AND u.targetUrl=:targetUrl
                """)
    boolean existsByTargetUrl(@Param("userId") UUID userId, @Param("targetUrl") String targetUrl);

    @Query("SELECT u FROM Url u WHERE u.user.id=:userId AND u.targetUrl=:targetUrl")
    Optional<Url> findByTargetUrl(@Param("userId") UUID userId, @Param("targetUrl") String targetUrl);

}