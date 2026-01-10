package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazEtiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazEtiquetaRepository extends JpaRepository<TipoFazEtiqueta, Long> {
}
