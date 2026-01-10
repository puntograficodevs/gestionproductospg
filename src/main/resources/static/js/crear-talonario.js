document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const precioDisenio = 5000;
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const adicionalCheckbox = document.getElementById('talonario-con-adicional-disenio');
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioDisenioInput = document.getElementById('precioDisenio');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const cantidadHojasInput = document.getElementById('talonario-cantidad-hojas');
      const conNumeradoCheckbox = document.getElementById('talonario-con-numerado');
      const radiosTipo = document.querySelectorAll('input[name="tipoTalonario.id"]');
      const radiosTroquelado = document.querySelectorAll('input[name="tipoTroqueladoTalonario.id"]');
      const radiosModo = document.querySelectorAll('input[name="modoTalonario.id"]');
      const radiosColor = document.querySelectorAll('input[name="tipoColorTalonario.id"]');
      const radiosMedida = document.querySelectorAll('input[name="medidaTalonario.id"]');
      const radiosPapel = document.querySelectorAll('input[name="tipoPapelTalonario.id"]');
      const radiosCantidad = document.querySelectorAll('input[name="cantidadTalonario.id"]');
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

      const medidaInputRow = document.getElementById('medidaPersonalizadaGroup');
      radiosMedida.forEach(radio => {
        radio.addEventListener('change', () => {
          const label = document.querySelector(`label[for="${radio.id}"]`);
          const esOtra = label?.textContent.trim().toLowerCase() === 'otra';
          medidaInputRow.classList.toggle('d-none', !esOtra);
        });
      });

      const cantidadInputRow = document.getElementById('cantidadPersonalizadaGroup');
      radiosCantidad.forEach(radio => {
        radio.addEventListener('change', () => {
          const label = document.querySelector(`label[for="${radio.id}"]`);
          const esOtra = label?.textContent.trim().toLowerCase() === 'otra';
          cantidadInputRow.classList.toggle('d-none', !esOtra);
        });
      });

      function resetearPrecio() {
        precioProductoInput.value = 0;
        precioProductoInput.readOnly = false;
      }

      async function calcularPrecio() {
        const cantidadHojas = parseInt(cantidadHojasInput.value, 10) || 0;
        const conNumerado = conNumeradoCheckbox.checked ? true : false;
        const tipoSeleccionado = document.querySelector('input[name="tipoTalonario.id"]:checked');
        const troqueladoSeleccionado = document.querySelector('input[name="tipoTroqueladoTalonario.id"]:checked');
        const modoSeleccionado = document.querySelector('input[name="modoTalonario.id"]:checked');
        const colorSeleccionado = document.querySelector('input[name="tipoColorTalonario.id"]:checked');
        const medidaSeleccionada = document.querySelector('input[name="medidaTalonario.id"]:checked');
        const papelSeleccionado = document.querySelector('input[name="tipoPapelTalonario.id"]:checked');
        const cantidadSeleccionada = document.querySelector('input[name="cantidadTalonario.id"]:checked');
        let tipoTalonarioId = 0;
        let tipoTroqueladoTalonarioId = 0;
        let modoTalonarioId = 0;
        let tipoColorTalonarioId = 0;
        let medidaTalonarioId = 0;
        let tipoPapelTalonarioId = 0;
        let cantidadTalonarioId = 0;
        let precioProducto = 0;

        if (!cantidadHojas || !tipoSeleccionado || !troqueladoSeleccionado || !modoSeleccionado || !colorSeleccionado || !medidaSeleccionada || !papelSeleccionado || !cantidadSeleccionada) {
            return;
        } else {
            tipoTalonarioId = Number(tipoSeleccionado.value);
            tipoTroqueladoTalonarioId = Number(troqueladoSeleccionado.value);
            modoTalonarioId = Number(modoSeleccionado.value);
            tipoColorTalonarioId = Number(colorSeleccionado.value);
            medidaTalonarioId = Number(medidaSeleccionada.value);
            tipoPapelTalonarioId = Number(papelSeleccionado.value);
            cantidadTalonarioId = Number(cantidadSeleccionada.value);
        }

        try {
            const response = await fetch(`/api/plantilla-talonario/precio?cantidadHojas=${cantidadHojas}&conNumerado=${conNumerado}&tipoTalonarioId=${tipoTalonarioId}&tipoTroqueladoTalonarioId=${tipoTroqueladoTalonarioId}&modoTalonarioId=${modoTalonarioId}&tipoColorTalonarioId=${tipoColorTalonarioId}&medidaTalonarioId=${medidaTalonarioId}&tipoPapelTalonarioId=${tipoPapelTalonarioId}&cantidadTalonarioId=${cantidadTalonarioId}`);
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
      cantidadHojasInput.addEventListener('input', () => {
        resetearPrecio();
        calcularPrecio();
      });
      conNumeradoCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      radiosTipo.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosTroquelado.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosModo.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosColor.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosMedida.forEach(radio => radio.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      }));
      radiosPapel.forEach(radio => radio.addEventListener('change', () => {
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
