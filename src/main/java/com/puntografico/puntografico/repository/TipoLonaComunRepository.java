package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoLonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoLonaComunRepository extends JpaRepository<TipoLonaComun, Long> {
}
