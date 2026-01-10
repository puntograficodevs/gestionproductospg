package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaTurnero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaTurneroRepository extends JpaRepository<MedidaTurnero, Long> {
}
