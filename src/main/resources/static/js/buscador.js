document.addEventListener("DOMContentLoaded", function () {
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-title]'));
  tooltipTriggerList.forEach(function (tooltipTriggerEl) {
    new bootstrap.Tooltip(tooltipTriggerEl);
  });

  const botonesEditar = document.querySelectorAll(".editar-btn");
  botonesEditar.forEach(boton => {
    boton.addEventListener("click", function () {
      const idOrden = this.getAttribute("data-idorden");
      const tipoProducto = this.getAttribute("data-tipoproducto");
      renderizarEdicionCorrespondiente(idOrden, tipoProducto);
    });
  });

  const botonesEliminar = document.querySelectorAll(".eliminar-btn");
  botonesEliminar.forEach(boton => {
    boton.addEventListener("click", function () {
      const idOrden = this.getAttribute("data-idorden");

      if (!confirm("¿Estás seguro que querés eliminar esta orden?")) {
        return; 
      }

      fetch(`/api/eliminar-producto/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden hojas membreteadas:", error));
    });
  });

  function renderizarEdicionCorrespondiente(idOrden, tipoProducto) {
    switch (tipoProducto) {
      case "agenda":
        window.location.href = `/crear-odt-agenda/${idOrden}`;
        break;
      case "anotador":
        window.location.href = `/crear-odt-anotador/${idOrden}`;
        break;
      case "carpeta con solapas":
        window.location.href = `/crear-odt-carpeta-solapa/${idOrden}`;
        break;
      case "catálogo":
        window.location.href = `/crear-odt-catalogo/${idOrden}`;
        break;
      case "cierra bolsas":
        window.location.href = `/crear-odt-cierra-bolsas/${idOrden}`;
        break;
      case "combo":
        window.location.href = `/crear-odt-combo/${idOrden}`;
        break;
      case "cuaderno anillado":
        window.location.href = `/crear-odt-cuaderno-anillado/${idOrden}`;
        break;
      case "entrada":
        window.location.href = `/crear-odt-entrada/${idOrden}`;
        break;
      case "etiqueta":
        window.location.href = `/crear-odt-etiqueta/${idOrden}`;
        break;
      case "flybanner":
        window.location.href = `/crear-odt-flybanner/${idOrden}`;
        break;
      case "folleto":
        window.location.href = `/crear-odt-folleto/${idOrden}`;
        break;
      case "hojas membretadas":
        window.location.href = `/crear-odt-hojas-membreteadas/${idOrden}`;
        break;
      case "impresion":
        window.location.href = `/crear-odt-impresion/${idOrden}`;
        break;
      case "lona común":
        window.location.href = `/crear-odt-lona-comun/${idOrden}`;
        break;
      case "lona publicitaria":
        window.location.href = `/crear-odt-lona-publicitaria/${idOrden}`;
        break;
      case "sin categoría":
        window.location.href = `/crear-odt-otro/${idOrden}`;
        break;
      case "rifa o bono":
        window.location.href = `/crear-odt-rifas-bonos-contribucion/${idOrden}`;
        break;
      case "rotulación":
        window.location.href = `/crear-odt-rotulacion/${idOrden}`;
        break;
      case "sello automático":
        window.location.href = `/crear-odt-sello-automatico/${idOrden}`;
        break;
      case "sello automático escolar":
            window.location.href = `/crear-odt-sello-automatico-escolar/${idOrden}`;
            break;
      case "sello de madera":
        window.location.href = `/crear-odt-sello-madera/${idOrden}`;
        break;
      case "sobre":
        window.location.href = `/crear-odt-sobre/${idOrden}`;
        break;
      case "sticker":
        window.location.href = `/crear-odt-sticker/${idOrden}`;
        break;
      case "sublimación":
        window.location.href = `/crear-odt-sublimacion/${idOrden}`;
        break;
      case "talonario":
        window.location.href = `/crear-odt-talonario/${idOrden}`;
        break;
      case "tarjeta":
        window.location.href = `/crear-odt-tarjeta/${idOrden}`;
        break;
      case "turnero":
        window.location.href = `/crear-odt-turnero/${idOrden}`;
        break;
      case "vinilo":
        window.location.href = `/crear-odt-vinilo/${idOrden}`;
        break;
      case "vinilo de corte":
        window.location.href = `/crear-odt-vinilo-de-corte/${idOrden}`;
        break;
      case "vinilo con plástico corrugado":
        window.location.href = `/crear-odt-vinilo-plastico-corrugado/${idOrden}`;
        break;
      case "voucher":
        window.location.href = `/crear-odt-voucher/${idOrden}`;
        break;
      default:
        console.warn("Tipo de producto no manejado:", tipoProducto);
    }
  }
});
