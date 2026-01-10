package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Flybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlybannerRepository extends JpaRepository<Flybanner, Long> {
    Optional<Flybanner> findByOrdenTrabajo_Id(Long id);
}
