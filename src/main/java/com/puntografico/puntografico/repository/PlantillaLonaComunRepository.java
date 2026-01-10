package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaLonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaLonaComunRepository extends JpaRepository<PlantillaLonaComun, Long> {
    Optional<PlantillaLonaComun> findByConBolsillosAndMedidaLonaComun_IdAndTipoLonaComun_Id(
            boolean conBolsillos, Long idMedidaLonaComun, Long idTipoLonaComun
    );
}
