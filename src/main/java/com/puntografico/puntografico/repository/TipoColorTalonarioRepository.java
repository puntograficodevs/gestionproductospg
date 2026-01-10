package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorTalonarioRepository extends JpaRepository<TipoColorTalonario, Long> {
}
