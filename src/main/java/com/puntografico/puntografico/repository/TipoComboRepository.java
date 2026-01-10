package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoComboRepository extends JpaRepository<TipoCombo, Long> {
}
