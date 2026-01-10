document.addEventListener('DOMContentLoaded', function () {
  // Valores predefinidos
  const recargoFactura = 0.21; // 21% sobre subtotal
  const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

  // Inputs y checkboxes
  const adicionalCheckbox = document.getElementById('vinilo-de-corte-con-adicional-disenio');
  const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
  const precioProductoInput = document.getElementById('precioProducto');
  const precioDisenioInput = document.getElementById('precioDisenio');
  const precioImpuestosInput = document.getElementById('precioImpuestos');
  const totalInput = document.getElementById('total');
  const abonadoInput = document.getElementById('abonado');
  const restaInput = document.getElementById('resta');
  const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
  const totalInicial = totalInput.value;

  // Inicializamos valores visibles
  precioImpuestosInput.value = 0;
  restaInput.value = 0;

  // Toggles
  let toggleFechaMuestra = document.getElementById('toggleFechaMuestra');
  const fechaMuestraRow = document.getElementById('fechaMuestraRow');
  toggleFechaMuestra.addEventListener('change', () => {
      fechaMuestraRow.classList.toggle('d-none', !toggleFechaMuestra.checked);
  });

  function calcularPrecio() {
    const precioProducto = parseInt(precioProductoInput.value, 10) || 0;
    let precioDisenioActual = parseInt(precioDisenioInput.value, 10) || 0;
    if (!adicionalCheckbox.checked) {
      precioDisenioActual = 0;
    }

    // Subtotal = producto + diseño
    let subtotal = Math.ceil(precioProducto + precioDisenioActual);

    // Impuesto por factura
    let impuestoFactura = 0;
    if (necesitaFacturaCheckbox.checked) {
      impuestoFactura = Math.ceil(subtotal * recargoFactura);
    }

    // Total inicial con impuesto
    let total = (totalInicial != 0) ? totalInicial : Math.ceil(subtotal + impuestoFactura);

    // Recargo por crédito
    const medioPagoSeleccionado = document.querySelector('input[name="medioPago.id"]:checked');
    let recargoCreditoMonto = 0;
    if ((medioPagoSeleccionado && Number(medioPagoSeleccionado.value) === 2) && !(totalInicial != 0)) {
      recargoCreditoMonto = Math.ceil(total * recargoCredito);
      total += recargoCreditoMonto;
    }

    // Cantidad abonada
    const abonado = parseInt(abonadoInput.value, 10) || 0;

    // Resta
    const resta = total - abonado;

    // Actualizamos inputs visibles
    precioDisenioInput.value = precioDisenioActual;
    precioImpuestosInput.value = impuestoFactura + recargoCreditoMonto;
    totalInput.value = total;
    restaInput.value = resta;
  }

  function revisarSiAbonadoEstaBien() {
      const total = parseFloat(totalInput.value) || 0;
      const abonado = parseFloat(abonadoInput.value) || 0;

      if (abonado > total) {
        abonadoInput.classList.add('is-invalid');
        restaInput.classList.add('is-invalid');
      } else {
        abonadoInput.classList.remove('is-invalid');
        restaInput.classList.remove('is-invalid');
      }

  	restaInput.value = total - abonado;
  }

  // Escuchamos cambios en todos los inputs
  precioProductoInput.addEventListener('input', calcularPrecio);
  precioDisenioInput.addEventListener('input', calcularPrecio);
  adicionalCheckbox.addEventListener('change', calcularPrecio);
  necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
  abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
  totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
  radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));

  // Llamamos al inicio para inicializar los valores
  calcularPrecio();
});
