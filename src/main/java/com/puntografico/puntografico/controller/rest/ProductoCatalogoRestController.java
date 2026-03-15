package com.puntografico.puntografico.controller.rest;

import com.puntografico.puntografico.dto.ConsultaPrecioDTO;
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

        return ResponseEntity.ok(precio != null ? precio : 0);
    }
}
