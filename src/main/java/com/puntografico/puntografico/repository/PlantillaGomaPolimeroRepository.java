package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaGomaPolimero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaGomaPolimeroRepository extends JpaRepository<PlantillaGomaPolimero, Long>  {
    Optional<PlantillaGomaPolimero> findByModeloGomaPolimero_Id(Long modeloGomaPolimeroId);
}
