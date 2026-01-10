package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorOtro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorOtroRepository extends JpaRepository<TipoColorOtro, Long> {
}
