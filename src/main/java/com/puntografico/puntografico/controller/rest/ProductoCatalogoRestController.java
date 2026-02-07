package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.domain.ConsultaPrecioDTO;
import com.puntografico.puntografico.service.ProductoCatalogoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogo")
@AllArgsConstructor
public class ProductoCatalogoRestController {

    private final ProductoCatalogoService productoCatalogoService;

    @PostMapping("/buscar-precio")
    public ResponseEntity<Integer> buscarPrecio(@RequestBody ConsultaPrecioDTO consulta) {
        Integer precio = productoCatalogoService.buscarPrecioCoincidente(
                consulta.getProductoId(),
                consulta.getDetalles()
        );

        if (precio != null) {
            return ResponseEntity.ok(precio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
