document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const precioDisenio = 5000;
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const adicionalCheckbox = document.getElementById('carpeta-con-adicional-disenio');
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioDisenioInput = document.getElementById('precioDisenio');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const radiosLaminado = document.querySelectorAll('input[name="tipoLaminadoCarpetaSolapa.id"]');
      const radiosFaz = document.querySelectorAll('input[name="tipoFazCarpetaSolapa.id"]');
      const cantidadCarpetasInput = document.getElementById('carpeta-cantidad');
      const totalInicial = totalInput.value;


      // Inicializamos valores visibles
      precioDisenioInput.value = 0;
      precioImpuestosInput.value = 0;
      restaInput.value = 0;

      // Toggles
      let toggleFechaMuestra = document.getElementById('toggleFechaMuestra');
      const fechaMuestraRow = document.getElementById('fechaMuestraRow');
      toggleFechaMuestra.addEventListener('change', () => {
          fechaMuestraRow.classList.toggle('d-none', !toggleFechaMuestra.checked);
      });

      function resetearPrecio() {
        precioProductoInput.value = 0;
        precioProductoInput.readOnly = false;
      }

      async function calcularPrecio() {
        const tipoLaminadoSeleccionado = document.querySelector('input[name="tipoLaminadoCarpetaSolapa.id"]:checked');
        const tipoFazSeleccionada = document.querySelector('input[name="tipoFazCarpetaSolapa.id"]:checked');
        const cantidad = parseInt(cantidadCarpetasInput.value, 10) || 0;
        let tipoLaminadoCarpetaSolapaId = 0;
        let tipoFazCarpetaSolapaId = 0;
        let precioProducto = 0;

        if (!tipoLaminadoSeleccionado || !tipoFazSeleccionada || !cantidad) {
            return;
        } else {
            tipoLaminadoCarpetaSolapaId = Number(tipoLaminadoSeleccionado.value);
            tipoFazCarpetaSolapaId = Number(tipoFazSeleccionada.value);
        }

        try {
            const response = await fetch(`/api/plantilla-carpeta-solapa/precio?cantidad=${cantidad}&tipoLaminadoCarpetaSolapaId=${tipoLaminadoCarpetaSolapaId}&tipoFazCarpetaSolapaId=${tipoFazCarpetaSolapaId}`);
            if (response.status === 200) {
                let precioUnitario = await response.json();
                if (cantidad < 50) {
                    precioProducto = precioUnitario * cantidad;
                }  else {
                    precioProducto = precioUnitario;
                }
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

        const precioDisenioActual = adicionalCheckbox.checked ? precioDisenio : 0;

        // Subtotal = producto + diseño
        let subtotal = precioProducto + precioDisenioActual;

        // Impuesto por factura
        let impuestoFactura = 0;
        if (necesitaFacturaCheckbox.checked) {
            impuestoFactura = Math.ceil(subtotal * recargoFactura);
        }

        // Total inicial con impuesto
        let total = (totalInicial != 0) ? totalInicial : subtotal + impuestoFactura;

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
      cantidadCarpetasInput.addEventListener('input', () => {
        resetearPrecio();
        calcularPrecio();
      });
      adicionalCheckbox.addEventListener('change', calcularPrecio);
      necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
      abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
      totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
      radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));

      radiosLaminado.forEach(radio => radio.addEventListener('change', () => {
          resetearPrecio();
          calcularPrecio();
      }));

      radiosFaz.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));

      // Llamamos al inicio para inicializar los valores
      calcularPrecio();
});
