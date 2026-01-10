package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {
    Optional<Combo> findByOrdenTrabajo_Id(Long id);
}
