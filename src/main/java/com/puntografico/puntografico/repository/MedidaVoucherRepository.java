package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaVoucherRepository extends JpaRepository<MedidaVoucher, Long> {
}
