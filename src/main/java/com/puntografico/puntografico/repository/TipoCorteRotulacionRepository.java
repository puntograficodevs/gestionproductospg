package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoCorteRotulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCorteRotulacionRepository extends JpaRepository<TipoCorteRotulacion, Long> {
}
