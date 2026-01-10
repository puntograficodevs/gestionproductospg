package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Anotador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnotadorRepository extends JpaRepository<Anotador, Long> {
    Optional<Anotador> findByOrdenTrabajo_Id(Long id);
}
