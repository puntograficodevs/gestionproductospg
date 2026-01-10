package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Sobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SobreRepository extends JpaRepository<Sobre, Long> {
    Optional<Sobre> findByOrdenTrabajo_Id(Long id);
}
