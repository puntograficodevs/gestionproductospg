package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Otro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtroRepository extends JpaRepository<Otro, Long> {
    Optional<Otro> findByOrdenTrabajo_Id(Long id);
}
