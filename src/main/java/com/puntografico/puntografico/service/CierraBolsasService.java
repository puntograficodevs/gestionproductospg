package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CierraBolsasDTO;
import com.puntografico.puntografico.repository.CierraBolsasRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class CierraBolsasService {

    private final CierraBolsasRepository cierraBolsasRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesCierraBolsasService opcionesCierraBolsasService;

    public CierraBolsas guardar(CierraBolsasDTO cierraBolsasDTO, Long idOrdenTrabajo) {
        validarCierraBolsasDTO(cierraBolsasDTO);
        CierraBolsas cierraBolsas = devolverCierraBolsasCorrespondiente(idOrdenTrabajo);

        boolean adicionalDisenio = cierraBolsasDTO.getConAdicionalDisenio();

        TipoTroqueladoCierraBolsas tipoTroqueladoCierraBolsas = opcionesCierraBolsasService.buscarTipoTroqueladoCierraBolsasPorId(cierraBolsasDTO.getTipoTroqueladoCierraBolsasId());
        MedidaCierraBolsas medidaCierraBolsas = opcionesCierraBolsasService.buscarMedidaCierraBolsasPorId(cierraBolsasDTO.getMedidaCierraBolsasId());
        CantidadCierraBolsas cantidadCierraBolsas = opcionesCierraBolsasService.buscarCantidadCierraBolsasPorId(cierraBolsasDTO.getCantidadCierraBolsasId());
        Integer cantidad = cierraBolsasDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadCierraBolsas.getId() != 4) {
            cantidad = Integer.valueOf(cantidadCierraBolsas.getCantidad());
        }

        cierraBolsas.setTipoTroqueladoCierraBolsas(tipoTroqueladoCierraBolsas);
        cierraBolsas.setMedidaCierraBolsas(medidaCierraBolsas);
        cierraBolsas.setCantidadCierraBolsas(cantidadCierraBolsas);
        cierraBolsas.setCantidad(cantidad);
        cierraBolsas.setEnlaceArchivo(cierraBolsasDTO.getEnlaceArchivo());
        cierraBolsas.setConAdicionalDisenio(adicionalDisenio);
        cierraBolsas.setInformacionAdicional(cierraBolsasDTO.getInformacionAdicional());

        if (medidaCierraBolsas.getMedida().equalsIgnoreCase("otra")) {
            cierraBolsas.setMedidaPersonalizada(cierraBolsasDTO.getMedidaPersonalizada());
        } else {
            cierraBolsas.setMedidaPersonalizada(null);
        }

        return cierraBolsasRepository.save(cierraBolsas);
    }

    private void validarCierraBolsasDTO(CierraBolsasDTO cierraBolsasDTO) {
        Assert.notNull(cierraBolsasDTO.getTipoTroqueladoCierraBolsasId(), "El tipo de troquelado es un dato obligatorio.");
        Assert.notNull(cierraBolsasDTO.getMedidaCierraBolsasId(), "La medida es un dato obligatorio.");
        Assert.notNull(cierraBolsasDTO.getCantidadCierraBolsasId(), "La opción de cantidad es un dato obligatorio.");
    }

    private CierraBolsas devolverCierraBolsasCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    CierraBolsas cierraBolsasNuevo = new CierraBolsas();
                    cierraBolsasNuevo.setOrdenTrabajo(ordenTrabajo);
                    return cierraBolsasNuevo;
                });
    }

    public Optional<CierraBolsas> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return cierraBolsasRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        CierraBolsas cierraBolsas = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        cierraBolsasRepository.deleteById(cierraBolsas.getId());
    }
}
