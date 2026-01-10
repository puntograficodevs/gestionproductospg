package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTapaCuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTapaCuadernoAnilladoRepository extends JpaRepository<TipoTapaCuadernoAnillado, Long> {
}
