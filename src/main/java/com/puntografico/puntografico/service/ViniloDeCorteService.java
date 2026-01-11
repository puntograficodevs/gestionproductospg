package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.TraeMaterialVinilo;
import com.puntografico.puntografico.domain.ViniloDeCorte;
import com.puntografico.puntografico.dto.ViniloDeCorteDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.ViniloDeCorteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class ViniloDeCorteService {

    private final ViniloDeCorteRepository viniloDeCorteRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesViniloDeCorteService opcionesViniloDeCorteService;

    public ViniloDeCorte guardar(ViniloDeCorteDTO viniloDeCorteDTO, Long idOrdenTrabajo) {
        validarViniloDeCorteDTO(viniloDeCorteDTO);
        ViniloDeCorte viniloDeCorte = devolverViniloDeCorteCorrespondiente(idOrdenTrabajo);

        TraeMaterialVinilo traeMaterialVinilo = opcionesViniloDeCorteService.buscarTraeMaterialViniloPorId(viniloDeCorteDTO.getTraeMaterialViniloId());

        boolean adicionalDisenio = viniloDeCorteDTO.getConAdicionalDisenio();
        boolean esPromocional = viniloDeCorteDTO.getEsPromocional();
        boolean esOracal = viniloDeCorteDTO.getEsOracal();
        boolean conColocacion = viniloDeCorteDTO.getConColocacion();

        viniloDeCorte.setEsPromocional(esPromocional);
        viniloDeCorte.setEsOracal(esOracal);
        viniloDeCorte.setCodigoColor(viniloDeCorteDTO.getCodigoColor());
        viniloDeCorte.setConColocacion(conColocacion);
        viniloDeCorte.setMedida(viniloDeCorteDTO.getMedida());
        viniloDeCorte.setEnlaceArchivo(viniloDeCorteDTO.getEnlaceArchivo());
        viniloDeCorte.setConAdicionalDisenio(adicionalDisenio);
        viniloDeCorte.setInformacionAdicional(viniloDeCorteDTO.getInformacionAdicional());
        viniloDeCorte.setTraeMaterialVinilo(traeMaterialVinilo);
        viniloDeCorte.setCantidad(viniloDeCorteDTO.getCantidad());

        return viniloDeCorteRepository.save(viniloDeCorte);
    }

    private void validarViniloDeCorteDTO(ViniloDeCorteDTO viniloDeCorteDTO) {
        Assert.notNull(viniloDeCorteDTO.getTraeMaterialViniloId(), "traeMaterialViniloString es un dato obligatorio.");
        Assert.notNull(viniloDeCorteDTO.getCantidad(), "cantidadString es un dato obligatorio.");
    }

    private ViniloDeCorte devolverViniloDeCorteCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    ViniloDeCorte viniloDeCorte = new ViniloDeCorte();
                    viniloDeCorte.setOrdenTrabajo(ordenTrabajo);
                    return viniloDeCorte;
                });
    }

    public Optional<ViniloDeCorte> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return viniloDeCorteRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        ViniloDeCorte viniloDeCorte = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        viniloDeCorteRepository.deleteById(viniloDeCorte.getId());
    }
}
