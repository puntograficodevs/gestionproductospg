package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaFolletoRepository extends JpaRepository<PlantillaFolleto, Long> {

    Optional<PlantillaFolleto> findByTipoPapelFolleto_IdAndTipoColorFolleto_IdAndTipoFazFolleto_IdAndTamanioHojaFolleto_IdAndTipoFolleto_IdAndCantidadFolleto_Id(
            Long tipoPapelFolletoId, Long tipoColorFolletoId, Long tipoFazFolletoId, Long tamanioHojaFolletoId, Long tipoFolletoId, Long cantidadFolletoId
    );
}
