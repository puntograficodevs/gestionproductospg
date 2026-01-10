package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorRifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorRifaRepository extends JpaRepository<TipoColorRifa, Long> {
}
