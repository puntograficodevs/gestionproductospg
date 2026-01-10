package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadSublimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadSublimacionRepository extends JpaRepository<CantidadSublimacion, Long> {
}
