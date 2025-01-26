package com.jeanbarcellos.project111.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeanbarcellos.project111.entity.Url;

public interface UrlRepository extends JpaRepository<Url, String> {



}