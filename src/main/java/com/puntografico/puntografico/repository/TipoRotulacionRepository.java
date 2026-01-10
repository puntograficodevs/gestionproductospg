package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoRotulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoRotulacionRepository extends JpaRepository<TipoRotulacion, Long> {
}
