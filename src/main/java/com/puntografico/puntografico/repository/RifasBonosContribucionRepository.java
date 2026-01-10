package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.RifasBonosContribucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RifasBonosContribucionRepository extends JpaRepository<RifasBonosContribucion, Long> {
}
