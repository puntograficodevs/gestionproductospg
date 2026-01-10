package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Rotulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotulacionRepository extends JpaRepository<Rotulacion, Long> {
}
