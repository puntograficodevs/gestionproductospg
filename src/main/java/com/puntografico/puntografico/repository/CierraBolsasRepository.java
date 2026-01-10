package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CierraBolsasRepository extends JpaRepository<CierraBolsas, Long> {
}
