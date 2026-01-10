package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaSelloMadera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaSelloMaderaRepository extends JpaRepository<PlantillaSelloMadera, Long> {
    Optional<PlantillaSelloMadera> findByTamanioSelloMadera_Id(Long tamanioSelloMaderaId);
}
