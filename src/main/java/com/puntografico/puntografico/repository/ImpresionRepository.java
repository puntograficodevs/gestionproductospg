package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Impresion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpresionRepository extends JpaRepository<Impresion, Long> {
    Optional<Impresion> findByOrdenTrabajo_Id(Long id);
    List<Impresion> findByEsAnilladoTrue();
}
