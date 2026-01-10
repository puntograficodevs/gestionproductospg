package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoBaseFlybanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoBaseFlybannerRepository extends JpaRepository<TipoBaseFlybanner, Long> {
}
