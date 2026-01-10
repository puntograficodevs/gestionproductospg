package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.OrdenSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenStickerRepository extends JpaRepository<OrdenSticker, Long> {

    OrdenSticker findByOrdenTrabajo_Id(Long id);
}
