package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazFlybannerRepository extends JpaRepository<TipoFazFlybanner, Long> {
}
