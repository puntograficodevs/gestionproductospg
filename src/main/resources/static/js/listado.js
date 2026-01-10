document.addEventListener('DOMContentLoaded', () => {
    const botonesVer = document.querySelectorAll('.ver-btn');
    const modalElement = document.getElementById('ordenModal');
    const cuerpoModal = document.getElementById("modalBody");

    // Inicializar modal de Bootstrap una sola vez
    const modal = new bootstrap.Modal(modalElement);

    botonesVer.forEach(boton => {
        boton.addEventListener('click', async () => {
            const idOrden = boton.dataset.idorden;
            const tipoProducto = boton.dataset.tipoproducto;

            // Mostrar loader
            cuerpoModal.innerHTML = '<p class="text-center">Cargando los datos de la orden...</p>';

            try {
                if (tipoProducto === "agenda") {
                    const orden = await fetch("/api/orden/ordenAgenda/" + idOrden)
                        .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenAgenda(orden);
                    modal.show();
                } else if (tipoProducto === "anotador") {
                    const orden = await fetch("/api/orden/ordenAnotador/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenAnotador(orden);
                    modal.show();
                } else if (tipoProducto === "carpeta con solapas") {
                    const orden = await fetch("/api/orden/ordenCarpetaSolapa/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenCarpetaSolapa(orden);
                    modal.show();
                } else if (tipoProducto === "catálogo") {
                    const orden = await fetch("/api/orden/ordenCatalogo/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenCatalogo(orden);
                    modal.show();
                } else if (tipoProducto === "cierra bolsas") {
                    const orden = await fetch("/api/orden/ordenCierraBolsas/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenCierraBolsas(orden);
                    modal.show();
                } else if (tipoProducto === "combo") {
                    const orden = await fetch("/api/orden/ordenCombo/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenCombo(orden);
                    modal.show();
                } else if (tipoProducto === "cuaderno anillado") {
                    const orden = await fetch("/api/orden/ordenCuadernoAnillado/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenCuadernoAnillado(orden);
                    modal.show();
                } else if (tipoProducto === "entrada") {
                    const orden = await fetch("/api/orden/ordenEntrada/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenEntrada(orden);
                    modal.show();
                } else if (tipoProducto === "etiqueta") {
                    const orden = await fetch("/api/orden/ordenEtiqueta/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenEtiqueta(orden);
                    modal.show();
                } else if (tipoProducto === "flybanner") {
                    const orden = await fetch("/api/orden/ordenFlybanner/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenFlybanner(orden);
                    modal.show();
                } else if (tipoProducto === "folleto") {
                    const orden = await fetch("/api/orden/ordenFolleto/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenFolleto(orden);
                    modal.show();
                } else if (tipoProducto === "hojas membretadas") {
                    const orden = await fetch("/api/orden/ordenHojasMembreteadas/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenHojasMembreteadas(orden);
                    modal.show();
                } else if (tipoProducto === "impresion") {
                    const orden = await fetch("/api/orden/ordenImpresion/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenImpresion(orden);
                    modal.show();
                } else if (tipoProducto === "lona común") {
                    const orden = await fetch("/api/orden/ordenLonaComun/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenLonaComun(orden);
                    modal.show();
                } else if (tipoProducto === "lona publicitaria") {
                    const orden = await fetch("/api/orden/ordenLonaPublicitaria/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenLonaPublicitaria(orden);
                    modal.show();
                } else if (tipoProducto === "sin categoría") {
                    const orden = await fetch("/api/orden/ordenOtro/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenOtro(orden);
                    modal.show();
                } else if (tipoProducto === "rifa o bono") {
                    const orden = await fetch("/api/orden/ordenRifa/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenRifasBonos(orden);
                    modal.show();
                } else if (tipoProducto === "rotulación") {
                   const orden = await fetch("/api/orden/ordenRotulacion/" + idOrden)
                   .then(res => res.json());

                   cuerpoModal.innerHTML = renderOrdenRotulacion(orden);
                   modal.show();
                } else if (tipoProducto === "sello automático") {
                    const orden = await fetch("/api/orden/ordenSelloAutomatico/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSelloAutomatico(orden);
                    modal.show();
                } else if (tipoProducto === "sello automático escolar") {
                    const orden = await fetch("/api/orden/ordenSelloAutomaticoEscolar/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSelloAutomaticoEscolar(orden);
                    modal.show();
                } else if (tipoProducto === "sello de madera") {
                    const orden = await fetch("/api/orden/ordenSelloMadera/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSelloMadera(orden);
                    modal.show();
                } else if (tipoProducto === "sobre") {
                    const orden = await fetch("/api/orden/ordenSobre/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSobre(orden);
                    modal.show();
                } else if (tipoProducto === "sticker") {
                    const orden = await fetch("/api/orden/ordenSticker/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSticker(orden);
                    modal.show();
                } else if (tipoProducto === "sublimación") {
                    const orden = await fetch("/api/orden/ordenSublimacion/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenSublimacion(orden);
                    modal.show();
                } else if (tipoProducto === "talonario") {
                    const orden = await fetch("/api/orden/ordenTalonario/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenTalonario(orden);
                    modal.show();
                } else if (tipoProducto === "tarjeta") {
                    const orden = await fetch("/api/orden/ordenTarjeta/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenTarjeta(orden);
                    modal.show();
                } else if (tipoProducto === "turnero") {
                    const orden = await fetch("/api/orden/ordenTurnero/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenTurnero(orden);
                    modal.show();
                } else if (tipoProducto === "vinilo") {
                    const orden = await fetch("/api/orden/ordenVinilo/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenVinilo(orden);
                    modal.show();
                } else if (tipoProducto === "vinilo de corte") {
                    const orden = await fetch("/api/orden/ordenViniloDeCorte/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenViniloDeCorte(orden);
                    modal.show();
                } else if (tipoProducto === "vinilo con plástico corrugado") {
                    const orden = await fetch("/api/orden/ordenViniloPlasticoCorrugado/" + idOrden)
                    .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenViniloPlasticoCorrugado(orden);
                    modal.show();
                } else if (tipoProducto === "voucher") {
                    const orden = await fetch("/api/orden/ordenVoucher/" + idOrden)
                   .then(res => res.json());

                    cuerpoModal.innerHTML = renderOrdenVoucher(orden);
                    modal.show();
                }

                // Aquí puedes agregar más tipos de producto si los tienes
            } catch (error) {
                cuerpoModal.innerHTML = '<p class="text-danger text-center">Error al cargar la orden.</p>';
                console.error(error);
            }
        });
    });

    // Limpiar modal al cerrarlo (no hace falta remover backdrop manualmente)
    modalElement.addEventListener('hidden.bs.modal', () => {
        cuerpoModal.innerHTML = '';
    });

    function renderOrdenAgenda(orden) {
        return `<div class="row mt-4">
            <div class="col-6">
                <div class="card mb-3 border contenedor-datos-orden-impresion">
                    <h6>Datos del cliente</h6>
                    <hr class="divider">
                    <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
                    <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
                    <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
                </div>
                <div class="card mb-3 border contenedor-datos-orden-impresion">
                    <h6>Datos del pago</h6>
                    <hr class="divider">
                    <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
                    <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
                    <p><strong>Abonado: </strong>$${orden.ordenTrabajo.abonado}</p>
                    <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
                    <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
                </div>
            </div>
            <div class="col-6">
                <div class="card mb-3 border contenedor-datos-orden-impresion">
                    <h6>Datos del producto</h6>
                    <hr class="divider">
                    <p><strong>Medida: </strong>${orden.agenda.medida}</p>
                    <p><strong>Tipo de tapa: </strong>${
                        orden.agenda.tipoTapaAgenda.tipo === 'OTRA'
                        ? orden.agenda.tipoTapaPersonalizada
                        : orden.agenda.tipoTapaAgenda.tipo
                    }</p>
                    <p><strong>Color: </strong>${orden.agenda.tipoColorAgenda.tipo}</p>
                    <p><strong>Cantidad de hojas: </strong>${orden.agenda.cantidadHojas}</p>
                    <hr class="divider">
                    <p><strong>Adicional diseño: </strong>${orden.agenda.conAdicionalDisenio ? 'Sí' : 'No'}</p>
                    <p><strong>Enlace al archivo: </strong>${
                        orden.agenda.enlaceArchivo ? `<a href="${orden.agenda.enlaceArchivo}" target="_blank">${orden.agenda.enlaceArchivo}</a>` : '-'
                    }</p>
                    <p><strong>Información adicional: </strong>${orden.agenda.informacionAdicional || '-'}</p>
                    <hr class="divider">
                    <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
                    ${orden.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>` : ''}
                    ${orden.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>` : ''}
                    <p><strong>Cantidad de productos: </strong>${orden.agenda.cantidad}</p>
                </div>
            </div>
            <div class="col-12">
                <div class="card mb-3 border contenedor-datos-orden-impresion">
                    <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
                </div>
            </div>
        </div>`;
    }
    function renderOrdenAnotador(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Medida: </strong>${orden.anotador.medida}</p>
              <p><strong>Tipo de tapa: </strong>${orden.anotador.tipoTapa}</p>
              <p><strong>Cantidad de hojas: </strong>${orden.anotador.cantidadHojas}</p>
              <hr class="divider">
              <p><strong>Adicional diseño: </strong>${orden.anotador.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                orden.anotador.enlaceArchivo === ''
                  ? ' - '
                  : `<a href="${orden.anotador.enlaceArchivo}" target="_blank">${orden.anotador.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                orden.anotador.informacionAdicional === ''
                  ? ' - '
                  : orden.anotador.informacionAdicional
              }</p>
              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.anotador.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
              <div class="card mb-3 border contenedor-datos-orden-impresion">
                  <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
              </div>
          </div>
        </div>
      `;
    }
    function renderOrdenCarpetaSolapa(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${orden.carpetaSolapa.tipoPapel}</p>
              <p><strong>Tipo de laminado: </strong>${orden.carpetaSolapa.tipoLaminadoCarpetaSolapa.laminado}</p>
              <p><strong>Tipo de faz: </strong>${orden.carpetaSolapa.tipoFazCarpetaSolapa.tipo}</p>
              <hr class="divider">
              <p><strong>Adicional diseño: </strong>${orden.carpetaSolapa.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                orden.carpetaSolapa.enlaceArchivo === ''
                  ? ' - '
                  : `<a href="${orden.carpetaSolapa.enlaceArchivo}" target="_blank">${orden.carpetaSolapa.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                orden.carpetaSolapa.informacionAdicional === ''
                  ? ' - '
                  : orden.carpetaSolapa.informacionAdicional
              }</p>
              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.carpetaSolapa.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenCatalogo(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion ">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${orden.catalogo.tipoPapel}</p>
              <p><strong>Tipo de laminado: </strong>${orden.catalogo.tipoLaminadoCatalogo.laminado}</p>
              <p><strong>Tipo de faz: </strong>${orden.catalogo.tipoFazCatalogo.tipo}</p>
              <hr class="divider">
              <p><strong>Adicional diseño: </strong>${orden.catalogo.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                orden.catalogo.enlaceArchivo === ''
                  ? ' - '
                  : `<a href="${orden.catalogo.enlaceArchivo}" target="_blank">${orden.catalogo.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                orden.catalogo.informacionAdicional === ''
                  ? ' - '
                  : orden.catalogo.informacionAdicional
              }</p>
              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.catalogo.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenCierraBolsas(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">

              <p><strong>Medida: </strong>
                ${orden.cierraBolsas.medidaCierraBolsas.medida === 'OTRA'
                  ? orden.cierraBolsas.medidaPersonalizada
                  : orden.cierraBolsas.medidaCierraBolsas.medida}
              </p>

              <p><strong>Tipo de troquelado: </strong>${orden.cierraBolsas.tipoTroqueladoCierraBolsas.tipo}</p>

              <hr class="divider">

              <p><strong>Adicional diseño: </strong>${orden.cierraBolsas.conAdicionalDisenio ? 'Sí' : 'No'}</p>

              <p><strong>Enlace al archivo: </strong>
                ${
                  !orden.cierraBolsas.enlaceArchivo || orden.cierraBolsas.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.cierraBolsas.enlaceArchivo}" target="_blank">${orden.cierraBolsas.enlaceArchivo}</a>`
                }
              </p>

              <p><strong>Información adicional: </strong>
                ${!orden.cierraBolsas.informacionAdicional || orden.cierraBolsas.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.cierraBolsas.informacionAdicional}
              </p>

              <hr class="divider">

              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.cierraBolsas.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenCombo(orden) {
      // Diccionario de descripciones de combo
      const descripcionesCombo = {
        "OFICINA": "100 tarjetas simple faz, 100 sobres oficio inglés, 1 talonario A4 a color con membrete",
        "PROFESIONAL": "100 tarjetas simple faz, 100 sobres oficio inglés, 2 talonarios RP blanco y negro (13x18 cm)",
        "EMPRENDEDOR X100": "100 cierra bolsas (8x4 cm), 100 etiquetas colgantes (7x5 cm), 100 circulares troqueladas (5 cm)",
        "EMPRENDEDOR X200": "200 cierra bolsas (8x4 cm), 200 etiquetas colgantes (7x5 cm), 200 circulares troqueladas (5 cm)",
        "EMPRENDEDOR X500": "500 cierra bolsas (8x4 cm), 500 etiquetas colgantes (7x5 cm), 500 circulares troqueladas (5 cm)"
      };

      const tipoCombo = orden.combo.tipoCombo.tipo;
      const descripcion = descripcionesCombo[tipoCombo] || " - ";

      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">

              <p><strong>Tipo de combo: </strong>${tipoCombo}</p>
              <p><strong>Descripción del combo: </strong>${descripcion}</p>

              <p><strong>Adicional diseño: </strong>${orden.combo.conAdicionalDisenio ? 'Sí' : 'No'}</p>

              <p><strong>Enlace al archivo: </strong>
                ${
                  !orden.combo.enlaceArchivo || orden.combo.enlaceArchivo.trim() === ''
                    ? ' - '
                    : `<a href="${orden.combo.enlaceArchivo}" target="_blank">${orden.combo.enlaceArchivo}</a>`
                }
              </p>

              <p><strong>Información adicional: </strong>
                ${!orden.combo.informacionAdicional || orden.combo.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.combo.informacionAdicional}
              </p>

              <hr class="divider">

              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.combo.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenCuadernoAnillado(orden) {
      // Normalizamos medida
      const medida = orden.cuadernoAnillado.medidaCuadernoAnillado.medida === "OTRA"
        ? orden.cuadernoAnillado.medidaPersonalizada
        : orden.cuadernoAnillado.medidaCuadernoAnillado.medida;

      // Normalizamos tipo de tapa
      const tipoTapa = orden.cuadernoAnillado.tipoTapaCuadernoAnillado.tipo === "OTRA"
        ? orden.cuadernoAnillado.tipoTapaPersonalizada
        : orden.cuadernoAnillado.tipoTapaCuadernoAnillado.tipo;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">

              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Tipo de tapa: </strong>${tipoTapa}</p>
              <p><strong>Cantidad de hojas: </strong>${orden.cuadernoAnillado.cantidadHojas}</p>

              <hr class="divider">

              <p><strong>Adicional diseño: </strong>${orden.cuadernoAnillado.conAdicionalDisenio ? 'Sí' : 'No'}</p>

              <p><strong>Enlace al archivo: </strong>
                ${
                  !orden.cuadernoAnillado.enlaceArchivo || orden.cuadernoAnillado.enlaceArchivo.trim() === ''
                    ? ' - '
                    : `<a href="${orden.cuadernoAnillado.enlaceArchivo}" target="_blank">${orden.cuadernoAnillado.enlaceArchivo}</a>`
                }
              </p>

              <p><strong>Información adicional: </strong>
                ${!orden.cuadernoAnillado.informacionAdicional || orden.cuadernoAnillado.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.cuadernoAnillado.informacionAdicional}
              </p>

              <hr class="divider">

              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.cuadernoAnillado.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenEntrada(orden) {
      // Normalizamos medida
      const medida = orden.entrada.medidaEntrada.medida === "OTRA"
        ? orden.entrada.medidaPersonalizada
        : orden.entrada.medidaEntrada.medida;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">

              <p><strong>Tipo de papel: </strong>${orden.entrada.tipoPapelEntrada.tipo}</p>
              <p><strong>Tipo de color: </strong>${orden.entrada.tipoColorEntrada.tipo}</p>
              <p><strong>Tipo de troquelado: </strong>${orden.entrada.tipoTroqueladoEntrada.tipo}</p>
              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Numerado: </strong>${orden.entrada.numeradoEntrada.numerado}</p>
              <p><strong>Terminación: </strong>${orden.entrada.terminacionEntrada.terminacion}</p>
              <p><strong>Adicional diseño: </strong>${orden.entrada.conAdicionalDisenio ? 'Sí' : 'No'}</p>

              <p><strong>Enlace al archivo: </strong>
                ${
                  !orden.entrada.enlaceArchivo || orden.entrada.enlaceArchivo.trim() === ''
                    ? ' - '
                    : `<a href="${orden.entrada.enlaceArchivo}" target="_blank">${orden.entrada.enlaceArchivo}</a>`
                }
              </p>

              <p><strong>Información adicional: </strong>
                ${!orden.entrada.informacionAdicional || orden.entrada.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.entrada.informacionAdicional}
              </p>

              <hr class="divider">

              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.entrada.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenEtiqueta(orden) {
      // Normalizamos medida
      const medida = orden.etiqueta.medidaEtiqueta.medida === "OTRA"
        ? orden.etiqueta.medidaPersonalizada
        : orden.etiqueta.medidaEtiqueta.medida;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">

              <p><strong>Tipo de papel: </strong>${orden.etiqueta.tipoPapelEtiqueta.tipo}</p>
              <p><strong>Tipo de laminado: </strong>${orden.etiqueta.tipoLaminadoEtiqueta.laminado}</p>
              <p><strong>Tipo de faz: </strong>${orden.etiqueta.tipoFazEtiqueta.tipo}</p>
              <p><strong>Tamaño de perforación: </strong>${orden.etiqueta.tamanioPerforacion.tamanio}</p>
              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Adicional diseño: </strong>${orden.etiqueta.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Perforación adicional: </strong>${orden.etiqueta.conPerforacionAdicional ? 'Sí' : 'No'}</p>
              <p><strong>Marca adicional: </strong>${orden.etiqueta.conMarcaAdicional ? 'Sí' : 'No'}</p>

              <p><strong>Enlace al archivo: </strong>
                ${
                  !orden.etiqueta.enlaceArchivo || orden.etiqueta.enlaceArchivo.trim() === ''
                    ? ' - '
                    : `<a href="${orden.etiqueta.enlaceArchivo}" target="_blank">${orden.etiqueta.enlaceArchivo}</a>`
                }
              </p>

              <p><strong>Información adicional: </strong>
                ${!orden.etiqueta.informacionAdicional || orden.etiqueta.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.etiqueta.informacionAdicional}
              </p>

              <hr class="divider">

              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.etiqueta.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenFlybanner(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Altura: </strong>${orden.flybanner.alturaFlybanner.altura}</p>
              <p><strong>Bandera: </strong>${orden.flybanner.banderaFlybanner.bandera}</p>
              <p><strong>Tipo de faz: </strong>${orden.flybanner.tipoFazFlybanner.tipo}</p>
              <p><strong>Tipo de base: </strong>${orden.flybanner.tipoBaseFlybanner.tipo}</p>
              <p><strong>Adicional diseño: </strong>${orden.flybanner.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !orden.flybanner.enlaceArchivo || orden.flybanner.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.flybanner.enlaceArchivo}" target="_blank">${orden.flybanner.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !orden.flybanner.informacionAdicional || orden.flybanner.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.flybanner.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.flybanner.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenFolleto(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${orden.folleto.tipoPapelFolleto.tipo}</p>
              <p><strong>Tipo de color: </strong>${orden.folleto.tipoColorFolleto.tipo}</p>
              <p><strong>Tipo de faz: </strong>${orden.folleto.tipoFazFolleto.tipo}</p>
              <p><strong>Tipo de folleto: </strong>${orden.folleto.tipoFolleto.tipo}</p>
              <p><strong>Tamaño de hoja: </strong>${orden.folleto.tamanioHojaFolleto.tamanio}</p>
              <p><strong>Adicional diseño: </strong>${orden.folleto.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Plegado: </strong>${orden.folleto.conPlegado ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !orden.folleto.enlaceArchivo || orden.folleto.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.folleto.enlaceArchivo}" target="_blank">${orden.folleto.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !orden.folleto.informacionAdicional || orden.folleto.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.folleto.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.folleto.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenHojasMembreteadas(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de color: </strong>${orden.hojasMembreteadas.tipoColorHojasMembreteadas.tipo}</p>
              <p><strong>Cantidad de hojas: </strong>${orden.hojasMembreteadas.cantidadHojas}</p>
              <p><strong>Medida: </strong>${
                orden.hojasMembreteadas.medidaHojasMembreteadas.medida === 'OTRA'
                  ? orden.hojasMembreteadas.medidaPersonalizada
                  : orden.hojasMembreteadas.medidaHojasMembreteadas.medida
              }</p>
              <p><strong>Adicional diseño: </strong>${orden.hojasMembreteadas.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !orden.hojasMembreteadas.enlaceArchivo || orden.hojasMembreteadas.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.hojasMembreteadas.enlaceArchivo}" target="_blank">${orden.hojasMembreteadas.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !orden.hojasMembreteadas.informacionAdicional || orden.hojasMembreteadas.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.hojasMembreteadas.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de productos: </strong>${orden.hojasMembreteadas.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenImpresion(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${orden.impresion.tipoPapelImpresion.tipo}</p>
              <p><strong>Tipo de color: </strong>${orden.impresion.tipoColorImpresion.tipo}</p>
              <p><strong>Tipo de faz: </strong>${orden.impresion.tipoFazImpresion.tipo}</p>
              <p><strong>Tipo de impresión: </strong>${orden.impresion.tipoImpresion.tipo}</p>
              <p><strong>Tamaño de hoja: </strong>${orden.impresion.tamanioHojaImpresion.tamanio}</p>
              <p><strong>Anillado: </strong>${orden.impresion.esAnillado ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !orden.impresion.enlaceArchivo || orden.impresion.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.impresion.enlaceArchivo}" target="_blank">${orden.impresion.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !orden.impresion.informacionAdicional || orden.impresion.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.impresion.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad de copias: </strong>${orden.impresion.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenLonaComun(orden) {
      return `
        <div class="row mt-4">
          <div class="col-6">
            <!-- DATOS DEL CLIENTE -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <!-- DATOS DEL PAGO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <!-- DATOS DEL PRODUCTO -->
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de lona: </strong>${orden.lonaComun.tipoLonaComun.tipo}</p>
              <p><strong>Medida: </strong>${
                orden.lonaComun.medidaLonaComun.medida === 'OTRA'
                  ? orden.lonaComun.medidaPersonalizada
                  : orden.lonaComun.medidaLonaComun.medida
              }</p>
              <p><strong>Con ojales comunes: </strong>${orden.lonaComun.conOjales ? 'Sí' : 'No'}</p>
              <p><strong>Con ojales reforzados: </strong>${orden.lonaComun.conOjalesConRefuerzo ? 'Sí' : 'No'}</p>
              <p><strong>Con bolsillos: </strong>${orden.lonaComun.conBolsillos ? 'Sí' : 'No'}</p>
              <p><strong>Con demasía para tensado: </strong>${orden.lonaComun.conDemasiaParaTensado ? 'Sí' : 'No'}</p>
              <p><strong>Con solapado: </strong>${orden.lonaComun.conSolapado ? 'Sí' : 'No'}</p>
              <p><strong>Con adicional diseño: </strong>${orden.lonaComun.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !orden.lonaComun.enlaceArchivo || orden.lonaComun.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${orden.lonaComun.enlaceArchivo}" target="_blank">${orden.lonaComun.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !orden.lonaComun.informacionAdicional || orden.lonaComun.informacionAdicional.trim() === ''
                  ? ' - '
                  : orden.lonaComun.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${
                orden.ordenTrabajo.horaEntrega && orden.ordenTrabajo.horaEntrega.trim() !== ''
                  ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>`
                  : ''
              }
              ${
                orden.ordenTrabajo.fechaMuestra
                  ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>`
                  : ''
              }
              <p><strong>Cantidad: </strong>${orden.lonaComun.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenLonaPublicitaria(ordenLonaPublicitaria) {
      const producto = ordenLonaPublicitaria.lonaPublicitaria;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenLonaPublicitaria.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenLonaPublicitaria.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenLonaPublicitaria.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenLonaPublicitaria.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenLonaPublicitaria.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenLonaPublicitaria.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenLonaPublicitaria.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenLonaPublicitaria.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de lona: </strong>${producto.tipoLonaPublicitaria.tipo}</p>
              <p><strong>Medida de lona: </strong>${producto.medidaLonaPublicitaria.medida}</p>
              <p><strong>Adicional portabanner: </strong>${producto.conAdicionalPortabanner ? 'Sí' : 'No'}</p>
              <p><strong>Con ojales comunes: </strong>${producto.conOjales ? 'Sí' : 'No'}</p>
              <p><strong>Con ojales reforzados: </strong>${producto.conOjalesConRefuerzo ? 'Sí' : 'No'}</p>
              <p><strong>Con bolsillos: </strong>${producto.conBolsillos ? 'Sí' : 'No'}</p>
              <p><strong>Con demasía para tensado: </strong>${producto.conDemasiaParaTensado ? 'Sí' : 'No'}</p>
              <p><strong>Con solapado: </strong>${producto.conSolapado ? 'Sí' : 'No'}</p>
              <p><strong>Con adicional diseño: </strong>${producto.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !producto.enlaceArchivo || producto.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${producto.enlaceArchivo}" target="_blank">${producto.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !producto.informacionAdicional || producto.informacionAdicional.trim() === ''
                  ? ' - '
                  : producto.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenLonaPublicitaria.ordenTrabajo.fechaEntrega}</p>
              ${ordenLonaPublicitaria.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenLonaPublicitaria.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenLonaPublicitaria.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenLonaPublicitaria.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${producto.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenLonaPublicitaria.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenOtro(ordenOtro) {
      const producto = ordenOtro.otro;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenOtro.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenOtro.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenOtro.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenOtro.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenOtro.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenOtro.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenOtro.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenOtro.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de color: </strong>${producto.tipoColorOtro.tipo}</p>
              <p><strong>Medida: </strong>${producto.medida}</p>
              <p><strong>Con adicional diseño: </strong>${producto.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !producto.enlaceArchivo || producto.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${producto.enlaceArchivo}" target="_blank">${producto.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !producto.informacionAdicional || producto.informacionAdicional.trim() === ''
                  ? ' - '
                  : producto.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenOtro.ordenTrabajo.fechaEntrega}</p>
              ${ordenOtro.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenOtro.ordenTrabajo.horaEntrega}</p>` : ''}
              <p><strong>Cantidad: </strong>${producto.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenOtro.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenRifasBonos(ordenRifasBonosContribucion) {
      const producto = ordenRifasBonosContribucion.rifasBonosContribucion;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenRifasBonosContribucion.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenRifasBonosContribucion.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenRifasBonosContribucion.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenRifasBonosContribucion.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenRifasBonosContribucion.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenRifasBonosContribucion.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenRifasBonosContribucion.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenRifasBonosContribucion.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${producto.tipoPapelRifa.tipo}</p>
              <p><strong>Tipo de troquelado: </strong>${producto.tipoTroqueladoRifa.tipo}</p>
              <p><strong>Tipo de color: </strong>${producto.tipoColorRifa.tipo}</p>
              <p><strong>Medida: </strong>${producto.medida}</p>
              <p><strong>Con encolado: </strong>${producto.conEncolado ? 'Sí' : 'No'}</p>
              <p><strong>Con numerado: </strong>${producto.conNumerado ? 'Sí' : 'No'}</p>
              <p><strong>Detalle del numerado: </strong>${
                !producto.detalleNumerado || producto.detalleNumerado.trim() === ''
                  ? ' - '
                  : producto.detalleNumerado
              }</p>
              <p><strong>Con adicional diseño: </strong>${producto.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !producto.enlaceArchivo || producto.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${producto.enlaceArchivo}" target="_blank">${producto.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !producto.informacionAdicional || producto.informacionAdicional.trim() === ''
                  ? ' - '
                  : producto.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenRifasBonosContribucion.ordenTrabajo.fechaEntrega}</p>
              ${ordenRifasBonosContribucion.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenRifasBonosContribucion.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenRifasBonosContribucion.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenRifasBonosContribucion.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${producto.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenRifasBonosContribucion.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenRotulacion(ordenRotulacion) {
      const producto = ordenRotulacion.rotulacion;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenRotulacion.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenRotulacion.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenRotulacion.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenRotulacion.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenRotulacion.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenRotulacion.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenRotulacion.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenRotulacion.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de rotulación: </strong>${producto.tipoRotulacion.tipo}</p>
              <p><strong>Tipo de corte: </strong>${producto.tipoCorteRotulacion.tipo}</p>
              <p><strong>Medida: </strong>${producto.medida}</p>
              <p><strong>Laminado: </strong>${producto.esLaminado ? 'Sí' : 'No'}</p>
              <p><strong>Con adicional diseño: </strong>${producto.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                !producto.enlaceArchivo || producto.enlaceArchivo.trim() === ''
                  ? ' - '
                  : `<a href="${producto.enlaceArchivo}" target="_blank">${producto.enlaceArchivo}</a>`
              }</p>
              <p><strong>Información adicional: </strong>${
                !producto.informacionAdicional || producto.informacionAdicional.trim() === ''
                  ? ' - '
                  : producto.informacionAdicional
              }</p>

              <hr class="divider">
              <p><strong>Fecha de colocación: </strong>${producto.horarioRotulacion}</p>
              <p><strong>Dirección de colocación: </strong>${producto.direccionRotulacion}</p>
              ${ordenRotulacion.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenRotulacion.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${producto.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenRotulacion.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSelloAutomatico(ordenSelloAutomatico) {
      const sello = ordenSelloAutomatico.selloAutomatico;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSelloAutomatico.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSelloAutomatico.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSelloAutomatico.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSelloAutomatico.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSelloAutomatico.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSelloAutomatico.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSelloAutomatico.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSelloAutomatico.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Modelo de sello: </strong>${sello.modeloSelloAutomatico.modelo}</p>
              <p><strong>Es profesional: </strong>${sello.esProfesional ? 'Sí' : 'No'}</p>
              <p><strong>Es particular: </strong>${sello.esParticular ? 'Sí' : 'No'}</p>
              <p><strong>Tipografía: </strong>${sello.tipografiaLineaUno}</p>
              <p><strong>Texto línea uno: </strong>${sello.textoLineaUno}</p>
              <p><strong>Texto línea dos: </strong>${sello.textoLineaDos ? sello.textoLineaDos : ' - '}</p>
              <p><strong>Texto línea tres: </strong>${sello.textoLineaTres ? sello.textoLineaTres : ' - '}</p>
              <p><strong>Texto línea cuatro: </strong>${sello.textoLineaCuatro ? sello.textoLineaCuatro : ' - '}</p>
              <p><strong>Con adicional diseño: </strong>${sello.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sello.enlaceArchivo && sello.enlaceArchivo.trim() !== ''
                  ? `<a href="${sello.enlaceArchivo}" target="_blank">${sello.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${sello.informacionAdicional && sello.informacionAdicional.trim() !== '' ? sello.informacionAdicional : ' - '}</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSelloAutomatico.ordenTrabajo.fechaEntrega}</p>
              ${ordenSelloAutomatico.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSelloAutomatico.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSelloAutomatico.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSelloAutomatico.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${sello.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSelloAutomatico.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSelloAutomaticoEscolar(ordenSelloAutomaticoEscolar) {
      const sello = ordenSelloAutomaticoEscolar.selloAutomaticoEscolar;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSelloAutomaticoEscolar.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSelloAutomaticoEscolar.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Modelo de sello: </strong>${sello.modeloSelloAutomaticoEscolar.modelo}</p>
              <p><strong>Tipografía: </strong>${sello.tipografia}</p>
              <p><strong>Dibujo: </strong>${sello.dibujo}</p>
              <p><strong>Texto línea uno: </strong>${sello.textoLineaUno}</p>
              <p><strong>Texto línea dos: </strong>${sello.textoLineaDos ? sello.textoLineaDos : ' - '}</p>
              <p><strong>Texto línea tres: </strong>${sello.textoLineaTres ? sello.textoLineaTres : ' - '}</p>
              <p><strong>Con adicional diseño: </strong>${sello.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sello.enlaceArchivo && sello.enlaceArchivo.trim() !== ''
                  ? `<a href="${sello.enlaceArchivo}" target="_blank">${sello.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${sello.informacionAdicional && sello.informacionAdicional.trim() !== '' ? sello.informacionAdicional : ' - '}</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.fechaEntrega}</p>
              ${ordenSelloAutomaticoEscolar.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSelloAutomaticoEscolar.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSelloAutomaticoEscolar.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${sello.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSelloAutomaticoEscolar.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSelloMadera(ordenSelloMadera) {
      const sello = ordenSelloMadera.selloMadera;

      const tamanio = sello.tamanioSelloMadera.tamanio === 'OTRO'
        ? sello.tamanioPersonalizado
        : sello.tamanioSelloMadera.tamanio;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSelloMadera.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSelloMadera.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSelloMadera.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSelloMadera.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSelloMadera.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSelloMadera.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSelloMadera.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSelloMadera.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tamaño: </strong>${tamanio}</p>
              <p><strong>Con adicional perilla: </strong>${sello.conAdicionalPerilla ? 'Sí' : 'No'}</p>
              <p><strong>Detalle del sello: </strong>${sello.detalleSello}</p>
              <p><strong>Tipografía: </strong>${sello.tipografiaLineaUno}</p>
              <p><strong>Con adicional diseño: </strong>${sello.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sello.enlaceArchivo && sello.enlaceArchivo.trim() !== ''
                  ? `<a href="${sello.enlaceArchivo}" target="_blank">${sello.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${sello.informacionAdicional && sello.informacionAdicional.trim() !== '' ? sello.informacionAdicional : ' - '}</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSelloMadera.ordenTrabajo.fechaEntrega}</p>
              ${ordenSelloMadera.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSelloMadera.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSelloMadera.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSelloMadera.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad: </strong>${sello.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSelloMadera.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSobre(ordenSobre) {
      const sobre = ordenSobre.sobre;

      const medida = sobre.medidaSobre.medida === 'OTRA'
        ? sobre.medidaPersonalizada
        : sobre.medidaSobre.medida;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSobre.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSobre.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSobre.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSobre.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSobre.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSobre.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSobre.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSobre.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Tipo de color: </strong>${sobre.tipoColorSobre.tipo}</p>
              <p><strong>Adicional diseño: </strong>${sobre.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sobre.enlaceArchivo && sobre.enlaceArchivo.trim() !== ''
                  ? `<a href="${sobre.enlaceArchivo}" target="_blank">${sobre.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${sobre.informacionAdicional && sobre.informacionAdicional.trim() !== '' ? sobre.informacionAdicional : ' - '}</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSobre.ordenTrabajo.fechaEntrega}</p>
              ${ordenSobre.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSobre.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSobre.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSobre.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${sobre.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSobre.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSticker(ordenSticker) {
      const sticker = ordenSticker.sticker;

      const medida = sticker.medidaSticker.medida === 'OTRA'
        ? sticker.medidaPersonalizada
        : sticker.medidaSticker.medida;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSticker.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSticker.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSticker.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSticker.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSticker.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSticker.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSticker.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSticker.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Tipo de troquelado: </strong>${sticker.tipoTroqueladoSticker.tipo}</p>
              <p><strong>Adicional diseño: </strong>${sticker.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sticker.enlaceArchivo && sticker.enlaceArchivo.trim() !== ''
                  ? `<a href="${sticker.enlaceArchivo}" target="_blank">${sticker.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${sticker.informacionAdicional && sticker.informacionAdicional.trim() !== '' ? sticker.informacionAdicional : ' - '}</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSticker.ordenTrabajo.fechaEntrega}</p>
              ${ordenSticker.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSticker.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSticker.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSticker.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${sticker.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSticker.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenSublimacion(ordenSublimacion) {
      const sublimacion = ordenSublimacion.sublimacion;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenSublimacion.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenSublimacion.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenSublimacion.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenSublimacion.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenSublimacion.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenSublimacion.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenSublimacion.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenSublimacion.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Material: </strong>${sublimacion.materialSublimacion.material}</p>
              <p><strong>Adicional diseño: </strong>${sublimacion.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                sublimacion.enlaceArchivo && sublimacion.enlaceArchivo.trim() !== ''
                  ? `<a href="${sublimacion.enlaceArchivo}" target="_blank">${sublimacion.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                sublimacion.informacionAdicional && sublimacion.informacionAdicional.trim() !== ''
                  ? sublimacion.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenSublimacion.ordenTrabajo.fechaEntrega}</p>
              ${ordenSublimacion.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenSublimacion.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenSublimacion.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenSublimacion.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${sublimacion.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenSublimacion.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenTalonario(ordenTalonario) {
      const talonario = ordenTalonario.talonario;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenTalonario.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenTalonario.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenTalonario.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenTalonario.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenTalonario.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenTalonario.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenTalonario.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenTalonario.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de talonario: </strong>${talonario.tipoTalonario.tipo}</p>
              <p><strong>Tipo de troquelado: </strong>${talonario.tipoTroqueladoTalonario.tipo}</p>
              <p><strong>Modo del talonario: </strong>${talonario.modoTalonario.modo}</p>
              <p><strong>Tipo de color: </strong>${talonario.tipoColorTalonario.tipo}</p>
              <p><strong>Tipo de papel: </strong>${talonario.tipoPapelTalonario.tipo}</p>
              <p><strong>Cantidad de hojas: </strong>${talonario.cantidadHojas}</p>
              <p><strong>Medida: </strong>${
                talonario.medidaTalonario.medida === 'OTRA'
                  ? talonario.medidaPersonalizada
                  : talonario.medidaTalonario.medida
              }</p>
              <p><strong>Adicional diseño: </strong>${talonario.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                talonario.enlaceArchivo && talonario.enlaceArchivo.trim() !== ''
                  ? `<a href="${talonario.enlaceArchivo}" target="_blank">${talonario.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                talonario.informacionAdicional && talonario.informacionAdicional.trim() !== ''
                  ? talonario.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenTalonario.ordenTrabajo.fechaEntrega}</p>
              ${ordenTalonario.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenTalonario.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenTalonario.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenTalonario.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${talonario.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenTalonario.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenTarjeta(ordenTarjeta) {
      const tarjeta = ordenTarjeta.tarjeta;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenTarjeta.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenTarjeta.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenTarjeta.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenTarjeta.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenTarjeta.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenTarjeta.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenTarjeta.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenTarjeta.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de papel: </strong>${tarjeta.tipoPapelTarjeta.tipo}</p>
              <p><strong>Tipo de color: </strong>${tarjeta.tipoColorTarjeta.tipo}</p>
              <p><strong>Tipo de faz: </strong>${tarjeta.tipoFazTarjeta.tipo}</p>
              <p><strong>Tipo de laminado: </strong>${tarjeta.tipoLaminadoTarjeta.laminado}</p>
              <p><strong>Medida: </strong>${
                tarjeta.medidaTarjeta.medida === 'OTRA'
                  ? tarjeta.medidaPersonalizada
                  : tarjeta.medidaTarjeta.medida
              }</p>
              <p><strong>Adicional diseño: </strong>${tarjeta.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                tarjeta.enlaceArchivo && tarjeta.enlaceArchivo.trim() !== ''
                  ? `<a href="${tarjeta.enlaceArchivo}" target="_blank">${tarjeta.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                tarjeta.informacionAdicional && tarjeta.informacionAdicional.trim() !== ''
                  ? tarjeta.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenTarjeta.ordenTrabajo.fechaEntrega}</p>
              ${ordenTarjeta.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenTarjeta.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenTarjeta.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenTarjeta.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${tarjeta.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenTarjeta.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenTurnero(ordenTurnero) {
      const turnero = ordenTurnero.turnero;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenTurnero.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenTurnero.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenTurnero.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenTurnero.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenTurnero.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenTurnero.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenTurnero.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenTurnero.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de color: </strong>${turnero.tipoColorTurnero.tipo}</p>
              <p><strong>Cantidad de hojas: </strong>${turnero.cantidadHojas}</p>
              <p><strong>Medida: </strong>${
                turnero.medidaTurnero.medida === 'OTRA'
                  ? turnero.medidaPersonalizada
                  : turnero.medidaTurnero.medida
              }</p>
              <p><strong>Adicional diseño: </strong>${turnero.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                turnero.enlaceArchivo && turnero.enlaceArchivo.trim() !== ''
                  ? `<a href="${turnero.enlaceArchivo}" target="_blank">${turnero.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                turnero.informacionAdicional && turnero.informacionAdicional.trim() !== ''
                  ? turnero.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenTurnero.ordenTrabajo.fechaEntrega}</p>
              ${ordenTurnero.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenTurnero.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenTurnero.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenTurnero.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${turnero.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenTurnero.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenVinilo(ordenVinilo) {
      const vinilo = ordenVinilo.vinilo;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenVinilo.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenVinilo.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenVinilo.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenVinilo.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenVinilo.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenVinilo.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenVinilo.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenVinilo.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Tipo de vinilo: </strong>${vinilo.tipoVinilo.tipo}</p>
              <p><strong>Tipo de adicional: </strong>${vinilo.tipoAdicionalVinilo.adicional}</p>
              <p><strong>Tipo de corte: </strong>${vinilo.tipoCorteVinilo.tipo}</p>
              <p><strong>Medida: </strong>${
                vinilo.medidaVinilo.medida === 'OTRA'
                  ? vinilo.medidaPersonalizada
                  : vinilo.medidaVinilo.medida
              }</p>
              <p><strong>Adicional diseño: </strong>${vinilo.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                vinilo.enlaceArchivo && vinilo.enlaceArchivo.trim() !== ''
                  ? `<a href="${vinilo.enlaceArchivo}" target="_blank">${vinilo.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                vinilo.informacionAdicional && vinilo.informacionAdicional.trim() !== ''
                  ? vinilo.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenVinilo.ordenTrabajo.fechaEntrega}</p>
              ${ordenVinilo.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenVinilo.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenVinilo.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenVinilo.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${vinilo.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenVinilo.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenViniloDeCorte(ordenViniloDeCorte) {
      const vinilo = ordenViniloDeCorte.viniloDeCorte;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${ordenViniloDeCorte.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${ordenViniloDeCorte.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${ordenViniloDeCorte.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${ordenViniloDeCorte.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${ordenViniloDeCorte.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${ordenViniloDeCorte.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${ordenViniloDeCorte.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${ordenViniloDeCorte.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Trae material: </strong>${vinilo.traeMaterialVinilo.material}</p>
              <p><strong>Medida: </strong>${vinilo.medida}</p>
              <p><strong>Código color: </strong>${
                vinilo.codigoColor && vinilo.codigoColor.trim() !== ''
                  ? `<span>${vinilo.codigoColor}</span>`
                  : ' - '
              }</p>
              <p><strong>Es promocional: </strong>${vinilo.esPromocional ? 'Sí' : 'No'}</p>
              <p><strong>Es oracal: </strong>${vinilo.esOracal ? 'Sí' : 'No'}</p>
              <p><strong>Con colocación: </strong>${vinilo.conColocacion ? 'Sí' : 'No'}</p>
              <p><strong>Adicional diseño: </strong>${vinilo.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                vinilo.enlaceArchivo && vinilo.enlaceArchivo.trim() !== ''
                  ? `<a href="${vinilo.enlaceArchivo}" target="_blank">${vinilo.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                vinilo.informacionAdicional && vinilo.informacionAdicional.trim() !== ''
                  ? vinilo.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${ordenViniloDeCorte.ordenTrabajo.fechaEntrega}</p>
              ${ordenViniloDeCorte.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenViniloDeCorte.ordenTrabajo.horaEntrega}</p>` : ''}
              ${ordenViniloDeCorte.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenViniloDeCorte.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${vinilo.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenViniloDeCorte.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenViniloPlasticoCorrugado(orden) {
      const vinilo = orden.viniloPlasticoCorrugado;
      const medida = vinilo.medidaViniloPlasticoCorrugado.medida === 'OTRA'
        ? vinilo.medidaPersonalizada
        : vinilo.medidaViniloPlasticoCorrugado.medida;

      return `
        <div class="row mt-4">
          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del cliente</h6>
              <hr class="divider">
              <p><strong>Cliente: </strong>${orden.ordenTrabajo.nombreCliente}</p>
              <p><strong>Teléfono: </strong>${orden.ordenTrabajo.telefonoCliente}</p>
              <p><strong>Cuenta corriente: </strong>${orden.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
            </div>

            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del pago</h6>
              <hr class="divider">
              <p><strong>Pedido: </strong>${orden.ordenTrabajo.fechaPedido}</p>
              <p><strong>Total: </strong>$${orden.ordenTrabajo.total}</p>
              <p><strong>Entrega: </strong>$${orden.ordenTrabajo.abonado}</p>
              <p><strong>Necesita factura: </strong>${orden.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
              <p><strong>Vendedor/a: </strong>${orden.ordenTrabajo.empleado.nombre}</p>
            </div>
          </div>

          <div class="col-6">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
              <h6>Datos del producto</h6>
              <hr class="divider">
              <p><strong>Medida: </strong>${medida}</p>
              <p><strong>Con ojales: </strong>${vinilo.conOjales ? 'Sí' : 'No'}</p>
              <p><strong>Adicional diseño: </strong>${vinilo.conAdicionalDisenio ? 'Sí' : 'No'}</p>
              <p><strong>Enlace al archivo: </strong>${
                vinilo.enlaceArchivo && vinilo.enlaceArchivo.trim() !== ''
                  ? `<a href="${vinilo.enlaceArchivo}" target="_blank">${vinilo.enlaceArchivo}</a>`
                  : ' - '
              }</p>
              <p><strong>Información adicional: </strong>${
                vinilo.informacionAdicional && vinilo.informacionAdicional.trim() !== ''
                  ? vinilo.informacionAdicional
                  : ' - '
              }</p>

              <hr class="divider">
              <p><strong>Fecha de entrega: </strong>${orden.ordenTrabajo.fechaEntrega}</p>
              ${orden.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${orden.ordenTrabajo.horaEntrega}</p>` : ''}
              ${orden.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${orden.ordenTrabajo.fechaMuestra}</p>` : ''}
              <p><strong>Cantidad de productos: </strong>${vinilo.cantidad}</p>
            </div>
          </div>
          <div class="col-12">
            <div class="card mb-3 border contenedor-datos-orden-impresion">
                <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${orden.ordenTrabajo.resta}</p>
            </div>
          </div>
        </div>
      `;
    }
    function renderOrdenVoucher(ordenVoucher) {
      return `
      <div class="row mt-4">
        <div class="col-6">
          <div class="card mb-3 border contenedor-datos-orden-impresion">
            <h6>Datos del cliente</h6>
            <hr class="divider">
            <p><strong>Cliente: </strong>${ordenVoucher.ordenTrabajo.nombreCliente}</p>
            <p><strong>Teléfono: </strong>${ordenVoucher.ordenTrabajo.telefonoCliente}</p>
            <p><strong>Cuenta corriente: </strong>${ordenVoucher.ordenTrabajo.esCuentaCorriente ? 'Sí' : 'No'}</p>
          </div>

          <div class="card mb-3 border contenedor-datos-orden-impresion">
            <h6>Datos del pago</h6>
            <hr class="divider">
            <p><strong>Pedido: </strong>${ordenVoucher.ordenTrabajo.fechaPedido}</p>
            <p><strong>Total: </strong>$${ordenVoucher.ordenTrabajo.total}</p>
            <p><strong>Entrega: </strong>$${ordenVoucher.ordenTrabajo.abonado}</p>
            <p><strong>Necesita factura: </strong>${ordenVoucher.ordenTrabajo.necesitaFactura ? 'Sí' : 'No'}</p>
            <p><strong>Vendedor/a: </strong>${ordenVoucher.ordenTrabajo.empleado.nombre}</p>
          </div>
        </div>

        <div class="col-6">
          <div class="card mb-3 border contenedor-datos-orden-impresion">
            <h6>Datos del producto</h6>
            <hr class="divider">
            <p><strong>Tipo de faz: </strong>${ordenVoucher.voucher.tipoFazVoucher.tipo}</p>
            <p><strong>Tipo de papel: </strong>${ordenVoucher.voucher.tipoPapelVoucher.tipo === 'OTRO' ? ordenVoucher.voucher.tipoPapelPersonalizado : ordenVoucher.voucher.tipoPapelVoucher.tipo}</p>
            <p><strong>Medida: </strong>${ordenVoucher.voucher.medidaVoucher.medida === 'OTRA' ? ordenVoucher.voucher.medidaPersonalizada : ordenVoucher.voucher.medidaVoucher.medida}</p>
            <p><strong>Adicional diseño: </strong>${ordenVoucher.voucher.conAdicionalDisenio ? 'Sí' : 'No'}</p>
            <p><strong>Enlace al archivo: </strong>${ordenVoucher.voucher.enlaceArchivo ? `<a href="${ordenVoucher.voucher.enlaceArchivo}" target="_blank">${ordenVoucher.voucher.enlaceArchivo}</a>` : '-'}</p>
            <p><strong>Información adicional: </strong>${ordenVoucher.voucher.informacionAdicional || '-'}</p>
            <hr class="divider">
            <p><strong>Fecha de entrega: </strong>${ordenVoucher.ordenTrabajo.fechaEntrega}</p>
            ${ordenVoucher.ordenTrabajo.horaEntrega ? `<p><strong>Hora de entrega: </strong>${ordenVoucher.ordenTrabajo.horaEntrega}</p>` : ''}
            ${ordenVoucher.ordenTrabajo.fechaMuestra ? `<p><strong>Fecha de muestra: </strong>${ordenVoucher.ordenTrabajo.fechaMuestra}</p>` : ''}
            <p><strong>Cantidad de productos: </strong>${ordenVoucher.voucher.cantidad}</p>
          </div>
        </div>
        <div class="col-12">
          <div class="card mb-3 border contenedor-datos-orden-impresion">
              <p class="pago-restante d-flex justify-content-center align-items-center mb-0">Restante: $${ordenVoucher.ordenTrabajo.resta}</p>
          </div>
        </div>
      </div>
      `;
    }

    // CAMBIOS DE ESTADO
    /*document.querySelectorAll('.corregir-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoACorregir(ordenId);
        });
    });*/

    document.querySelectorAll('.pasar-en-proceso-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoAEnProceso(ordenId);
        });
    });

    document.querySelectorAll('.pasar-lista-para-retirar-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoAListaParaRetirar(ordenId);
        });
    });

    document.querySelectorAll('.pasar-retirada-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            const ordenId = boton.dataset.idorden;
            const resta = boton.dataset.resta;

            if (resta != 0) {
                const confirmar = confirm("¡ADVERTENCIA! El producto no fue abonado por completo.\n\n¿Desea continuar de todos modos?");
                if (!confirmar) {
                    return;
                }
            }
            document.getElementById('spinner-overlay').style.display = 'flex';
            cambiarEstadoARetirada(ordenId);
        });
    });

    document.querySelectorAll('.pasar-corregido-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoaSinHacer(ordenId);
        });
    });

    document.querySelectorAll('.btn-lupa').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
        });
    });

    function cambiarEstadoACorregir(ordenId) {
        fetch(`/api/orden/cambiar-a-corregir/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoAEnProceso(ordenId) {
        fetch(`/api/orden/cambiar-a-en-proceso/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoAListaParaRetirar(ordenId) {
        fetch(`/api/orden/cambiar-a-lista-para-retirar/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoARetirada(ordenId) {
        fetch(`/api/orden/cambiar-a-retirada/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoaSinHacer(ordenId) {
        fetch(`/api/orden/cambiar-a-sin-hacer/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }

    const select = document.getElementById('selector-producto');
      const form = document.getElementById('formulario-selector-producto');

      select.addEventListener('change', () => {
        document.getElementById('spinner-overlay').style.display = 'flex';
        form.submit();
      });
});

window.addEventListener('load', () => {
  document.getElementById('spinner-overlay').style.display = 'none';
});
