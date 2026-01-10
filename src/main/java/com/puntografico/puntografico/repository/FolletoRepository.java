package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Folleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolletoRepository extends JpaRepository<Folleto, Long> {
    Optional<Folleto> findByOrdenTrabajo_Id(Long id);
}
