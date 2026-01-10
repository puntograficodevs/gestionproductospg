package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Impresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpresionRepository extends JpaRepository<Impresion, Long> {
}
