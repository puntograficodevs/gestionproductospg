package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadSobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadSobreRepository extends JpaRepository<CantidadSobre, Long> {
}
