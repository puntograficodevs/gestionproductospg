package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.StickerDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.StickerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class StickerService {

    private final StickerRepository stickerRepository;
    private final OpcionesStickerService opcionesStickerService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Sticker guardar(StickerDTO stickerDTO, Long idOrdenTrabajo) {
        validarStickerDTO(stickerDTO);
        Sticker sticker = devolverStickerCorrespondiente(idOrdenTrabajo);

        TipoTroqueladoSticker tipoTroqueladoSticker = opcionesStickerService.buscarTipoTroqueladoStickerPorId(stickerDTO.getTipoTroqueladoStickerId());
        CantidadSticker cantidadSticker = opcionesStickerService.buscarCantidadStickerPorId(stickerDTO.getCantidadStickerId());
        MedidaSticker medidaSticker = opcionesStickerService.buscarMedidaStickerPorId(stickerDTO.getMedidaStickerId());
        Integer cantidad = stickerDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadSticker.getId() != 4) {
            cantidad = Integer.valueOf(cantidadSticker.getCantidad());
        }

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

    private Sticker devolverStickerCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Sticker stickerNuevo = new Sticker();
                    stickerNuevo.setOrdenTrabajo(ordenTrabajo);
                    return stickerNuevo;
                });
    }

    public Optional<Sticker> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return stickerRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Sticker sticker = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        stickerRepository.deleteById(sticker.getId());
    }
}
