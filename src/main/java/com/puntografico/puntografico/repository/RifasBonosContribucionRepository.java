package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.RifasBonosContribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RifasBonosContribucionRepository extends JpaRepository<RifasBonosContribucion, Long> {
    Optional<RifasBonosContribucion> findByOrdenTrabajo_Id(Long id);
}
