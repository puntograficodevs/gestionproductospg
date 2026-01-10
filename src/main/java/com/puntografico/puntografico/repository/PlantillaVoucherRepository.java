package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.PlantillaVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantillaVoucherRepository extends JpaRepository<PlantillaVoucher, Long> {
    Optional<PlantillaVoucher> findByMedidaVoucher_IdAndTipoPapelVoucher_IdAndTipoFazVoucher_IdAndCantidadVoucher_Id(
            Long medidaVoucherId, Long tipoPapelVoucherId, Long tipoFazVoucherId, Long cantidadVoucherId
    );
}
