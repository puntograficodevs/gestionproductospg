package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaSobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaSobreRepository extends JpaRepository<MedidaSobre, Long> {
}
