package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.EtiquetaDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesEtiquetaService opcionesEtiquetaService;

    public Etiqueta guardar(EtiquetaDTO etiquetaDTO, Long idOrdenTrabajo) {
        validarEtiquetaDTO(etiquetaDTO);
        Etiqueta etiqueta = devolverEtiquetaCorrespondiente(idOrdenTrabajo);

        TipoPapelEtiqueta tipoPapelEtiqueta = opcionesEtiquetaService.buscarTipoPapelEtiquetaPorId(etiquetaDTO.getTipoPapelEtiquetaId());
        TipoLaminadoEtiqueta tipoLaminadoEtiqueta = opcionesEtiquetaService.buscarTipoLaminadoEtiquetaPorId(etiquetaDTO.getTipoLaminadoEtiquetaId());
        TamanioPerforacion tamanioPerforacion = opcionesEtiquetaService.buscarTamanioPerforacionPorId(etiquetaDTO.getTamanioPerforacionId());
        TipoFazEtiqueta tipoFazEtiqueta = opcionesEtiquetaService.buscarTipoFazEtiquetaPorId(etiquetaDTO.getTipoFazEtiquetaId());
        CantidadEtiqueta cantidadEtiqueta = opcionesEtiquetaService.buscarCantidadEtiquetaPorId(etiquetaDTO.getCantidadEtiquetaId());
        MedidaEtiqueta medidaEtiqueta = opcionesEtiquetaService.buscarMedidaEtiquetaPorId(etiquetaDTO.getMedidaEtiquetaId());
        Integer cantidad = etiquetaDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadEtiqueta.getId() != 5) {
            cantidad = Integer.valueOf(cantidadEtiqueta.getCantidad());
        }

        boolean conPerforacionAdicional = etiquetaDTO.getConPerforacionAdicional();
        boolean conMarcaAdicional = etiquetaDTO.getConMarcaAdicional();
        boolean adicionalDisenio = etiquetaDTO.getConAdicionalDisenio();

        etiqueta.setConPerforacionAdicional(conPerforacionAdicional);
        etiqueta.setConMarcaAdicional(conMarcaAdicional);
        etiqueta.setEnlaceArchivo(etiquetaDTO.getEnlaceArchivo());
        etiqueta.setConAdicionalDisenio(adicionalDisenio);
        etiqueta.setInformacionAdicional(etiquetaDTO.getInformacionAdicional());
        etiqueta.setTipoPapelEtiqueta(tipoPapelEtiqueta);
        etiqueta.setTipoLaminadoEtiqueta(tipoLaminadoEtiqueta);
        etiqueta.setTamanioPerforacion(tamanioPerforacion);
        etiqueta.setTipoFazEtiqueta(tipoFazEtiqueta);
        etiqueta.setCantidadEtiqueta(cantidadEtiqueta);
        etiqueta.setMedidaEtiqueta(medidaEtiqueta);
        etiqueta.setCantidad(cantidad);

        if (medidaEtiqueta.getMedida().equalsIgnoreCase("otra")) {
            etiqueta.setMedidaPersonalizada(etiquetaDTO.getMedidaPersonalizada());
        } else {
            etiqueta.setMedidaPersonalizada(null);
        }

        return etiquetaRepository.save(etiqueta);
    }

    private void validarEtiquetaDTO(EtiquetaDTO etiquetaDTO) {
        Assert.notNull(etiquetaDTO.getTipoPapelEtiquetaId(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(etiquetaDTO.getTipoLaminadoEtiquetaId(), "El tipo de laminado es un dato obligatorio.");
        Assert.notNull(etiquetaDTO.getTamanioPerforacionId(), "El tamaño de la perforación es un dato obligatorio.");
        Assert.notNull(etiquetaDTO.getTipoFazEtiquetaId(),"El tipo de faz es un dato obligatorio.");
        Assert.notNull(etiquetaDTO.getMedidaEtiquetaId(), "La medida es un dato obligatorio.");
        Assert.notNull(etiquetaDTO.getCantidadEtiquetaId(), "La cantidad es un dato obligatorio.");
    }

    private Etiqueta devolverEtiquetaCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Etiqueta etiquetaNueva = new Etiqueta();
                    etiquetaNueva.setOrdenTrabajo(ordenTrabajo);
                    return etiquetaNueva;
                });
    }

    public Optional<Etiqueta> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return etiquetaRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Etiqueta etiqueta = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        etiquetaRepository.deleteById(etiqueta.getId());
    }
}
