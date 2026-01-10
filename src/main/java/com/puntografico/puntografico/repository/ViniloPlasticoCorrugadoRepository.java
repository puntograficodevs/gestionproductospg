package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.ViniloPlasticoCorrugado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViniloPlasticoCorrugadoRepository extends JpaRepository<ViniloPlasticoCorrugado, Long> {
    Optional<ViniloPlasticoCorrugado> findByOrdenTrabajo_Id(Long id);
}
