package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long>  {

    List<Movimiento> findByOrdenIdOrderByFechaDesc(Long idOrden);
}
