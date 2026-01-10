document.addEventListener('DOMContentLoaded', () => {
      // Valores predefinidos
      const precioDisenio = 8000;
      const recargoFactura = 0.21; // 21% sobre subtotal
      const recargoCredito = 0.10; // 10% sobre subtotal+impuesto

      // Inputs y checkboxes
      const adicionalCheckbox = document.getElementById('lona-publicitaria-con-adicional-disenio');
      const necesitaFacturaCheckbox = document.getElementById('necesitaFactura');
      const precioProductoInput = document.getElementById('precioProducto');
      const precioDisenioInput = document.getElementById('precioDisenio');
      const precioImpuestosInput = document.getElementById('precioImpuestos');
      const totalInput = document.getElementById('total');
      const abonadoInput = document.getElementById('abonado');
      const restaInput = document.getElementById('resta');
      const radiosMedioPago = document.querySelectorAll('input[name="medioPago.id"]');
      const bolsillosCheckbox = document.getElementById('lona-publicitaria-con-bolsillos');
      const portabannerCheckbox = document.getElementById('lona-publicitaria-con-adicional-portabanner');
      const ojalesCheckbox = document.getElementById('lona-publicitaria-con-ojales');
      const ojalesReforzadosCheckbox = document.getElementById('lona-publicitaria-con-ojales-con-refuerzo');
      const demasiaParaTensadoCheckbox = document.getElementById('lona-publicitaria-con-demasia-para-tensado');
      const solapadoCheckbox = document.getElementById('lona-publicitaria-con-solapado');
      const radiosMedida = document.querySelectorAll('input[name="medidaLonaPublicitaria.id"]');
      const radiosTipo = document.querySelectorAll('input[name="tipoLonaPublicitaria.id"]');
      const cantidadLonasPublicitariasInput = document.getElementById('cantidad');
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
        const conAdicionalPortabanner = portabannerCheckbox.checked ? true : false;
        const conOjales = ojalesCheckbox.checked ? true : false;
        const conOjalesConRefuerzo = ojalesReforzadosCheckbox.checked ? true : false;
        const conBolsillos = bolsillosCheckbox.checked ? true : false;
        const conDemasiaParaTensado = demasiaParaTensadoCheckbox.checked ? true : false;
        const conSolapado = solapadoCheckbox.checked ? true : false;
        const medidaSeleccionada = document.querySelector('input[name="medidaLonaPublicitaria.id"]:checked');
        const lonaSeleccionada = document.querySelector('input[name="tipoLonaPublicitaria.id"]:checked');
        let medidaLonaPublicitariaId = 0;
        let tipoLonaPublicitariaId = 0;
        let precioProducto = 0;

        if (!medidaSeleccionada || !lonaSeleccionada) {
            return;
        } else {
            medidaLonaPublicitariaId = Number(medidaSeleccionada.value);
            tipoLonaPublicitariaId = Number(lonaSeleccionada.value);
        }

        try {
            const response = await fetch(`/api/plantilla-lona-publicitaria/precio?conAdicionalPortabanner=${conAdicionalPortabanner}&conOjales=${conOjales}&conOjalesConRefuerzo=${conOjalesConRefuerzo}&conBolsillos=${conBolsillos}&conDemasiaParaTensado=${conDemasiaParaTensado}&conSolapado=${conSolapado}&medidaLonaPublicitariaId=${medidaLonaPublicitariaId}&tipoLonaPublicitariaId=${tipoLonaPublicitariaId}`);
            if (response.status === 200) {
                let precioUnitario = await response.json();
                const cantidad = parseInt(cantidadLonasPublicitariasInput.value, 10) || 0;
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
      cantidadLonasPublicitariasInput.addEventListener('input', calcularPrecio);
      adicionalCheckbox.addEventListener('change', calcularPrecio);
      portabannerCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      ojalesCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      ojalesReforzadosCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      bolsillosCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      demasiaParaTensadoCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      solapadoCheckbox.addEventListener('change', () => {
        resetearPrecio();
        calcularPrecio();
      });
      necesitaFacturaCheckbox.addEventListener('change', calcularPrecio);
      abonadoInput.addEventListener('input', revisarSiAbonadoEstaBien);
      totalInput.addEventListener('input', revisarSiAbonadoEstaBien);
      radiosMedioPago.forEach(radio => radio.addEventListener('change', calcularPrecio));
      radiosMedida.forEach(radio => radio.addEventListener('change', () => {
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
