package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorTarjetaRepository extends JpaRepository<TipoColorTarjeta, Long> {
}
