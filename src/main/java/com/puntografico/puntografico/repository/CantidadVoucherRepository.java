package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadVoucherRepository extends JpaRepository<CantidadVoucher, Long> {
}
