package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TamanioSelloMadera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanioSelloMaderaRepository extends JpaRepository<TamanioSelloMadera, Long> {
}
