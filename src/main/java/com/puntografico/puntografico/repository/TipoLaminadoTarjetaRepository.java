package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoLaminadoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLaminadoTarjetaRepository extends JpaRepository<TipoLaminadoTarjeta, Long> {
}
