package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoPapelTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPapelTalonarioRepository extends JpaRepository<TipoPapelTalonario, Long> {
}
