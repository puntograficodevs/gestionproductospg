package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.EntradaDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final OpcionesEntradaService opcionesEntradaService;

    public Entrada guardar(EntradaDTO entradaDTO, Long idEntrada) {
        validarEntradaDTO(entradaDTO);

        TipoPapelEntrada tipoPapelEntrada = opcionesEntradaService.buscarTipoPapelEntradaPorId(entradaDTO.getTipoPapelEntradaId());
        TipoColorEntrada tipoColorEntrada = opcionesEntradaService.buscarTipoColorEntradaPorId(entradaDTO.getTipoColorEntradaId());
        TipoTroqueladoEntrada tipoTroqueladoEntrada = opcionesEntradaService.buscarTipoTroqueladoEntradaPorId(entradaDTO.getTipoTroqueladoEntradaId());
        MedidaEntrada medidaEntrada = opcionesEntradaService.buscarMedidaEntradaPorId(entradaDTO.getMedidaEntradaId());
        CantidadEntrada cantidadEntrada = opcionesEntradaService.buscarCantidadEntradaPorId(entradaDTO.getCantidadEntradaId());
        NumeradoEntrada numeradoEntrada = opcionesEntradaService.buscarNumeradoEntradaPorId(entradaDTO.getNumeradoEntradaId());
        TerminacionEntrada terminacionEntrada = opcionesEntradaService.buscarTerminacionEntradaPorId(entradaDTO.getTerminacionEntradaId());
        Integer cantidad = entradaDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadEntrada.getId() != 7) {
            cantidad = Integer.valueOf(cantidadEntrada.getCantidad());
        }

        Entrada entrada = (idEntrada != null) ? entradaRepository.findById(idEntrada).get() : new Entrada();
        boolean adicionalDisenio = entradaDTO.getConAdicionalDisenio();

        entrada.setEnlaceArchivo(entradaDTO.getEnlaceArchivo());
        entrada.setConAdicionalDisenio(adicionalDisenio);
        entrada.setInformacionAdicional(entradaDTO.getInformacionAdicional());
        entrada.setTipoPapelEntrada(tipoPapelEntrada);
        entrada.setTipoColorEntrada(tipoColorEntrada);
        entrada.setTipoTroqueladoEntrada(tipoTroqueladoEntrada);
        entrada.setMedidaEntrada(medidaEntrada);
        entrada.setCantidadEntrada(cantidadEntrada);
        entrada.setNumeradoEntrada(numeradoEntrada);
        entrada.setTerminacionEntrada(terminacionEntrada);
        entrada.setCantidad(cantidad);

        if (medidaEntrada.getMedida().equalsIgnoreCase("otra")) {
            entrada.setMedidaPersonalizada(entradaDTO.getMedidaPersonalizada());
        } else {
            entrada.setMedidaPersonalizada(null);
        }

        return entradaRepository.save(entrada);
    }

    private void validarEntradaDTO(EntradaDTO entradaDTO) {
        Assert.notNull(entradaDTO.getTipoPapelEntradaId(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(entradaDTO.getTipoColorEntradaId(), "El tipo de color es un dato obligatorio.");
        Assert.notNull(entradaDTO.getTipoTroqueladoEntradaId(), "El tipo de troquelado es un dato obligatorio.");
        Assert.notNull(entradaDTO.getMedidaEntradaId(), "La medida es un dato obligatorio.");
        Assert.notNull(entradaDTO.getNumeradoEntradaId(), "El numerado es un dato obligatorio.");
        Assert.notNull(entradaDTO.getTerminacionEntradaId(), "La terminación es un dato obligatorio.");
        Assert.notNull(entradaDTO.getCantidadEntradaId(), "La cantidad es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        entradaRepository.deleteById(id);
    }
}
