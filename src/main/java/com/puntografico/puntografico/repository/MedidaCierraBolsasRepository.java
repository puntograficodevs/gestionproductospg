package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaCierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaCierraBolsasRepository extends JpaRepository<MedidaCierraBolsas, Long> {
}
