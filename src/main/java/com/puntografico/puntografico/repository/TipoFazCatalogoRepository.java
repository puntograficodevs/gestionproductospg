package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoFazCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFazCatalogoRepository extends JpaRepository<TipoFazCatalogo, Long> {
}
