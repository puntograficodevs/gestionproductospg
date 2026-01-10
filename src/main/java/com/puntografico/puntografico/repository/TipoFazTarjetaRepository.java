package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazTarjetaRepository extends JpaRepository<TipoFazTarjeta, Long> {
}
