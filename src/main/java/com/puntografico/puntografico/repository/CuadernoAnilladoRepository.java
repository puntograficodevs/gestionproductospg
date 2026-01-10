package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.CuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuadernoAnilladoRepository extends JpaRepository<CuadernoAnillado, Long> {
}
