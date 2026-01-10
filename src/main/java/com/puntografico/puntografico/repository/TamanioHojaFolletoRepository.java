package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TamanioHojaFolleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanioHojaFolletoRepository extends JpaRepository<TamanioHojaFolleto, Long> {
}
