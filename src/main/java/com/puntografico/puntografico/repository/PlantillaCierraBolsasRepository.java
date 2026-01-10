package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaCierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlantillaCierraBolsasRepository extends JpaRepository<PlantillaCierraBolsas, Long> {

    Optional<PlantillaCierraBolsas> findByTipoTroqueladoCierraBolsas_IdAndMedidaCierraBolsas_IdAndCantidadCierraBolsas_Id(
            Long tipoTroqueladoCierraBolsasId, Long medidaCierraBolsasId, Long cantidadCierraBolsasId
    );
}
