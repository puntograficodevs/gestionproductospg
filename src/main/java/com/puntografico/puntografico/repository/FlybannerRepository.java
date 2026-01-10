package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Flybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlybannerRepository extends JpaRepository<Flybanner, Long> {
}
