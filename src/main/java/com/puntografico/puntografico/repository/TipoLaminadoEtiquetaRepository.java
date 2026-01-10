package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoLaminadoEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLaminadoEtiquetaRepository extends JpaRepository<TipoLaminadoEtiqueta, Long> {
}
