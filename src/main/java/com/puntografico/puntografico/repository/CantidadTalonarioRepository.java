package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CantidadTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CantidadTalonarioRepository extends JpaRepository<CantidadTalonario, Long> {
}
