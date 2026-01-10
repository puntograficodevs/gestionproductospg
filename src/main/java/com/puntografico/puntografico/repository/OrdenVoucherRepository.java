package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenVoucherRepository extends JpaRepository<OrdenVoucher, Long> {
    OrdenVoucher findByOrdenTrabajo_Id(Long id);
}
