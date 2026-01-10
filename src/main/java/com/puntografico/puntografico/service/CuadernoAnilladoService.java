package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CuadernoAnilladoDTO;
import com.puntografico.puntografico.repository.CuadernoAnilladoRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class CuadernoAnilladoService {

    private final CuadernoAnilladoRepository cuadernoAnilladoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesCuadernoAnilladoService opcionesCuadernoAnilladoService;

    public CuadernoAnillado guardar(CuadernoAnilladoDTO cuadernoAnilladoDTO, Long idOrdenTrabajo) {
        validarCuadernoAnilladoDTO(cuadernoAnilladoDTO);
        CuadernoAnillado cuadernoAnillado = devolverCuadernoAnilladoCorrespondiente(idOrdenTrabajo);

        TipoTapaCuadernoAnillado tipoTapaCuadernoAnillado = opcionesCuadernoAnilladoService.buscarTipoTapaCuadernoAnilladoPorId(cuadernoAnilladoDTO.getTipoTapaCuadernoAnilladoId());
        MedidaCuadernoAnillado medidaCuadernoAnillado = opcionesCuadernoAnilladoService.buscarMedidaCuadernoAnilladoPorId(cuadernoAnilladoDTO.getMedidaCuadernoAnilladoId());

        boolean adicionalDisenio = cuadernoAnilladoDTO.getConAdicionalDisenio();

        cuadernoAnillado.setCantidadHojas(cuadernoAnilladoDTO.getCantidadHojas());
        cuadernoAnillado.setEnlaceArchivo(cuadernoAnilladoDTO.getEnlaceArchivo());
        cuadernoAnillado.setConAdicionalDisenio(adicionalDisenio);
        cuadernoAnillado.setInformacionAdicional(cuadernoAnilladoDTO.getInformacionAdicional());
        cuadernoAnillado.setCantidad(cuadernoAnilladoDTO.getCantidad());
        cuadernoAnillado.setTipoTapaCuadernoAnillado(tipoTapaCuadernoAnillado);
        cuadernoAnillado.setMedidaCuadernoAnillado(medidaCuadernoAnillado);

        if (medidaCuadernoAnillado.getMedida().equalsIgnoreCase("otra")) {
            cuadernoAnillado.setMedidaPersonalizada(cuadernoAnilladoDTO.getMedidaPersonalizada());
        } else {
            cuadernoAnillado.setMedidaPersonalizada(null);
        }

        if (tipoTapaCuadernoAnillado.getTipo().equalsIgnoreCase("otra")) {
            cuadernoAnillado.setTipoTapaPersonalizada(cuadernoAnilladoDTO.getTipoTapaPersonalizada());
        } else {
            cuadernoAnillado.setTipoTapaPersonalizada(null);
        }

        return cuadernoAnilladoRepository.save(cuadernoAnillado);
    }

    private void validarCuadernoAnilladoDTO(CuadernoAnilladoDTO cuadernoAnilladoDTO) {
        Assert.notNull(cuadernoAnilladoDTO.getCantidadHojas(), "La cantidad de hojas es un dato obligatorio.");
        Assert.notNull(cuadernoAnilladoDTO.getCantidad(), "La cantidad es un dato obligatorio.");
        Assert.notNull(cuadernoAnilladoDTO.getTipoTapaCuadernoAnilladoId(), "El tipo de tapa es un dato obligatorio.");
        Assert.notNull(cuadernoAnilladoDTO.getMedidaCuadernoAnilladoId(), "La medida del cuaderno es un dato obligatorio.");
    }

    private CuadernoAnillado devolverCuadernoAnilladoCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    CuadernoAnillado cuadernoAnilladoNuevo = new CuadernoAnillado();
                    cuadernoAnilladoNuevo.setOrdenTrabajo(ordenTrabajo);
                    return cuadernoAnilladoNuevo;
                });
    }

    public Optional<CuadernoAnillado> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return cuadernoAnilladoRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        CuadernoAnillado cuadernoAnillado = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        cuadernoAnilladoRepository.deleteById(cuadernoAnillado.getId());
    }
}
