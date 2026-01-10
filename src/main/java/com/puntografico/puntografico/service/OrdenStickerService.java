package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenStickerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenStickerService {

    private final OrdenStickerRepository ordenStickerRepository;

    public OrdenSticker guardar(OrdenTrabajo ordenTrabajo, Sticker sticker, Long idOrdenSticker) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(sticker, "Debe venir un sticker para enlazar.");

        OrdenSticker ordenSticker = (idOrdenSticker != null) ? ordenStickerRepository.findById(idOrdenSticker).get() : new OrdenSticker();
        ordenSticker.setCantidad(sticker.getCantidad());
        ordenSticker.setOrdenTrabajo(ordenTrabajo);
        ordenSticker.setSticker(sticker);

        return ordenStickerRepository.save(ordenSticker);
    }

    public OrdenSticker buscarPorId(Long id) {
        return ordenStickerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenSticker con ID " + id + " no encontrada"));
    }

    public OrdenSticker buscarPorOrdenId(Long id) {
        return ordenStickerRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenStickerRepository.deleteById(id);
    }
}
