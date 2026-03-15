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
            "(CAST(o.id AS string) LIKE %:dato% OR " +
            "LOWER(o.nombreCliente) LIKE LOWER(concat('%', :dato, '%')) OR " +
            "o.telefonoCliente LIKE %:dato%) " +
            "AND (" +
            "  (:idRol = 2L AND o.empleado.id = (SELECT e.id FROM Empleado e WHERE e.rol.id = 2L AND e.id = o.empleado.id)) " +
            "  OR " +
            "  (:idRol != 2L AND o.empleado.rol.id != 2L)" +
            ")")
    List<Orden> buscarPorIdNombreClienteOTelefono(@Param("dato") String dato, @Param("idRol") Long idRol);

    @Query("SELECT o FROM Orden o WHERE o.necesitaFactura = true AND o.facturaHecha = false " +
            "AND (" +
            "  (:idRol = 2L AND o.empleado.rol.id = 2L) OR " +
            "  (:idRol != 2L AND o.empleado.rol.id != 2L)" +
            ")")
    List<Orden> buscarFacturasPendientesSegunRol(@Param("idRol") Long idRol);

    @Query("SELECT o FROM Orden o WHERE o.estadoOrden.id = :idEstado " +
            "AND (" +
            "  :rolId <> 5L " +
            "  OR NOT EXISTS (" +
            "    SELECT i FROM OrdenItem i WHERE i.orden = o AND i.producto.id = 12L" +
            "  )" +
            ") " +
            "AND (" +
            "  (:rolId = 2L AND o.empleado.rol.id = 2L) " +
            "  OR (:rolId <> 2L AND o.empleado.rol.id <> 2L)" +
            ")")
    List<Orden> buscarOrdenesConEstadoSegunRol(@Param("idEstado") Long idEstado, @Param("rolId") Long rolId);
}
