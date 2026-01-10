document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const precioDisenio = 6000;
      const precioPerilla = 2800;
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const adicionalDisenioCheckbox = document.getElementById('sello-madera-con-adicional-disenio');
      const adicionalPerillaCheckbox = document.getElementById('sello-madera-con-adicional-perilla');
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioDisenioInput = document.getElementById('precioDisenio');
      const precioPerillaInput = document.getElementById('precioPerilla');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const radiosTamanio = document.querySelectorAll('input[name="tamanioSelloMadera.id"]');
      const cantidadSellosMaderaInput = document.getElementById('cantidad');
      const totalInicial = totalInput.value;

      // Inicializamos valores visibles
      precioDisenioInput.value = 0;
      precioPerillaInput.value = 0;
      precioImpuestosInput.value = 0;
      restaInput.value = 0;

      // Toggles
      let toggleFechaMuestra = document.getElementById('toggleFechaMuestra');
      const fechaMuestraRow = document.getElementById('fechaMuestraRow');
      toggleFechaMuestra.addEventListener('change', () => {
          fechaMuestraRow.classList.toggle('d-none', !toggleFechaMuestra.checked);
      });

      const tamanioInputRow = document.getElementById('tamanioPersonalizadoGroup');
      radiosTamanio.forEach(radio => {
        radio.addEventListener('change', () => {
          const label = document.querySelector(`label[for="${radio.id}"]`);
          const texto = label?.textContent.trim().toLowerCase();
          const esOtro = texto === 'otro';
          tamanioInputRow.classList.toggle('d-none', !esOtro);
        });
      });

      function resetearPrecio() {
        precioProductoInput.value = 0;
        precioProductoInput.readOnly = false;
      }

      async function calcularPrecio() {
        const tamanioSeleccionado = document.querySelector('input[name="tamanioSelloMadera.id"]:checked');
        let tamanioSelloMaderaId = 0;
        let precioProducto = 0;
        const cantidad = parseInt(cantidadSellosMaderaInput.value, 10) || 0;

        if (!tamanioSeleccionado) {
            return;
        } else {
            tamanioSelloMaderaId = Number(tamanioSeleccionado.value);
        }

        try {
            const response = await fetch(`/api/plantilla-sello-madera/precio?tamanioSelloMaderaId=${tamanioSelloMaderaId}`);
            if (response.status === 200) {
                let precioUnitario = await response.json();
                precioProducto = precioUnitario * cantidad;
                precioProductoInput.readOnly = true;
            } else if (response.status === 204) {
                precioProductoInput.readOnly = false;
                precioProducto = parseInt(precioProductoInput.value, 10) || 0;
            } else {
                console.error('Error al obtener precio base');
            }
        } catch (error) {
            console.error('Error en la conexión:', error);
        }

        const precioDisenioActual = adicionalDisenioCheckbox.checked ? precioDisenio : 0;
        const precioPerillaActual = adicionalPerillaCheckbox.checked ? precioPerilla * cantidad : 0;

        // Subtotal = producto + diseño
        let subtotal = precioProducto + precioDisenioActual + precioPerillaActual;

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
        precioPerillaInput.value = precioPerillaActual;
        precioImpuestosInput.value = impuestoFactura + recargoCreditoMonto;
        totalInput.value = total;
        restaInput.value = resta;
        precioProductoInput.value = precioProducto;
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
      cantidadSellosMaderaInput.addEventListener('input', calcularPrecio);
      adicionalDisenioCheckbox.addEventListener('change', calcularPrecio);
      adicionalPerillaCheckbox.addEventListener('change', calcularPrecio);
      necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
      abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
      totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
      radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));
      radiosTamanio.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));

      // Llamamos al inicio para inicializar los valores
      calcularPrecio();
});
