package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.FolletoDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class FolletoService {

    private final FolletoRepository folletoRepository;
    private final OpcionesFolletoService opcionesFolletoService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Folleto guardar(FolletoDTO folletoDTO, Long idOrdenTrabajo) {
        validarFolletoDTO(folletoDTO);
        Folleto folleto = devolverFolletoCorrespondiente(idOrdenTrabajo);

        TipoPapelFolleto tipoPapelFolleto = opcionesFolletoService.buscarTipoPapelFolletoPorId(folletoDTO.getTipoPapelFolletoId());
        TipoColorFolleto tipoColorFolleto = opcionesFolletoService.buscarTipoColorFolletoPorId(folletoDTO.getTipoColorFolletoId());
        TipoFazFolleto tipoFazFolleto = opcionesFolletoService.buscarTipoFazFolletoPorId(folletoDTO.getTipoFazFolletoId());
        TamanioHojaFolleto tamanioHojaFolleto = opcionesFolletoService.buscarTamanioHojaFolletoPorId(folletoDTO.getTamanioHojaFolletoId());
        TipoFolleto tipoFolleto = opcionesFolletoService.buscarTipoFolletoPorId(folletoDTO.getTipoFolletoId());
        CantidadFolleto cantidadFolleto = opcionesFolletoService.buscarCantidadFolletoPorId(folletoDTO.getCantidadFolletoId());

        Integer cantidad = folletoDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadFolleto.getId() != 9) {
            cantidad = Integer.valueOf(cantidadFolleto.getCantidad());
        }

        boolean adicionalDisenio = folletoDTO.getConAdicionalDisenio();
        boolean conPlegado = folletoDTO.getConPlegado();

        folleto.setConPlegado(conPlegado);
        folleto.setEnlaceArchivo(folletoDTO.getEnlaceArchivo());
        folleto.setConAdicionalDisenio(adicionalDisenio);
        folleto.setInformacionAdicional(folletoDTO.getInformacionAdicional());
        folleto.setTipoPapelFolleto(tipoPapelFolleto);
        folleto.setTipoColorFolleto(tipoColorFolleto);
        folleto.setTipoFazFolleto(tipoFazFolleto);
        folleto.setTamanioHojaFolleto(tamanioHojaFolleto);
        folleto.setTipoFolleto(tipoFolleto);
        folleto.setCantidadFolleto(cantidadFolleto);
        folleto.setCantidad(cantidad);

        return folletoRepository.save(folleto);
    }

    private void validarFolletoDTO(FolletoDTO folletoDTO) {
        Assert.notNull(folletoDTO.getTipoPapelFolletoId(), "tipoPapelFolletoString no puede venir vacío.");
        Assert.notNull(folletoDTO.getTipoColorFolletoId(), "tipoColorFolletoString no puede venir vacío.");
        Assert.notNull(folletoDTO.getTipoFazFolletoId(), "tipoFazFolletoString no puede venir vacío.");
        Assert.notNull(folletoDTO.getTamanioHojaFolletoId(), "tamanioHojaFolletoString no puede venir vacío.");
        Assert.notNull(folletoDTO.getTipoFolletoId(), "tipoFolletoString no puede venir vacío.");
        Assert.notNull(folletoDTO.getCantidadFolletoId(), "cantidadFolletoString no puede venir vacío.");
    }

    private Folleto devolverFolletoCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Folleto folletoNuevo = new Folleto();
                    folletoNuevo.setOrdenTrabajo(ordenTrabajo);
                    return folletoNuevo;
                });
    }

    public Optional<Folleto> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return folletoRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Folleto folleto = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        folletoRepository.deleteById(folleto.getId());
    }
}
