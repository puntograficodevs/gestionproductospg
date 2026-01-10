document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const anilladoCheckbox = document.getElementById('impresion-es-anillado');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioAnilladoInput = document.getElementById('precioAnillado');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const radiosColor = document.querySelectorAll('input[name="tipoColorImpresion.id"]');
      const radiosTamanio = document.querySelectorAll('input[name="tamanioHojaImpresion.id"]');
      const radiosFaz = document.querySelectorAll('input[name="tipoFazImpresion.id"]');
      const radiosPapel = document.querySelectorAll('input[name="tipoPapelImpresion.id"]');
      const radiosTipo = document.querySelectorAll('input[name="tipoImpresion.id"]');
      const cantidadOpcion = document.getElementById('cantidadImpresionId');
      const cantidadImpresionesInput = document.getElementById('cantidad');
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

      function resetearPrecio() {
        precioProductoInput.value = 0;
        precioProductoInput.readOnly = false;
      }

      function mapearOpcionCantidad() {
          const cantidadHojas = parseInt(cantidadImpresionesInput.value) || 0;

          if (cantidadHojas <= 10) {
              cantidadOpcion.value = 1;
              return 1;
          }
          if (cantidadHojas <= 100) {
              cantidadOpcion.value = 2;
              return 2;
          }
          if (cantidadHojas > 100) {
              cantidadOpcion.value = 3;
              return 3;
          }
      }

      async function calcularPrecio() {
        const colorSeleccionado = document.querySelector('input[name="tipoColorImpresion.id"]:checked');
        const tamanioSeleccionado = document.querySelector('input[name="tamanioHojaImpresion.id"]:checked');
        const fazSeleccionada = document.querySelector('input[name="tipoFazImpresion.id"]:checked');
        const papelSeleccionado = document.querySelector('input[name="tipoPapelImpresion.id"]:checked');
        const cantidadSeleccionada = mapearOpcionCantidad();
        const tipoSeleccionado = document.querySelector('input[name="tipoImpresion.id"]:checked');
        let tipoColorImpresionId = 0;
        let tamanioHojaImpresionId = 0;
        let tipoFazImpresionId = 0;
        let tipoPapelImpresionId = 0;
        let cantidadImpresionId = 0;
        let tipoImpresionId = 0;
        let precioProducto = 0;

        if (!colorSeleccionado || !tamanioSeleccionado || !fazSeleccionada || !papelSeleccionado || !cantidadSeleccionada || !tipoSeleccionado) {
            return;
        } else {
            tipoColorImpresionId = Number(colorSeleccionado.value);
            tamanioHojaImpresionId = Number(tamanioSeleccionado.value);
            tipoFazImpresionId = Number(fazSeleccionada.value);
            tipoPapelImpresionId = Number(papelSeleccionado.value);
            cantidadImpresionId = cantidadSeleccionada;
            tipoImpresionId = Number(tipoSeleccionado.value);
        }

        try {
            const response = await fetch(`/api/plantilla-impresion/precio?tipoColorImpresionId=${tipoColorImpresionId}&tamanioHojaImpresionId=${tamanioHojaImpresionId}&tipoFazImpresionId=${tipoFazImpresionId}&tipoPapelImpresionId=${tipoPapelImpresionId}&cantidadImpresionId=${cantidadImpresionId}&tipoImpresionId=${tipoImpresionId}`);
            if (response.status === 200) {
                let precioUnitario = await response.json();
                const cantidad = parseInt(cantidadImpresionesInput.value) || 0;
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

        const precioAnillado = anilladoCheckbox.checked ? calcularPrecioAnillado() : 0;

        // Subtotal = producto + diseño
        let subtotal = precioProducto + precioAnillado;

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
        precioImpuestosInput.value = impuestoFactura + recargoCreditoMonto;
        totalInput.value = total;
        restaInput.value = resta;
        precioProductoInput.value = precioProducto;
        precioAnilladoInput.value = precioAnillado;
      }

      function calcularPrecioAnillado() {
           const fazSeleccionada = document.querySelector('input[name="tipoFazImpresion.id"]:checked');
           let cantidadHojas = parseInt(cantidadImpresionesInput.value) || 0;

           if (fazSeleccionada && Number(fazSeleccionada.value) === 2) {
                cantidadHojas = cantidadHojas / 2;
           }

           const cantidadAnillados = Math.ceil(cantidadHojas / 400);


           if (cantidadAnillados === 1) {
                return calcularPrecioSegunCantidadHojas(cantidadHojas);
           } else {
                const cantidadHojasPorParte = cantidadHojas / cantidadAnillados;
                const precioParcial = calcularPrecioSegunCantidadHojas(cantidadHojasPorParte);
                return precioParcial * cantidadAnillados;
           }
      }

      function calcularPrecioSegunCantidadHojas(cantidadHojas) {
            const precioMenos20 = 890;
            const precioEntre20Y40 = 980;
            const precioEntre40Y60 = 1100;
            const precioEntre60Y100 = 1300;
            const precioEntre100Y150 = 1500;
            const precioEntre150Y200 = 1950;
            const precioEntre200Y300 = 2500;
            const precioEntre300Y400 = 2800;

            if (cantidadHojas <= 20) {
                return precioMenos20;
            } else if (cantidadHojas <= 40) {
                return precioEntre20Y40;
            } else if (cantidadHojas <= 60) {
                return precioEntre40Y60;
            } else if (cantidadHojas <= 100) {
                return precioEntre60Y100;
            } else if (cantidadHojas <= 150) {
                return precioEntre100Y150;
            } else if (cantidadHojas <= 200) {
                return precioEntre150Y200;
            } else if (cantidadHojas <= 300) {
                return precioEntre200Y300;
            } else if (cantidadHojas <= 400) {
                return precioEntre300Y400;
            }

            return precioEntre300Y400;
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
      cantidadImpresionesInput.addEventListener('input', calcularPrecio);
      necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
      anilladoCheckbox.addEventListener('change', calcularPrecio);
      abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
      totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
      radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));
      radiosColor.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosTamanio.forEach(radio => radio.addEventListener('change', () => {
          resetearPrecio();
          calcularPrecio();
      }));
      radiosFaz.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosPapel.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosTipo.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));

      // Llamamos al inicio para inicializar los valores
      calcularPrecio();
});
