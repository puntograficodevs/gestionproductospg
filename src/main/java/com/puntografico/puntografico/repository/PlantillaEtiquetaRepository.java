package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaEtiquetaRepository extends JpaRepository<PlantillaEtiqueta, Long> {
    Optional<PlantillaEtiqueta> findByTipoPapelEtiqueta_IdAndTipoLaminadoEtiqueta_IdAndTipoFazEtiqueta_IdAndCantidadEtiqueta_IdAndMedidaEtiqueta_Id(
            Long tipoPapelEtiquetaId, Long tipoLaminadoEtiquetaId, Long tipoFazEtiquetaId, Long cantidadEtiquetaId, Long medidaEtiquetaId
    );
}
