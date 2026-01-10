package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelVoucherRepository extends JpaRepository<TipoPapelVoucher, Long> {
}
