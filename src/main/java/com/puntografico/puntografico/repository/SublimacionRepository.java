package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Sublimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SublimacionRepository extends JpaRepository<Sublimacion, Long> {
}
