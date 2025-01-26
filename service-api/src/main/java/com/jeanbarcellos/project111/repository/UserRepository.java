package com.jeanbarcellos.project111.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeanbarcellos.project111.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "select w.id from User w WHERE w.email = :email")
    Optional<UUID> findIdByEmail(@Param("email") String email);

}