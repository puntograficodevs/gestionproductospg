package com.puntografico.puntografico.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puntografico.puntografico.domain.ProductoCatalogo;
import com.puntografico.puntografico.repository.ProductoCatalogoRepostory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductoCatalogoService {

    private final ProductoCatalogoRepostory catalogoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Integer buscarPrecioCoincidente(Integer productoId, Map<String, String> detallesSeleccionados) {
        List<ProductoCatalogo> variantes = catalogoRepository.findByProductoId(productoId);

        for (ProductoCatalogo variante : variantes) {
            try {
                Map<String, String> mapaVariante = objectMapper.readValue(
                        variante.getDetallesProducto(),
                        new TypeReference<Map<String, String>>() {}
                );

                if (detallesSeleccionados.entrySet().containsAll(mapaVariante.entrySet())) {
                    return variante.getPrecio();
                }
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
    }

    public List<ProductoCatalogo> buscarTodasLasCopiasEscolaresEnCatalogo() {
        return catalogoRepository.findByProductoId(34);
    }
}
