package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenCuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenCuadernoAnilladoRepository extends JpaRepository<OrdenCuadernoAnillado, Long> {

    OrdenCuadernoAnillado findByOrdenTrabajo_Id(Long id);
}
