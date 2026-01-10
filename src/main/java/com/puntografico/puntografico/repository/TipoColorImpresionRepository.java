package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorImpresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorImpresionRepository extends JpaRepository<TipoColorImpresion, Long> {
}
