package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaViniloPlasticoCorrugado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaViniloPlasticoCorrugadoRepository extends JpaRepository<PlantillaViniloPlasticoCorrugado, Long> {
    Optional<PlantillaViniloPlasticoCorrugado> findByMedidaViniloPlasticoCorrugado_Id(Long medidaViniloPlasticoCorrugadoId);
}
