package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadSticker;
import com.puntografico.puntografico.domain.MedidaSticker;
import com.puntografico.puntografico.domain.TipoTroqueladoSticker;
import com.puntografico.puntografico.repository.CantidadStickerRepository;
import com.puntografico.puntografico.repository.MedidaStickerRepository;
import com.puntografico.puntografico.repository.TipoTroqueladoStickerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesStickerService {

    private final TipoTroqueladoStickerRepository tipoTroqueladoStickerRepository;
    private final CantidadStickerRepository cantidadStickerRepository;
    private final MedidaStickerRepository medidaStickerRepository;

    public List<TipoTroqueladoSticker> buscarTodosTipoTroqueladoSticker() {
        return tipoTroqueladoStickerRepository.findAll();
    }

    public List<CantidadSticker> buscarTodosCantidadSticker() {
        return cantidadStickerRepository.findAll();
    }

    public List<MedidaSticker> buscarTodosMedidaSticker() {
        return medidaStickerRepository.findAll();
    }

    public TipoTroqueladoSticker buscarTipoTroqueladoStickerPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoTroqueladoStickerRepository.findById(id).get();
    }
    public CantidadSticker buscarCantidadStickerPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadStickerRepository.findById(id).get();
    }
    public MedidaSticker buscarMedidaStickerPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaStickerRepository.findById(id).get();
    }
}
