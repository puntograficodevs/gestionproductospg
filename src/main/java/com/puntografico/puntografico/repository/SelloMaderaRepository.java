package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.SelloMadera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelloMaderaRepository extends JpaRepository<SelloMadera, Long> {
}
