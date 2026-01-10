package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadSticker;
import com.puntografico.puntografico.domain.MedidaSticker;
import com.puntografico.puntografico.domain.Sticker;
import com.puntografico.puntografico.domain.TipoTroqueladoSticker;
import com.puntografico.puntografico.dto.StickerDTO;
import com.puntografico.puntografico.repository.StickerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;
    private final OpcionesStickerService opcionesStickerService;

    public Sticker guardar(StickerDTO stickerDTO, Long idSticker) {
        validarStickerDTO(stickerDTO);

        TipoTroqueladoSticker tipoTroqueladoSticker = opcionesStickerService.buscarTipoTroqueladoStickerPorId(stickerDTO.getTipoTroqueladoStickerId());
        CantidadSticker cantidadSticker = opcionesStickerService.buscarCantidadStickerPorId(stickerDTO.getCantidadStickerId());
        MedidaSticker medidaSticker = opcionesStickerService.buscarMedidaStickerPorId(stickerDTO.getMedidaStickerId());
        Integer cantidad = stickerDTO.getCantidad();

        if (cantidad == null || cantidad == 0) {
            cantidad = Integer.valueOf(cantidadSticker.getCantidad());
        }

        Sticker sticker = (idSticker != null) ? stickerRepository.findById(idSticker).get() : new Sticker();
        boolean adicionalDisenio = stickerDTO.getConAdicionalDisenio();

        sticker.setEnlaceArchivo(stickerDTO.getEnlaceArchivo());
        sticker.setConAdicionalDisenio(adicionalDisenio);
        sticker.setInformacionAdicional(stickerDTO.getInformacionAdicional());
        sticker.setTipoTroqueladoSticker(tipoTroqueladoSticker);
        sticker.setCantidadSticker(cantidadSticker);
        sticker.setMedidaSticker(medidaSticker);
        sticker.setCantidad(cantidad);

        if (medidaSticker.getMedida().equalsIgnoreCase("otra")) {
            sticker.setMedidaPersonalizada(stickerDTO.getMedidaPersonalizada());
        } else {
            sticker.setMedidaPersonalizada(null);
        }

        return stickerRepository.save(sticker);
    }

    private void validarStickerDTO(StickerDTO stickerDTO) {
        Assert.notNull(stickerDTO.getMedidaStickerId(), "medidaStickerString es un dato obligatorio.");
        Assert.notNull(stickerDTO.getTipoTroqueladoStickerId(), "tipoTroqueladoStickerString es un dato obligatorio.");
        Assert.notNull(stickerDTO.getCantidadStickerId(), "cantidadStickerString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        stickerRepository.deleteById(id);
    }
}
