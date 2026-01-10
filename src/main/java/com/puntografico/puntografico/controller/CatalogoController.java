package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.CatalogoDTO;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller @AllArgsConstructor
public class CatalogoController {

    private final OpcionesCatalogoService opcionesCatalogoService;
    private final MedioPagoService medioPagoService;
    private final OrdenCatalogoService ordenCatalogoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CatalogoService catalogoService;
    private final ProductoService productoService;
    private final PagoService pagoService;

    @GetMapping({"/crear-odt-catalogo", "/crear-odt-catalogo/{idOrden}"})
    public String verCrearOdtCatalogo(Model model, HttpSession session, @PathVariable(required = false) Long idOrden) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCatalogo ordenCatalogo = (idOrden != null) ? ordenCatalogoService.buscarPorOrdenId(idOrden) : null;
        OrdenTrabajo ordenTrabajo = (ordenCatalogo != null) ? ordenCatalogo.getOrdenTrabajo() : new OrdenTrabajo();
        Catalogo catalogo = (ordenCatalogo != null) ? ordenCatalogo.getCatalogo() : new Catalogo();


        List<TipoFazCatalogo> listaTipoFazCatalogo = opcionesCatalogoService.buscarTodosTipoFazCatalogo();
        List<TipoLaminadoCatalogo> listaTipoLaminadoCatalogo = opcionesCatalogoService.buscarTodosTipoLaminadoCatalogo();
        List<MedioPago> listaMediosDePago = medioPagoService.buscarTodos();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("catalogo", catalogo);
        model.addAttribute("listaTipoFazCatalogo", listaTipoFazCatalogo);
        model.addAttribute("listaTipoLaminadoCatalogo", listaTipoLaminadoCatalogo);
        model.addAttribute("listaMediosDePago", listaMediosDePago);

        return "crear-odt-catalogo";
    }

    @GetMapping("/mostrar-odt-catalogo/{ordenCatalogoId}")
    public String verOrdenCatalogo(@PathVariable("ordenCatalogoId") Long ordenCatalogoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");

        if (empleado == null) {
            return "redirect:/"; // Si no hay sesión, lo manda al login
        }

        OrdenCatalogo ordenCatalogo = ordenCatalogoService.buscarPorId(ordenCatalogoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenCatalogo.getOrdenTrabajo().getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenCatalogo.getOrdenTrabajo().getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenCatalogo.getOrdenTrabajo().getFechaPedido());

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenCatalogo", ordenCatalogo);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        return "mostrar-odt-catalogo";
    }

    @PostMapping("/api/creacion-catalogo")
    public String creacionCatalogo(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));

        OrdenCatalogo ordenCatalogoExistente = (idOrden != null) ? ordenCatalogoService.buscarPorOrdenId(idOrden) : null;
        Long idOrdenTrabajo = (ordenCatalogoExistente != null) ? ordenCatalogoExistente.getOrdenTrabajo().getId() : null;
        Long idCatalogo = (ordenCatalogoExistente != null) ? ordenCatalogoExistente.getCatalogo().getId() : null;
        Long idOrdenCatalogo = (ordenCatalogoExistente != null) ? ordenCatalogoExistente.getId() : null;

        CatalogoDTO catalogoDTO = armarCatalogoDTO(request);

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrdenTrabajo);
        pagoService.guardar(request, ordenTrabajo.getId());
        Catalogo catalogo = catalogoService.guardar(catalogoDTO, idCatalogo);
        OrdenCatalogo ordenCatalogo = ordenCatalogoService.guardar(ordenTrabajo, catalogo, idOrdenCatalogo);

        return "redirect:/mostrar-odt-catalogo/" + ordenCatalogo.getId();
    }

    private CatalogoDTO armarCatalogoDTO(HttpServletRequest request) {
        CatalogoDTO catalogoDTO = new CatalogoDTO();
        catalogoDTO.setTipoLaminadoCatalogoId(Long.parseLong(request.getParameter("tipoLaminadoCatalogo.id")));
        catalogoDTO.setTipoFazCatalogoId(Long.parseLong(request.getParameter("tipoFazCatalogo.id")));
        catalogoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        catalogoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        catalogoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        catalogoDTO.setTipoPapel(request.getParameter("tipoPapel"));
        catalogoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);

        return catalogoDTO;
    }

    @DeleteMapping("/api/eliminar-orden-catalogo/{idOrden}")
    public void eliminarOrdenCatalogo(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenCatalogo ordenCatalogo = ordenCatalogoService.buscarPorOrdenId(idOrden);
        Long idOrdenCatalogo = ordenCatalogo.getId();
        Long idOrdenTrabajo = ordenCatalogo.getOrdenTrabajo().getId();
        Long idCatalogo = ordenCatalogo.getCatalogo().getId();

        ordenCatalogoService.eliminar(idOrdenCatalogo);
        ordenTrabajoService.eliminar(idOrdenTrabajo);
        catalogoService.eliminar(idCatalogo);
    }
}
