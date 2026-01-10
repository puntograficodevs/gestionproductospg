package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.AlturaFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlturaFlybannerRepository extends JpaRepository<AlturaFlybanner, Long> {
}
