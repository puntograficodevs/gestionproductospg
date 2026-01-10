package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.TipoTroqueladoSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTroqueladoStickerRepository extends JpaRepository<TipoTroqueladoSticker, Long> {
}
