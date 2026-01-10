package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    Optional<Catalogo> findByOrdenTrabajo_Id(Long id);
}
