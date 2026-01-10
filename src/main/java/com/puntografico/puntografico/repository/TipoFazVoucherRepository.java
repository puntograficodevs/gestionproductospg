package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazVoucherRepository extends JpaRepository<TipoFazVoucher, Long> {
}
