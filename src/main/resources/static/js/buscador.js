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
      const tipoProducto = this.getAttribute("data-tipoproducto");
      renderizarEliminacionCorrespondiente(idOrden, tipoProducto);
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

  function renderizarEliminacionCorrespondiente(idOrden, tipoProducto) {
      if (!confirm("¿Estás seguro que querés eliminar esta orden?")) {
        return; // si cancela, no hace nada
      }

      switch (tipoProducto) {
        case "agenda":
            fetch(`/api/eliminar-orden-agenda/${idOrden}`, {
                    method: "DELETE"
                  })
                  .then(response => {
                      window.location.href = `/buscador`;
                  })
                  .catch(error => console.error("Error eliminando orden agenda:", error));
          break;
        case "anotador":
            fetch(`/api/eliminar-orden-anotador/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                    window.location.href = `/buscador`;
                })
              .catch(error => console.error("Error eliminando orden anotador:", error));
          break;
        case "carpeta con solapas":
                fetch(`/api/eliminar-orden-carpeta-solapa/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                    window.location.href = `/buscador`;
                })
              .catch(error => console.error("Error eliminando orden carpeta con solapas:", error));
          break;
        case "catálogo":
            fetch(`/api/eliminar-orden-catalogo/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden catálogo:", error));
          break;
        case "cierra bolsas":
          fetch(`/api/eliminar-orden-cierra-bolsas/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden cierra bolsas:", error));
          break;
        case "combo":
            fetch(`/api/eliminar-orden-combo/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden combo:", error));
          break;
        case "cuaderno anillado":
            fetch(`/api/eliminar-orden-cuaderno-anillado/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden cuaderno anillado:", error));
          break;
        case "entrada":
          fetch(`/api/eliminar-orden-entrada/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden entrada:", error));
          break;
        case "etiqueta":
          fetch(`/api/eliminar-orden-etiqueta/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden etiqueta:", error));
          break;
        case "flybanner":
          fetch(`/api/eliminar-orden-flybanner/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden flybanner:", error));
          break;
        case "folleto":
          fetch(`/api/eliminar-orden-folleto/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden folleto:", error));
          break;
        case "hojas membretadas":
        fetch(`/api/eliminar-orden-hojas-membreteadas/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden hojas membreteadas:", error));
          break;
        case "impresion":
        fetch(`/api/eliminar-orden-impresion/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden impresion:", error));
          break;
        case "lona común":
          fetch(`/api/eliminar-orden-lona-comun/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden lona comun:", error));
          break;
        case "lona publicitaria":
          fetch(`/api/eliminar-orden-lona-publicitaria/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden lona publicitaria:", error));
          break;
        case "sin categoría":
          fetch(`/api/eliminar-orden-otro/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden otro:", error));
          break;
        case "rifa o bono":
          fetch(`/api/eliminar-orden-rifa-bono-contribucion/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden rifa o bono contribución:", error));
          break;
        case "rotulación":
          fetch(`/api/eliminar-orden-rotulacion/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden rotulación:", error));
          break;
        case "sello automático":
           fetch(`/api/eliminar-orden-sello-automatico/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden sello automático:", error));
          break;
        case "sello automático escolar":
        fetch(`/api/eliminar-orden-sello-automatico-escolar/${idOrden}`, {
                    method: "DELETE"
                  })
                  .then(response => {
                    window.location.href = `/buscador`;
                  })
                  .catch(error => console.error("Error eliminando orden sello automático escolar:", error));
          break;
        case "sello de madera":
          fetch(`/api/eliminar-orden-sello-madera/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden sello de madera:", error));
          break;
        case "sobre":
            fetch(`/api/eliminar-orden-sobre/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden sobre:", error));
          break;
        case "sticker":
          fetch(`/api/eliminar-orden-sticker/${idOrden}`, {
                method: "DELETE"
              })
              .then(response => {
                window.location.href = `/buscador`;
              })
              .catch(error => console.error("Error eliminando orden sticker:", error));
          break;
        case "sublimación":
          fetch(`/api/eliminar-orden-sublimacion/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden sublimación:", error));
          break;
        case "talonario":
          fetch(`/api/eliminar-orden-talonario/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden talonario:", error));
          break;
        case "tarjeta":
          fetch(`/api/eliminar-orden-tarjeta/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden tarjeta:", error));
          break;
        case "turnero":
          fetch(`/api/eliminar-orden-turnero/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden turnero:", error));
          break;
        case "vinilo":
          fetch(`/api/eliminar-orden-vinilo/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden vinilo:", error));
          break;
        case "vinilo de corte":
          fetch(`/api/eliminar-orden-vinilo-de-corte/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden vinilo de corte:", error));
          break;
        case "vinilo con plástico corrugado":
          fetch(`/api/eliminar-orden-vinilo-plastico-corrugado/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden vinio plástico corrugado:", error));
          break;
        case "voucher":
          fetch(`/api/eliminar-orden-voucher/${idOrden}`, {
            method: "DELETE"
          })
          .then(response => {
            window.location.href = `/buscador`;
          })
          .catch(error => console.error("Error eliminando orden voucher:", error));
          break;
        default:
          console.warn("Tipo de producto no manejado:", tipoProducto);
      }
    }
});
