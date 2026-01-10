package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoColorSobre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoColorSobreRepository extends JpaRepository<TipoColorSobre, Long> {
}
