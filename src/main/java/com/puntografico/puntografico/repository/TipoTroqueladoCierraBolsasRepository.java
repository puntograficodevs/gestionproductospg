package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTroqueladoCierraBolsas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTroqueladoCierraBolsasRepository extends JpaRepository<TipoTroqueladoCierraBolsas, Long> {
}
