package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByOrdenId(Long ordenId);

    @Modifying
    @Query("DELETE FROM Pago p WHERE p.orden.id = :ordenId")
    void deleteByOrdenId(Long ordenId);
}
