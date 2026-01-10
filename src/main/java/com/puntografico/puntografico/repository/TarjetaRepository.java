package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    Optional<Tarjeta> findByOrdenTrabajo_Id(Long id);
}
