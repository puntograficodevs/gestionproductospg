package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> {

    List<OrdenTrabajo> findByEstadoOrdenId(Long id);
    List<OrdenTrabajo> findByEstadoOrdenIdAndTipoProducto(Long id, String tipoProducto);
    List<OrdenTrabajo> findByNombreClienteContainingIgnoreCaseOrTelefonoClienteContaining(String nombre, String telefono);
}
