package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MedidaCuadernoAnillado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedidaCuadernoAnilladoRepository extends JpaRepository<MedidaCuadernoAnillado, Long> {
}
