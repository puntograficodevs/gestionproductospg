package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.BanderaFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanderaFlybannerRepository extends JpaRepository<BanderaFlybanner, Long> {
}
