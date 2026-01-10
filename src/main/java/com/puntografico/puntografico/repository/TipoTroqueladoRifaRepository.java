package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTroqueladoRifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTroqueladoRifaRepository extends JpaRepository<TipoTroqueladoRifa, Long> {
}
