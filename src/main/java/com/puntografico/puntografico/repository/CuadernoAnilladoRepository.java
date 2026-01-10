package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuadernoAnilladoRepository extends JpaRepository<CuadernoAnillado, Long> {
    Optional<CuadernoAnillado> findByOrdenTrabajo_Id(Long id);
}
