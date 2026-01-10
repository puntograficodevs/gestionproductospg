package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CatalogoDTO;
import com.puntografico.puntografico.repository.CatalogoRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesCatalogoService opcionesCatalogoService;

    public Catalogo guardar(CatalogoDTO catalogoDTO, Long idOrdenTrabajo) {
        validarCatalogoDTO(catalogoDTO);
        Catalogo catalogo = devolverCatalogoCorrespondiente(idOrdenTrabajo);

        TipoFazCatalogo tipoFazCatalogo = opcionesCatalogoService.buscarTipoFazCatalogoPorId(catalogoDTO.getTipoFazCatalogoId());
        TipoLaminadoCatalogo tipoLaminadoCatalogo = opcionesCatalogoService.buscarTipoLaminadoCatalogoPorId(catalogoDTO.getTipoLaminadoCatalogoId());

        boolean adicionalDisenio = catalogoDTO.getConAdicionalDisenio();

        catalogo.setTipoPapel(catalogoDTO.getTipoPapel());
        catalogo.setTipoFazCatalogo(tipoFazCatalogo);
        catalogo.setTipoLaminadoCatalogo(tipoLaminadoCatalogo);
        catalogo.setEnlaceArchivo(catalogoDTO.getEnlaceArchivo());
        catalogo.setInformacionAdicional(catalogoDTO.getInformacionAdicional());
        catalogo.setConAdicionalDisenio(adicionalDisenio);
        catalogo.setCantidad(catalogoDTO.getCantidad());

        return catalogoRepository.save(catalogo);
    }

    private void validarCatalogoDTO(CatalogoDTO catalogoDTO) {
        Assert.notNull(catalogoDTO.getTipoPapel(), "El tipo de papel es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getTipoLaminadoCatalogoId(), "El tipo de laminado es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getTipoFazCatalogoId(), "El tipo de faz es un dato obligatorio.");
        Assert.notNull(catalogoDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private Catalogo devolverCatalogoCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Catalogo catalogoNuevo = new Catalogo();
                    catalogoNuevo.setOrdenTrabajo(ordenTrabajo);
                    return catalogoNuevo;
                });
    }

    public Optional<Catalogo> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return catalogoRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Catalogo catalogo = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        catalogoRepository.deleteById(catalogo.getId());
    }
}
