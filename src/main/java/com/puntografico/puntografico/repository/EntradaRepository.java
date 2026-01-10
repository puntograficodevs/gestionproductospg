package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    Optional<Entrada> findByOrdenTrabajo_Id(Long id);
}
