package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TarjetaDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class TarjetaService {

    private final TarjetaRepository tarjetaRepository;

    private final OpcionesTarjetaService opcionesTarjetaService;

    public Tarjeta guardar(TarjetaDTO tarjetaDTO, Long idTarjeta) {
        validarTarjetaDTO(tarjetaDTO);

        TipoPapelTarjeta tipoPapelTarjeta = opcionesTarjetaService.buscarTipoPapelTarjetaPorId(tarjetaDTO.getTipoPapelTarjetaId());
        TipoColorTarjeta tipoColorTarjeta = opcionesTarjetaService.buscarTipoColorTarjetaPorId(tarjetaDTO.getTipoColorTarjetaId());
        TipoFazTarjeta tipoFazTarjeta = opcionesTarjetaService.buscarTipoFazTarjetaPorId(tarjetaDTO.getTipoFazTarjetaId());
        TipoLaminadoTarjeta tipoLaminadoTarjeta = opcionesTarjetaService.buscarTipoLaminadoTarjetaPorId(tarjetaDTO.getTipoLaminadoTarjetaId());
        MedidaTarjeta medidaTarjeta = opcionesTarjetaService.buscarMedidaTarjetaPorId(tarjetaDTO.getMedidaTarjetaId());
        CantidadTarjeta cantidadTarjeta = opcionesTarjetaService.buscarCantidadTarjetaPorId(tarjetaDTO.getCantidadTarjetaId());

        Integer cantidad = tarjetaDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadTarjeta.getId() != 9) {
            cantidad = Integer.valueOf(cantidadTarjeta.getCantidad());
        }

        Tarjeta tarjeta = (idTarjeta != null) ? tarjetaRepository.findById(idTarjeta).get() : new Tarjeta();
        boolean adicionalDisenio = tarjetaDTO.getConAdicionalDisenio();

        tarjeta.setEnlaceArchivo(tarjetaDTO.getEnlaceArchivo());
        tarjeta.setConAdicionalDisenio(adicionalDisenio);
        tarjeta.setInformacionAdicional(tarjetaDTO.getInformacionAdicional());
        tarjeta.setTipoPapelTarjeta(tipoPapelTarjeta);
        tarjeta.setTipoColorTarjeta(tipoColorTarjeta);
        tarjeta.setTipoFazTarjeta(tipoFazTarjeta);
        tarjeta.setTipoLaminadoTarjeta(tipoLaminadoTarjeta);
        tarjeta.setMedidaTarjeta(medidaTarjeta);
        tarjeta.setCantidadTarjeta(cantidadTarjeta);
        tarjeta.setCantidad(cantidad);

        if (medidaTarjeta.getMedida().equalsIgnoreCase("otra")) {
            tarjeta.setMedidaPersonalizada(tarjetaDTO.getMedidaPersonalizada());
        } else {
            tarjeta.setMedidaPersonalizada(null);
        }

        return tarjetaRepository.save(tarjeta);
    }

    private void validarTarjetaDTO(TarjetaDTO tarjetaDTO) {
        Assert.notNull(tarjetaDTO.getTipoPapelTarjetaId(), "tipoPapelTarjetaString es un dato obligatorio.");
        Assert.notNull(tarjetaDTO.getTipoColorTarjetaId(), "tipoColorTarjetaString es un dato obligatorio.");
        Assert.notNull(tarjetaDTO.getTipoFazTarjetaId(), "tipoFazTarjetaString es un dato obligatorio.");
        Assert.notNull(tarjetaDTO.getTipoLaminadoTarjetaId(), "tipoLaminadoTarjetaString es un dato obligatorio.");
        Assert.notNull(tarjetaDTO.getMedidaTarjetaId(), "medidaTarjetaString es un dato obligatorio.");
        Assert.notNull(tarjetaDTO.getCantidadTarjetaId(), "cantidadTarjetaString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        tarjetaRepository.deleteById(id);
    }
}
