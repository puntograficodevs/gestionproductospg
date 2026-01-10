package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    Optional<Sticker> findByOrdenTrabajo_Id(Long id);
}
