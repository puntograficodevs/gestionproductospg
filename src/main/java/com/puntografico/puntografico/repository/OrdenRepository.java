package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @Query("SELECT o FROM Orden o WHERE " +
            "CAST(o.id AS string) LIKE %:dato% OR " +
            "LOWER(o.nombreCliente) LIKE LOWER(concat('%', :dato, '%')) OR " +
            "o.telefonoCliente LIKE %:dato%")
    List<Orden> buscarPorCriterioGenerico(@Param("dato") String dato);

    List<Orden> findAllByOrderByIdDesc();

    List<Orden> findByNecesitaFacturaTrueAndFacturaHechaFalse();
}
