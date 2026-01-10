    document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const precioDisenio = 5000;
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const adicionalCheckbox = document.getElementById('entrada-con-adicional-disenio');
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioDisenioInput = document.getElementById('precioDisenio');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const radiosMedida = document.querySelectorAll('input[name="medidaEntrada.id"]');
      const radiosCantidad = document.querySelectorAll('input[name="cantidadEntrada.id"]');
      const radiosPapel = document.querySelectorAll('input[name="tipoPapelEntrada.id"]');
      const radiosColor = document.querySelectorAll('input[name="tipoColorEntrada.id"]');
      const radiosTroquelado = document.querySelectorAll('input[name="tipoTroqueladoEntrada.id"]');
      const radiosNumerado = document.querySelectorAll('input[name="numeradoEntrada.id"]');
      const radiosTerminacion = document.querySelectorAll('input[name="terminacionEntrada.id"]');
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

      const cantidadInputRow = document.getElementById('cantidadPersonalizadaGroup');
      radiosCantidad.forEach(radio => {
        radio.addEventListener('change', () => {
          const label = document.querySelector(`label[for="${radio.id}"]`);
          const esOtra = label?.textContent.trim().toLowerCase() === 'otra';
          cantidadInputRow.classList.toggle('d-none', !esOtra);
        });
      });

      const medidaInputRow = document.getElementById('medidaPersonalizadaGroup');
      radiosMedida.forEach(radio => {
        radio.addEventListener('change', () => {
          const label = document.querySelector(`label[for="${radio.id}"]`);
          const esOtra = label?.textContent.trim().toLowerCase() === 'otra';
          medidaInputRow.classList.toggle('d-none', !esOtra);
        });
      });

      function resetearPrecio() {
        precioProductoInput.value = 0;
        precioProductoInput.readOnly = false;
      }

      async function calcularPrecio() {
        const papelSeleccionado = document.querySelector('input[name="tipoPapelEntrada.id"]:checked');
        const colorSeleccionado = document.querySelector('input[name="tipoColorEntrada.id"]:checked');
        const troqueladoSeleccionado = document.querySelector('input[name="tipoTroqueladoEntrada.id"]:checked');
        const medidaSeleccionada = document.querySelector('input[name="medidaEntrada.id"]:checked');
        const cantidadSeleccionada = document.querySelector('input[name="cantidadEntrada.id"]:checked');
        const numeradoSeleccionado = document.querySelector('input[name="numeradoEntrada.id"]:checked');
        const terminacionSeleccionada = document.querySelector('input[name="terminacionEntrada.id"]:checked');
        let tipoPapelEntradaId = 0;
        let tipoColorEntradaId = 0;
        let tipoTroqueladoEntradaId = 0;
        let medidaEntradaId = 0;
        let cantidadEntradaId = 0;
        let numeradoEntradaId = 0;
        let terminacionEntradaId = 0;
        let precioProducto = 0;

        if (!papelSeleccionado || !colorSeleccionado || !troqueladoSeleccionado || !medidaSeleccionada || !cantidadSeleccionada || !numeradoSeleccionado || !terminacionSeleccionada) {
            return;
        } else {
            tipoPapelEntradaId = Number(papelSeleccionado.value);
            tipoColorEntradaId = Number(colorSeleccionado.value);
            tipoTroqueladoEntradaId = Number(troqueladoSeleccionado.value);
            medidaEntradaId = Number(medidaSeleccionada.value);
            cantidadEntradaId = Number(cantidadSeleccionada.value);
            numeradoEntradaId = Number(numeradoSeleccionado.value);
            terminacionEntradaId = Number(terminacionSeleccionada.value);
        }

        try {
            const response = await fetch(`/api/plantilla-entrada/precio?tipoPapelEntradaId=${tipoPapelEntradaId}&tipoColorEntradaId=${tipoColorEntradaId}&tipoTroqueladoEntradaId=${tipoTroqueladoEntradaId}&medidaEntradaId=${medidaEntradaId}&cantidadEntradaId=${cantidadEntradaId}&numeradoEntradaId=${numeradoEntradaId}&terminacionEntradaId=${terminacionEntradaId}`);
            if (response.status === 200) {
                precioProducto = await response.json();
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
      adicionalCheckbox.addEventListener('change', calcularPrecio);
      necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
      abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
      totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
      radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));
      radiosTroquelado.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosPapel.forEach(radio => radio.addEventListener('change', () => {
          resetearPrecio();
          calcularPrecio();
      }));
      radiosColor.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosNumerado.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosTerminacion.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosMedida.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosCantidad.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));

      // Llamamos al inicio para inicializar los valores
      calcularPrecio();
});
