package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTalonarioRepository extends JpaRepository<TipoTalonario, Long> {
}
