package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.*;
import com.puntografico.puntografico.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller @AllArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final AgendaService agendaService;
    private final AnotadorService anotadorService;
    private final CarpetaSolapaService carpetaSolapaService;
    private final CatalogoService catalogoService;
    private final CierraBolsasService cierraBolsasService;
    private final ComboService comboService;
    private final CuadernoAnilladoService cuadernoAnilladoService;
    private final EntradaService entradaService;
    private final EtiquetaService etiquetaService;
    private final FlybannerService flybannerService;
    private final FolletoService folletoService;
    private final HojasMembreteadasService hojasMembreteadasService;
    private final ImpresionService impresionService;
    private final LonaComunService lonaComunService;
    private final LonaPublicitariaService lonaPublicitariaService;
    private final OtroService otroService;
    private final RifasBonosContribucionService rifasBonosContribucionService;
    private final RotulacionService rotulacionService;
    private final SelloAutomaticoService selloAutomaticoService;
    private final SelloAutomaticoEscolarService selloAutomaticoEscolarService;
    private final SelloMaderaService selloMaderaService;
    private final SobreService sobreService;
    private final StickerService stickerService;
    private final SublimacionService sublimacionService;
    private final TalonarioService talonarioService;
    private final TurneroService turneroService;
    private final TarjetaService tarjetaService;
    private final ViniloService viniloService;
    private final ViniloDeCorteService viniloDeCorteService;
    private final ViniloPlasticoCorrugadoService viniloPlasticoCorrugadoService;
    private final VoucherService voucherService;
    private final PagoService pagoService;

    @PostMapping("/api/creacion-producto")
    public String creacionProducto(HttpServletRequest request) {
        Long idOrden = productoService.buscarOrdenIdSiExiste(request.getParameter("idOrden"));
        String tipoProducto = request.getParameter("tipoProducto");

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.guardar(request, idOrden);
        pagoService.guardar(request, ordenTrabajo.getId());
        crearProductoCorrespondiente(request, tipoProducto, ordenTrabajo.getId());

        return "redirect:/mostrar-odt-producto/" + ordenTrabajo.getId();
    }

    private void crearProductoCorrespondiente(HttpServletRequest request, String tipoProducto, Long idOrden) {
        Assert.notNull(tipoProducto, "El tipo de producto no puede ser nulo.");

        switch (tipoProducto) {
            case "agenda":
                AgendaDTO agendaDTO = armarAgendaDTO(request);
                agendaService.guardar(agendaDTO, idOrden);
                break;
            case "anotador":
                AnotadorDTO anotadorDTO = armarAnotadorDTO(request);
                anotadorService.guardar(anotadorDTO, idOrden);
                break;
            case "carpeta con solapas":
                CarpetaSolapaDTO carpetaSolapaDTO = armarCarpetaSolapaDTO(request);
                carpetaSolapaService.guardar(carpetaSolapaDTO, idOrden);
                break;
            case "catálogo":
                CatalogoDTO catalogoDTO = armarCatalogoDTO(request);
                catalogoService.guardar(catalogoDTO, idOrden);
                break;
            case "cierra bolsas":
                CierraBolsasDTO cierraBolsasDTO = armarCierraBolsasDTO(request);
                cierraBolsasService.guardar(cierraBolsasDTO, idOrden);
                break;
            case "combo":
                ComboDTO comboDTO = armarComboDTO(request);
                comboService.guardar(comboDTO, idOrden);
                break;
            case "cuaderno anillado":
                CuadernoAnilladoDTO cuadernoAnilladoDTO = armarCuadernoAnilladoDTO(request);
                cuadernoAnilladoService.guardar(cuadernoAnilladoDTO, idOrden);
                break;
            case "entrada":
                EntradaDTO entradaDTO = armarEntradaDTO(request);
                entradaService.guardar(entradaDTO, idOrden);
                break;
            case "etiqueta":
                EtiquetaDTO etiquetaDTO = armarEtiquetaDTO(request);
                etiquetaService.guardar(etiquetaDTO, idOrden);
                break;
            case "flybanner":
                FlybannerDTO flybannerDTO = armarFlybannerDTO(request);
                flybannerService.guardar(flybannerDTO, idOrden);
                break;
            case "folleto":
                FolletoDTO folletoDTO = armarFolletoDTO(request);
                folletoService.guardar(folletoDTO, idOrden);
                break;
            case "hojas membretadas":
                HojasMembreteadasDTO hojasMembreteadasDTO = armarHojasMembreteadasDTO(request);
                hojasMembreteadasService.guardar(hojasMembreteadasDTO, idOrden);
                break;
            case "impresion":
                ImpresionDTO impresionDTO = armarImpresionDTO(request);
                impresionService.guardar(impresionDTO, idOrden);
                break;
            case "lona común":
                LonaComunDTO lonaComunDTO = armarLonaComunDTO(request);
                lonaComunService.guardar(lonaComunDTO, idOrden);
                break;
            case "lona publicitaria":
                LonaPublicitariaDTO lonaPublicitariaDTO = armarLonaPubliciatariaDTO(request);
                lonaPublicitariaService.guardar(lonaPublicitariaDTO, idOrden);
                break;
            case "sin categoría":
                OtroDTO otroDTO = armarOtroDTO(request);
                otroService.guardar(otroDTO, idOrden);
                break;
            case "rifa o bono":
                RifasBonosContribucionDTO rifasBonosContribucionDTO = armarRifasBonosContribucionDTO(request);
                rifasBonosContribucionService.guardar(rifasBonosContribucionDTO, idOrden);
                break;
            case "rotulación":
                RotulacionDTO rotulacionDTO = armarRotulacionDTO(request);
                rotulacionService.guardar(rotulacionDTO, idOrden);
                break;
            case "sello automático":
                SelloAutomaticoDTO selloAutomaticoDTO = armarSelloAutomaticoDTO(request);
                selloAutomaticoService.guardar(selloAutomaticoDTO, idOrden);
                break;
            case "sello automático escolar":
                SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO = armarSelloAutomaticoEscolarDTO(request);
                selloAutomaticoEscolarService.guardar(selloAutomaticoEscolarDTO, idOrden);
                break;
            case "sello de madera":
                SelloMaderaDTO selloMaderaDTO = armarSelloMaderaDTO(request);
                selloMaderaService.guardar(selloMaderaDTO, idOrden);
                break;
            case "sobre":
                SobreDTO sobreDTO = armarSobreDTO(request);
                sobreService.guardar(sobreDTO, idOrden);
                break;
            case "sticker":
                StickerDTO stickerDTO = armarStickerDTO(request);
                stickerService.guardar(stickerDTO, idOrden);
                break;
            case "sublimación":
                SublimacionDTO sublimacionDTO = armarSublimacionDTO(request);
                sublimacionService.guardar(sublimacionDTO, idOrden);
                break;
            case "talonario":
                TalonarioDTO talonarioDTO = armarTalonarioDTO(request);
                talonarioService.guardar(talonarioDTO, idOrden);
                break;
            case "tarjeta":
                TarjetaDTO tarjetaDTO = armarTarjetaDTO(request);
                tarjetaService.guardar(tarjetaDTO, idOrden);
                break;
            case "turnero":
                TurneroDTO turneroDTO = armarTurneroDTO(request);
                turneroService.guardar(turneroDTO, idOrden);
                break;
            case "vinilo":
                ViniloDTO viniloDTO = armarViniloDTO(request);
                viniloService.guardar(viniloDTO, idOrden);
                break;
            case "vinilo de corte":
                ViniloDeCorteDTO viniloDeCorteDTO = armarViniloDeCorteDTO(request);
                viniloDeCorteService.guardar(viniloDeCorteDTO, idOrden);
                break;
            case "vinilo con plástico corrugado":
                ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO = armarViniloPlasticoCorrugadoDTO(request);
                viniloPlasticoCorrugadoService.guardar(viniloPlasticoCorrugadoDTO, idOrden);
                break;
            case "voucher":
                VoucherDTO voucherDTO = armarVoucherDTO(request);
                voucherService.guardar(voucherDTO, idOrden);
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }
    }

    @GetMapping("/mostrar-odt-producto/{ordenTrabajoId}")
    public String verOrdenProducto(@PathVariable("ordenTrabajoId") Long ordenTrabajoId, Model model, HttpSession session) {
        Empleado empleado = (Empleado) session.getAttribute("empleadoLogueado");
        String htmlRedireccion;

        if (empleado == null) {
            return "redirect:/";
        }

        OrdenTrabajo ordenTrabajo = ordenTrabajoService.buscarPorId(ordenTrabajoId);
        String fechaEntrega = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaEntrega());
        String fechaMuestra = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaMuestra());
        String fechaPedido = ordenTrabajoService.formatearFecha(ordenTrabajo.getFechaPedido());
        String tipoProducto = ordenTrabajo.getTipoProducto();

        model.addAttribute("empleado", empleado);
        model.addAttribute("ordenTrabajo", ordenTrabajo);
        model.addAttribute("fechaEntrega", fechaEntrega);
        model.addAttribute("fechaMuestra", fechaMuestra);
        model.addAttribute("fechaPedido", fechaPedido);

        switch(tipoProducto) {
            case "agenda":
                Agenda agenda = agendaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("agenda", agenda);
                htmlRedireccion = "mostrar-odt-agenda";
                break;
            case "anotador":
                Anotador anotador = anotadorService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("anotador", anotador);
                htmlRedireccion = "mostrar-odt-anotador";
                break;
            case "carpeta con solapas":
                CarpetaSolapa carpetaSolapa = carpetaSolapaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("carpetaSolapa", carpetaSolapa);
                htmlRedireccion = "mostrar-odt-carpeta-solapa";
                break;
            case "catálogo":
                Catalogo catalogo = catalogoService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("catalogo", catalogo);
                htmlRedireccion = "mostrar-odt-catalogo";
                break;
            case "cierra bolsas":
                CierraBolsas cierraBolsas = cierraBolsasService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("cierraBolsas", cierraBolsas);
                htmlRedireccion = "mostrar-odt-cierra-bolsas";
                break;
            case "combo":
                Combo combo = comboService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("combo", combo);
                htmlRedireccion = "mostrar-odt-combo";
                break;
            case "cuaderno anillado":
                CuadernoAnillado cuadernoAnillado = cuadernoAnilladoService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("cuadernoAnillado", cuadernoAnillado);
                htmlRedireccion = "mostrar-odt-cuaderno-anillado";
                break;
            case "entrada":
                Entrada entrada = entradaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("entrada", entrada);
                htmlRedireccion = "mostrar-odt-entrada";
                break;
            case "etiqueta":
                Etiqueta etiqueta = etiquetaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("etiqueta", etiqueta);
                htmlRedireccion = "mostrar-odt-etiqueta";
                break;
            case "flybanner":
                Flybanner flybanner = flybannerService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("flybanner", flybanner);
                htmlRedireccion = "mostrar-odt-flybanner";
                break;
            case "folleto":
                Folleto folleto = folletoService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("folleto", folleto);
                htmlRedireccion = "mostrar-odt-folleto";
                break;
            case "hojas membretadas":
                HojasMembreteadas hojasMembreteadas = hojasMembreteadasService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("hojasMembreteadas", hojasMembreteadas);
                htmlRedireccion = "mostrar-odt-hojas-membreteadas";
                break;
            case "impresion":
                Impresion impresion = impresionService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("impresion", impresion);
                htmlRedireccion = "mostrar-odt-impresion";
                break;
            case "lona común":
                LonaComun lonaComun = lonaComunService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("lonaComun", lonaComun);
                htmlRedireccion = "mostrar-odt-lona-comun";
                break;
            case "lona publicitaria":
                LonaPublicitaria lonaPublicitaria = lonaPublicitariaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("lonaPublicitaria", lonaPublicitaria);
                htmlRedireccion = "mostrar-odt-lona-publicitaria";
                break;
            case "sin categoría":
                Otro otro = otroService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("otro", otro);
                htmlRedireccion = "mostrar-odt-otro";
                break;
            case "rifa o bono":
                RifasBonosContribucion rifasBonosContribucion = rifasBonosContribucionService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("rifasBonosContribucion", rifasBonosContribucion);
                htmlRedireccion = "mostrar-odt-rifas-bonos-contribucion";
                break;
            case "rotulación":
                Rotulacion rotulacion = rotulacionService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("rotulacion", rotulacion);
                htmlRedireccion = "mostrar-odt-rotulacion";
                break;
            case "sello automático":
                SelloAutomatico selloAutomatico = selloAutomaticoService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("selloAutomatico", selloAutomatico);
                htmlRedireccion = "mostrar-odt-sello-automatico";
                break;
            case "sello automático escolar":
                SelloAutomaticoEscolar selloAutomaticoEscolar = selloAutomaticoEscolarService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("selloAutomaticoEscolar", selloAutomaticoEscolar);
                htmlRedireccion = "mostrar-odt-sello-automatico-escolar";
                break;
            case "sello de madera":
                SelloMadera selloMadera = selloMaderaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("selloMadera", selloMadera);
                htmlRedireccion = "mostrar-odt-sello-madera";
                break;
            case "sobre":
                Sobre sobre = sobreService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("sobre", sobre);
                htmlRedireccion = "mostrar-odt-sobre";
                break;
            case "sticker":
                Sticker sticker = stickerService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("sticker", sticker);
                htmlRedireccion = "mostrar-odt-sticker";
                break;
            case "sublimación":
                Sublimacion sublimacion = sublimacionService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("sublimacion", sublimacion);
                htmlRedireccion = "mostrar-odt-sublimacion";
                break;
            case "talonario":
                Talonario talonario = talonarioService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("talonario", talonario);
                htmlRedireccion = "mostrar-odt-talonario";
                break;
            case "tarjeta":
                Tarjeta tarjeta = tarjetaService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("tarjeta", tarjeta);
                htmlRedireccion = "mostrar-odt-tarjeta";
                break;
            case "turnero":
                Turnero turnero = turneroService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("turnero", turnero);
                htmlRedireccion = "mostrar-odt-turnero";
                break;
            case "vinilo":
                Vinilo vinilo = viniloService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("vinilo", vinilo);
                htmlRedireccion = "mostrar-odt-vinilo";
                break;
            case "vinilo de corte":
                ViniloDeCorte viniloDeCorte = viniloDeCorteService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("viniloDeCorte", viniloDeCorte);
                htmlRedireccion = "mostrar-odt-vinilo-de-corte";
                break;
            case "vinilo con plástico corrugado":
                ViniloPlasticoCorrugado viniloPlasticoCorrugado = viniloPlasticoCorrugadoService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("viniloPlasticoCorrugado", viniloPlasticoCorrugado);
                htmlRedireccion = "mostrar-odt-vinilo-plastico-corrugado";
                break;
            case "voucher":
                Voucher voucher = voucherService.buscarPorOrdenTrabajoId(ordenTrabajoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado para la orden"));
                model.addAttribute("voucher", voucher);
                htmlRedireccion = "mostrar-odt-voucher";
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }

        return htmlRedireccion;
    }

    @DeleteMapping("/api/eliminar-producto/{idOrden}")
    @ResponseBody
    public void eliminarProducto(Model model, HttpSession session, @PathVariable Long idOrden) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoService.buscarPorId(idOrden);
        String tipoProducto = ordenTrabajo.getTipoProducto();

        switch (tipoProducto) {
            case "agenda":
                agendaService.eliminar(ordenTrabajo.getId());
                break;
            case "anotador":
                anotadorService.eliminar(ordenTrabajo.getId());
                break;
            case "carpeta con solapas":
                carpetaSolapaService.eliminar(ordenTrabajo.getId());
                break;
            case "catálogo":
                catalogoService.eliminar(ordenTrabajo.getId());
                break;
            case "cierra bolsas":
                cierraBolsasService.eliminar(ordenTrabajo.getId());
                break;
            case "combo":
                comboService.eliminar(ordenTrabajo.getId());
                break;
            case "cuaderno anillado":
                cuadernoAnilladoService.eliminar(ordenTrabajo.getId());
                break;
            case "entrada":
                entradaService.eliminar(ordenTrabajo.getId());
                break;
            case "etiqueta":
                etiquetaService.eliminar(ordenTrabajo.getId());
                break;
            case "flybanner":
                flybannerService.eliminar(ordenTrabajo.getId());
                break;
            case "folleto":
                folletoService.eliminar(ordenTrabajo.getId());
                break;
            case "hojas membretadas":
                hojasMembreteadasService.eliminar(ordenTrabajo.getId());
                break;
            case "impresion":
                impresionService.eliminar(ordenTrabajo.getId());
                break;
            case "lona común":
                lonaComunService.eliminar(ordenTrabajo.getId());
                break;
            case "lona publicitaria":
                lonaPublicitariaService.eliminar(ordenTrabajo.getId());
                break;
            case "sin categoría":
                otroService.eliminar(ordenTrabajo.getId());
                break;
            case "rifa o bono":
                rifasBonosContribucionService.eliminar(ordenTrabajo.getId());
                break;
            case "rotulación":
                rotulacionService.eliminar(ordenTrabajo.getId());
                break;
            case "sello automático":
                selloAutomaticoService.eliminar(ordenTrabajo.getId());
                break;
            case "sello automático escolar":
                selloAutomaticoEscolarService.eliminar(ordenTrabajo.getId());
                break;
            case "sello de madera":
                selloMaderaService.eliminar(ordenTrabajo.getId());
                break;
            case "sobre":
                sobreService.eliminar(ordenTrabajo.getId());
                break;
            case "sticker":
                stickerService.eliminar(ordenTrabajo.getId());
                break;
            case "sublimación":
                sublimacionService.eliminar(ordenTrabajo.getId());
                break;
            case "talonario":
                talonarioService.eliminar(ordenTrabajo.getId());
                break;
            case "tarjeta":
                tarjetaService.eliminar(ordenTrabajo.getId());
                break;
            case "turnero":
                turneroService.eliminar(ordenTrabajo.getId());
                break;
            case "vinilo":
                viniloService.eliminar(ordenTrabajo.getId());
                break;
            case "vinilo de corte":
                viniloDeCorteService.eliminar(ordenTrabajo.getId());
                break;
            case "vinilo con plástico corrugado":
                viniloPlasticoCorrugadoService.eliminar(ordenTrabajo.getId());
                break;
            case "voucher":
                voucherService.eliminar(ordenTrabajo.getId());
                break;
            default:
                throw new IllegalStateException("Tipo de producto inesperado: " + tipoProducto);
        }

        ordenTrabajoService.eliminar(ordenTrabajo.getId());
    }

    private AgendaDTO armarAgendaDTO(HttpServletRequest request) {
        AgendaDTO agendaDTO = new AgendaDTO();
        agendaDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        agendaDTO.setTipoTapaAgendaId(Long.parseLong(request.getParameter("tipoTapaAgenda.id")));
        agendaDTO.setTipoColorAgendaId(Long.parseLong(request.getParameter("tipoColorAgenda.id")));
        agendaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        agendaDTO.setMedida(request.getParameter("medida"));
        agendaDTO.setTipoTapaPersonalizada(request.getParameter("tipoTapaPersonalizada"));
        agendaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        agendaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        agendaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));

        return agendaDTO;
    }

    private AnotadorDTO armarAnotadorDTO(HttpServletRequest request) {
        AnotadorDTO anotadorDTO = new AnotadorDTO();
        anotadorDTO.setMedida(request.getParameter("medida"));
        anotadorDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        anotadorDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        anotadorDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        anotadorDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        anotadorDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        anotadorDTO.setTipoTapa(request.getParameter("tipoTapa"));

        return anotadorDTO;
    }

    private CarpetaSolapaDTO armarCarpetaSolapaDTO(HttpServletRequest request) {
        CarpetaSolapaDTO carpetaSolapaDTO = new CarpetaSolapaDTO();
        carpetaSolapaDTO.setTipoFazCarpetaSolapaId(Long.parseLong(request.getParameter("tipoFazCarpetaSolapa.id")));
        carpetaSolapaDTO.setTipoLaminadoCarpetaSolapaId(Long.parseLong(request.getParameter("tipoLaminadoCarpetaSolapa.id")));
        carpetaSolapaDTO.setTipoPapel(request.getParameter("tipoPapel"));
        carpetaSolapaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        carpetaSolapaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        carpetaSolapaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        carpetaSolapaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);

        return carpetaSolapaDTO;
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

    private CierraBolsasDTO armarCierraBolsasDTO(HttpServletRequest request) {
        CierraBolsasDTO cierraBolsasDTO = new CierraBolsasDTO();
        cierraBolsasDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        cierraBolsasDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        cierraBolsasDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        cierraBolsasDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        cierraBolsasDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        cierraBolsasDTO.setTipoTroqueladoCierraBolsasId(Long.parseLong(request.getParameter("tipoTroqueladoCierraBolsas.id")));
        cierraBolsasDTO.setCantidadCierraBolsasId(Long.parseLong(request.getParameter("cantidadCierraBolsas.id")));
        cierraBolsasDTO.setMedidaCierraBolsasId(Long.parseLong(request.getParameter("medidaCierraBolsas.id")));

        return cierraBolsasDTO;
    }

    private ComboDTO armarComboDTO(HttpServletRequest request) {
        ComboDTO comboDTO = new ComboDTO();
        comboDTO.setTipoComboId(Long.parseLong(request.getParameter("tipoCombo.id")));
        comboDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        comboDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        comboDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        comboDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);

        return comboDTO;
    }

    private CuadernoAnilladoDTO armarCuadernoAnilladoDTO(HttpServletRequest request) {
        CuadernoAnilladoDTO cuadernoAnilladoDTO = new CuadernoAnilladoDTO();
        cuadernoAnilladoDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        cuadernoAnilladoDTO.setTipoTapaPersonalizada(request.getParameter("tipoTapaPersonalizada"));
        cuadernoAnilladoDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        cuadernoAnilladoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        cuadernoAnilladoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        cuadernoAnilladoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        cuadernoAnilladoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        cuadernoAnilladoDTO.setMedidaCuadernoAnilladoId(Long.parseLong(request.getParameter("medidaCuadernoAnillado.id")));
        cuadernoAnilladoDTO.setTipoTapaCuadernoAnilladoId(Long.parseLong(request.getParameter("tipoTapaCuadernoAnillado.id")));

        return cuadernoAnilladoDTO;
    }

    private EntradaDTO armarEntradaDTO(HttpServletRequest request) {
        EntradaDTO entradaDTO = new EntradaDTO();
        entradaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        entradaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        entradaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        entradaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        entradaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        entradaDTO.setTipoPapelEntradaId(Long.parseLong(request.getParameter("tipoPapelEntrada.id")));
        entradaDTO.setTipoColorEntradaId(Long.parseLong(request.getParameter("tipoColorEntrada.id")));
        entradaDTO.setTipoTroqueladoEntradaId(Long.parseLong(request.getParameter("tipoTroqueladoEntrada.id")));
        entradaDTO.setMedidaEntradaId(Long.parseLong(request.getParameter("medidaEntrada.id")));
        entradaDTO.setCantidadEntradaId(Long.parseLong(request.getParameter("cantidadEntrada.id")));
        entradaDTO.setNumeradoEntradaId(Long.parseLong(request.getParameter("numeradoEntrada.id")));
        entradaDTO.setTerminacionEntradaId(Long.parseLong(request.getParameter("terminacionEntrada.id")));

        return entradaDTO;
    }

    private EtiquetaDTO armarEtiquetaDTO(HttpServletRequest request) {
        EtiquetaDTO etiquetaDTO = new EtiquetaDTO();
        etiquetaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        etiquetaDTO.setConPerforacionAdicional(request.getParameter("conPerforacionAdicional") != null);
        etiquetaDTO.setConMarcaAdicional(request.getParameter("conMarcaAdicional") != null);
        etiquetaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        etiquetaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        etiquetaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        etiquetaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        etiquetaDTO.setTipoPapelEtiquetaId(Long.parseLong(request.getParameter("tipoPapelEtiqueta.id")));
        etiquetaDTO.setTipoLaminadoEtiquetaId(Long.parseLong(request.getParameter("tipoLaminadoEtiqueta.id")));
        etiquetaDTO.setTamanioPerforacionId(Long.parseLong(request.getParameter("tamanioPerforacion.id")));
        etiquetaDTO.setTipoFazEtiquetaId(Long.parseLong(request.getParameter("tipoFazEtiqueta.id")));
        etiquetaDTO.setCantidadEtiquetaId(Long.parseLong(request.getParameter("cantidadEtiqueta.id")));
        etiquetaDTO.setMedidaEtiquetaId(Long.parseLong(request.getParameter("medidaEtiqueta.id")));

        return etiquetaDTO;
    }

    private FlybannerDTO armarFlybannerDTO(HttpServletRequest request) {
        FlybannerDTO flybannerDTO = new FlybannerDTO();
        flybannerDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        flybannerDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        flybannerDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        flybannerDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        flybannerDTO.setTipoFazFlybannerId(Long.parseLong(request.getParameter("tipoFazFlybanner.id")));
        flybannerDTO.setAlturaFlybannerId(Long.parseLong(request.getParameter("alturaFlybanner.id")));
        flybannerDTO.setBanderaFlybannerId(Long.parseLong(request.getParameter("banderaFlybanner.id")));
        flybannerDTO.setTipoBaseFlybannerId(Long.parseLong(request.getParameter("tipoBaseFlybanner.id")));

        return flybannerDTO;
    }

    private FolletoDTO armarFolletoDTO(HttpServletRequest request) {
        FolletoDTO folletoDTO = new FolletoDTO();
        folletoDTO.setConPlegado(request.getParameter("conPlegado") != null);
        folletoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        folletoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        folletoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        folletoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        folletoDTO.setTipoPapelFolletoId(Long.parseLong(request.getParameter("tipoPapelFolleto.id")));
        folletoDTO.setTipoColorFolletoId(Long.parseLong(request.getParameter("tipoColorFolleto.id")));
        folletoDTO.setTipoFazFolletoId(Long.parseLong(request.getParameter("tipoFazFolleto.id")));
        folletoDTO.setTamanioHojaFolletoId(Long.parseLong(request.getParameter("tamanioHojaFolleto.id")));
        folletoDTO.setTipoFolletoId(Long.parseLong(request.getParameter("tipoFolleto.id")));
        folletoDTO.setCantidadFolletoId(Long.parseLong(request.getParameter("cantidadFolleto.id")));

        return folletoDTO;
    }

    private HojasMembreteadasDTO armarHojasMembreteadasDTO(HttpServletRequest request) {
        HojasMembreteadasDTO hojasMembreteadasDTO = new HojasMembreteadasDTO();
        hojasMembreteadasDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        hojasMembreteadasDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        hojasMembreteadasDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        hojasMembreteadasDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        hojasMembreteadasDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        hojasMembreteadasDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        hojasMembreteadasDTO.setMedidaHojasMembreteadasId(Long.parseLong(request.getParameter("medidaHojasMembreteadas.id")));
        hojasMembreteadasDTO.setTipoColorHojasMembreteadasId(Long.parseLong(request.getParameter("tipoColorHojasMembreteadas.id")));
        hojasMembreteadasDTO.setCantidadHojasMembreteadasId(Long.parseLong(request.getParameter("cantidadHojasMembreteadas.id")));

        return hojasMembreteadasDTO;
    }

    private ImpresionDTO armarImpresionDTO(HttpServletRequest request) {
        ImpresionDTO impresionDTO = new ImpresionDTO();
        impresionDTO.setEsAnillado(request.getParameter("esAnillado") != null);
        impresionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        impresionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        impresionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        impresionDTO.setTipoColorImpresionId(Long.parseLong(request.getParameter("tipoColorImpresion.id")));
        impresionDTO.setTamanioHojaImpresionId(Long.parseLong(request.getParameter("tamanioHojaImpresion.id")));
        impresionDTO.setTipoFazImpresionId(Long.parseLong(request.getParameter("tipoFazImpresion.id")));
        impresionDTO.setTipoPapelImpresionId(Long.parseLong(request.getParameter("tipoPapelImpresion.id")));
        impresionDTO.setCantidadImpresionId(Long.parseLong(request.getParameter("cantidadImpresion.id")));
        impresionDTO.setTipoImpresionId(Long.parseLong(request.getParameter("tipoImpresion.id")));

        return impresionDTO;
    }

    private LonaComunDTO armarLonaComunDTO(HttpServletRequest request) {
        LonaComunDTO lonaComunDTO = new LonaComunDTO();
        lonaComunDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        lonaComunDTO.setConOjales(request.getParameter("conOjales") != null);
        lonaComunDTO.setConOjalesConRefuerzo(request.getParameter("conOjalesConRefuerzo") != null);
        lonaComunDTO.setConBolsillos(request.getParameter("conBolsillos") != null);
        lonaComunDTO.setConDemasiaParaTensado(request.getParameter("conDemasiaParaTensado") != null);
        lonaComunDTO.setConSolapado(request.getParameter("conSolapado") != null);
        lonaComunDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        lonaComunDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        lonaComunDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        lonaComunDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        lonaComunDTO.setMedidaLonaComunId(Long.parseLong(request.getParameter("medidaLonaComun.id")));
        lonaComunDTO.setTipoLonaComunId(Long.parseLong(request.getParameter("tipoLonaComun.id")));

        return lonaComunDTO;
    }

    private LonaPublicitariaDTO armarLonaPubliciatariaDTO(HttpServletRequest request) {
        LonaPublicitariaDTO lonaPublicitariaDTO = new LonaPublicitariaDTO();
        lonaPublicitariaDTO.setConAdicionalPortabanner(request.getParameter("conAdicionalPortabanner") != null);
        lonaPublicitariaDTO.setConOjales(request.getParameter("conOjales") != null);
        lonaPublicitariaDTO.setConOjalesConRefuerzo(request.getParameter("conOjalesConRefuerzo") != null);
        lonaPublicitariaDTO.setConBolsillos(request.getParameter("conBolsillos") != null);
        lonaPublicitariaDTO.setConDemasiaParaTensado(request.getParameter("conDemasiaParaTensado") != null);
        lonaPublicitariaDTO.setConSolapado(request.getParameter("conSolapado") != null);
        lonaPublicitariaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        lonaPublicitariaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        lonaPublicitariaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        lonaPublicitariaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        lonaPublicitariaDTO.setMedidaLonaPublicitariaId(Long.parseLong(request.getParameter("medidaLonaPublicitaria.id")));
        lonaPublicitariaDTO.setTipoLonaPublicitariaId(Long.parseLong(request.getParameter("tipoLonaPublicitaria.id")));

        return lonaPublicitariaDTO;
    }

    private OtroDTO armarOtroDTO(HttpServletRequest request) {
        OtroDTO otroDTO = new OtroDTO();
        otroDTO.setMedida(request.getParameter("medida"));
        otroDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        otroDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        otroDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        otroDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        otroDTO.setTipoColorOtroId(Long.parseLong(request.getParameter("tipoColorOtro.id")));

        return otroDTO;
    }

    private RifasBonosContribucionDTO armarRifasBonosContribucionDTO(HttpServletRequest request) {
        RifasBonosContribucionDTO rifasBonosContribucionDTO = new RifasBonosContribucionDTO();
        rifasBonosContribucionDTO.setConNumerado(request.getParameter("conNumerado") != null);
        rifasBonosContribucionDTO.setDetalleNumerado(request.getParameter("detalleNumerado"));
        rifasBonosContribucionDTO.setConEncolado(request.getParameter("conEncolado") != null);
        rifasBonosContribucionDTO.setMedida(request.getParameter("medida"));
        rifasBonosContribucionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        rifasBonosContribucionDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        rifasBonosContribucionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        rifasBonosContribucionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        rifasBonosContribucionDTO.setTipoPapelRifaId(Long.parseLong(request.getParameter("tipoPapelRifa.id")));
        rifasBonosContribucionDTO.setTipoTroqueladoRifaId(Long.parseLong(request.getParameter("tipoTroqueladoRifa.id")));
        rifasBonosContribucionDTO.setTipoColorRifaId(Long.parseLong(request.getParameter("tipoColorRifa.id")));

        return rifasBonosContribucionDTO;
    }

    private RotulacionDTO armarRotulacionDTO(HttpServletRequest request) {
        RotulacionDTO rotulacionDTO = new RotulacionDTO();
        rotulacionDTO.setEsLaminado(request.getParameter("esLaminado") != null);
        rotulacionDTO.setHorarioRotulacion(request.getParameter("horarioRotulacion"));
        rotulacionDTO.setDireccionRotulacion(request.getParameter("direccionRotulacion"));
        rotulacionDTO.setMedida(request.getParameter("medida"));
        rotulacionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        rotulacionDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        rotulacionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        rotulacionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        rotulacionDTO.setTipoRotulacionId(Long.parseLong(request.getParameter("tipoRotulacion.id")));
        rotulacionDTO.setTipoCorteRotulacionId(Long.parseLong(request.getParameter("tipoCorteRotulacion.id")));

        return rotulacionDTO;
    }

    private SelloAutomaticoDTO armarSelloAutomaticoDTO(HttpServletRequest request) {
        SelloAutomaticoDTO selloAutomaticoDTO = new SelloAutomaticoDTO();
        selloAutomaticoDTO.setEsProfesional(request.getParameter("esProfesional") != null);
        selloAutomaticoDTO.setEsParticular(request.getParameter("esParticular") != null);
        selloAutomaticoDTO.setTextoLineaUno(request.getParameter("textoLineaUno"));
        selloAutomaticoDTO.setTextoLineaDos(request.getParameter("textoLineaDos"));
        selloAutomaticoDTO.setTextoLineaTres(request.getParameter("textoLineaTres"));
        selloAutomaticoDTO.setTextoLineaCuatro(request.getParameter("textoLineaCuatro"));
        selloAutomaticoDTO.setTipografiaLineaUno(request.getParameter("tipografiaLineaUno"));
        selloAutomaticoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloAutomaticoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloAutomaticoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloAutomaticoDTO.setModeloSelloAutomaticoId(Long.parseLong(request.getParameter("modeloSelloAutomatico.id")));

        return selloAutomaticoDTO;
    }

    private SelloAutomaticoEscolarDTO armarSelloAutomaticoEscolarDTO(HttpServletRequest request) {
        SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO = new SelloAutomaticoEscolarDTO();
        selloAutomaticoEscolarDTO.setTextoLineaUno(request.getParameter("textoLineaUno"));
        selloAutomaticoEscolarDTO.setTextoLineaDos(request.getParameter("textoLineaDos"));
        selloAutomaticoEscolarDTO.setTextoLineaTres(request.getParameter("textoLineaTres"));
        selloAutomaticoEscolarDTO.setTipografia(request.getParameter("tipografia"));
        selloAutomaticoEscolarDTO.setDibujo(request.getParameter("dibujo"));
        selloAutomaticoEscolarDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloAutomaticoEscolarDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        selloAutomaticoEscolarDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloAutomaticoEscolarDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloAutomaticoEscolarDTO.setModeloSelloAutomaticoEscolarId(Long.parseLong(request.getParameter("modeloSelloAutomaticoEscolar.id")));

        return selloAutomaticoEscolarDTO;
    }

    private SelloMaderaDTO armarSelloMaderaDTO(HttpServletRequest request) {
        SelloMaderaDTO selloMaderaDTO = new SelloMaderaDTO();
        selloMaderaDTO.setTamanioPersonalizado(request.getParameter("tamanioPersonalizado"));
        selloMaderaDTO.setConAdicionalPerilla(request.getParameter("conAdicionalPerilla") != null);
        selloMaderaDTO.setDetalleSello(request.getParameter("detalleSello"));
        selloMaderaDTO.setTipografiaLineaUno(request.getParameter("tipografiaLineaUno"));
        selloMaderaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        selloMaderaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        selloMaderaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        selloMaderaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        selloMaderaDTO.setTamanioSelloMaderaId(Long.parseLong(request.getParameter("tamanioSelloMadera.id")));

        return selloMaderaDTO;
    }

    private SobreDTO armarSobreDTO(HttpServletRequest request) {
        SobreDTO sobreDTO = new SobreDTO();
        sobreDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        sobreDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        sobreDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        sobreDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        sobreDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        sobreDTO.setMedidaSobreId(Long.parseLong(request.getParameter("medidaSobre.id")));
        sobreDTO.setTipoColorSobreId(Long.parseLong(request.getParameter("tipoColorSobre.id")));
        sobreDTO.setCantidadSobreId(Long.parseLong(request.getParameter("cantidadSobre.id")));

        return sobreDTO;
    }

    private StickerDTO armarStickerDTO(HttpServletRequest request) {
        StickerDTO stickerDTO = new StickerDTO();
        stickerDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        stickerDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        stickerDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        stickerDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        stickerDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        stickerDTO.setTipoTroqueladoStickerId(Long.parseLong(request.getParameter("tipoTroqueladoSticker.id")));
        stickerDTO.setCantidadStickerId(Long.parseLong(request.getParameter("cantidadSticker.id")));
        stickerDTO.setMedidaStickerId(Long.parseLong(request.getParameter("medidaSticker.id")));

        return stickerDTO;
    }

    private SublimacionDTO armarSublimacionDTO(HttpServletRequest request) {
        SublimacionDTO sublimacionDTO = new SublimacionDTO();
        sublimacionDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        sublimacionDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        sublimacionDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        sublimacionDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        sublimacionDTO.setMaterialSublimacionId(Long.parseLong(request.getParameter("materialSublimacion.id")));
        sublimacionDTO.setCantidadSublimacionId(Long.parseLong(request.getParameter("cantidadSublimacion.id")));

        return sublimacionDTO;
    }

    private TalonarioDTO armarTalonarioDTO(HttpServletRequest request) {
        TalonarioDTO talonarioDTO = new TalonarioDTO();
        talonarioDTO.setConNumerado(request.getParameter("conNumerado") != null);
        talonarioDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        talonarioDTO.setDetalleNumerado(request.getParameter("detalleNumerado"));
        talonarioDTO.setEsEncolado(request.getParameter("esEncolado") != null);
        talonarioDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        talonarioDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        talonarioDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        talonarioDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        talonarioDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        talonarioDTO.setTipoTalonarioId(Long.parseLong(request.getParameter("tipoTalonario.id")));
        talonarioDTO.setTipoTroqueladoTalonarioId(Long.parseLong(request.getParameter("tipoTroqueladoTalonario.id")));
        talonarioDTO.setModoTalonarioId(Long.parseLong(request.getParameter("modoTalonario.id")));
        talonarioDTO.setTipoColorTalonarioId(Long.parseLong(request.getParameter("tipoColorTalonario.id")));
        talonarioDTO.setMedidaTalonarioId(Long.parseLong(request.getParameter("medidaTalonario.id")));
        talonarioDTO.setTipoPapelTalonarioId(Long.parseLong(request.getParameter("tipoPapelTalonario.id")));
        talonarioDTO.setCantidadTalonarioId(Long.parseLong(request.getParameter("cantidadTalonario.id")));

        return talonarioDTO;
    }

    private TarjetaDTO armarTarjetaDTO(HttpServletRequest request) {
        TarjetaDTO tarjetaDTO = new TarjetaDTO();
        tarjetaDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        tarjetaDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        tarjetaDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        tarjetaDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        tarjetaDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        tarjetaDTO.setTipoPapelTarjetaId(Long.parseLong(request.getParameter("tipoPapelTarjeta.id")));
        tarjetaDTO.setTipoColorTarjetaId(Long.parseLong(request.getParameter("tipoColorTarjeta.id")));
        tarjetaDTO.setTipoFazTarjetaId(Long.parseLong(request.getParameter("tipoFazTarjeta.id")));
        tarjetaDTO.setTipoLaminadoTarjetaId(Long.parseLong(request.getParameter("tipoLaminadoTarjeta.id")));
        tarjetaDTO.setMedidaTarjetaId(Long.parseLong(request.getParameter("medidaTarjeta.id")));
        tarjetaDTO.setCantidadTarjetaId(Long.parseLong(request.getParameter("cantidadTarjeta.id")));

        return tarjetaDTO;
    }

    private TurneroDTO armarTurneroDTO(HttpServletRequest request) {
        TurneroDTO turneroDTO = new TurneroDTO();
        turneroDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        turneroDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        turneroDTO.setCantidadHojas(Integer.parseInt(request.getParameter("cantidadHojas")));
        turneroDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        turneroDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        turneroDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        turneroDTO.setTipoColorTurneroId(Long.parseLong(request.getParameter("tipoColorTurnero.id")));
        turneroDTO.setCantidadTurneroId(Long.parseLong(request.getParameter("cantidadTurnero.id")));
        turneroDTO.setMedidaTurneroId(Long.parseLong(request.getParameter("medidaTurnero.id")));

        return turneroDTO;
    }

    private ViniloDTO armarViniloDTO(HttpServletRequest request) {
        ViniloDTO viniloDTO = new ViniloDTO();
        viniloDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        viniloDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloDTO.setTipoViniloId(Long.parseLong(request.getParameter("tipoVinilo.id")));
        viniloDTO.setTipoAdicionalViniloId(Long.parseLong(request.getParameter("tipoAdicionalVinilo.id")));
        viniloDTO.setTipoCorteViniloId(Long.parseLong(request.getParameter("tipoCorteVinilo.id")));
        viniloDTO.setMedidaViniloId(Long.parseLong(request.getParameter("medidaVinilo.id")));
        viniloDTO.setCantidadViniloId(Long.parseLong(request.getParameter("cantidadVinilo.id")));

        return viniloDTO;
    }

    private ViniloDeCorteDTO armarViniloDeCorteDTO(HttpServletRequest request) {
        ViniloDeCorteDTO viniloDeCorteDTO = new ViniloDeCorteDTO();
        viniloDeCorteDTO.setEsPromocional(request.getParameter("esPromocional") != null);
        viniloDeCorteDTO.setEsOracal(request.getParameter("esOracal") != null);
        viniloDeCorteDTO.setCodigoColor(request.getParameter("codigoColor"));
        viniloDeCorteDTO.setConColocacion(request.getParameter("conColocacion") != null);
        viniloDeCorteDTO.setMedida(request.getParameter("medida"));
        viniloDeCorteDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloDeCorteDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloDeCorteDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloDeCorteDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloDeCorteDTO.setTraeMaterialViniloId(Long.parseLong(request.getParameter("traeMaterialVinilo.id")));

        return viniloDeCorteDTO;
    }

    private ViniloPlasticoCorrugadoDTO armarViniloPlasticoCorrugadoDTO(HttpServletRequest request) {
        ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO = new ViniloPlasticoCorrugadoDTO();
        viniloPlasticoCorrugadoDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        viniloPlasticoCorrugadoDTO.setConOjales(request.getParameter("conOjales") != null);
        viniloPlasticoCorrugadoDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        viniloPlasticoCorrugadoDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        viniloPlasticoCorrugadoDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        viniloPlasticoCorrugadoDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        viniloPlasticoCorrugadoDTO.setMedidaViniloPlasticoCorrugadoId(Long.parseLong(request.getParameter("medidaViniloPlasticoCorrugado.id")));

        return viniloPlasticoCorrugadoDTO;
    }

    private VoucherDTO armarVoucherDTO(HttpServletRequest request) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTipoPapelPersonalizado(request.getParameter("tipoPapelPersonalizado"));
        voucherDTO.setMedidaPersonalizada(request.getParameter("medidaPersonalizada"));
        voucherDTO.setEnlaceArchivo(request.getParameter("enlaceArchivo"));
        voucherDTO.setConAdicionalDisenio(request.getParameter("conAdicionalDisenio") != null);
        voucherDTO.setInformacionAdicional(request.getParameter("informacionAdicional"));
        voucherDTO.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        voucherDTO.setMedidaVoucherId(Long.parseLong(request.getParameter("medidaVoucher.id")));
        voucherDTO.setTipoPapelVoucherId(Long.parseLong(request.getParameter("tipoPapelVoucher.id")));
        voucherDTO.setTipoFazVoucherId(Long.parseLong(request.getParameter("tipoFazVoucher.id")));
        voucherDTO.setCantidadVoucherId(Long.parseLong(request.getParameter("cantidadVoucher.id")));

        return voucherDTO;
    }
}
