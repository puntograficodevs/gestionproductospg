let debounceTimer;

/**
 * Función para evitar múltiples peticiones al escribir rápido
 */
function debounceBuscarPrecio() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => {
        buscarPrecioCatalogo();
    }, 300);
}

function seleccionarProducto(id) {
    const contenedor = document.getElementById('contenedor-formulario-dinamico');
    fetch(`/ordenes/formulario-producto/${id}`)
        .then(response => response.text())
        .then(html => {
            contenedor.innerHTML = html;
            document.getElementById('selector-productos').classList.add('d-none');
            contenedor.classList.remove('d-none');
            inicializarLogicaCondicional();
            manejarLogicaCantidadDinamica();
            recalcular();
        })
        .catch(error => console.error('Error al cargar el formulario:', error));
}

function calcularLogicaAnillado(paginas, faz) {
    let hojas = (faz === "DOBLE FAZ") ? Math.ceil(paginas / 2) : paginas;
    if (hojas === 0) return 0;

    const cantidadAnillados = Math.ceil(hojas / 400);
    const hojasPorAnillado = Math.ceil(hojas / cantidadAnillados);

    const escalas = [
        { hasta: 20, precio: 1000 }, { hasta: 40, precio: 1200 },
        { hasta: 60, precio: 1300 }, { hasta: 100, precio: 1500 },
        { hasta: 150, precio: 1700 }, { hasta: 200, precio: 2500 },
        { hasta: 300, precio: 2800 }, { hasta: 400, precio: 3300 }
    ];

    const escala = escalas.find(p => hojasPorAnillado <= p.hasta) || escalas[escalas.length - 1];
    return escala.precio * cantidadAnillados;
}

function buscarPrecioCatalogo() {
    const h2Element = document.querySelector('h2');
    const inputProductoId = document.getElementById('currentProductoId');
    const inputSubtotal = document.getElementById('inputPrecioProd');
    const inputCantidadReal = document.getElementById('inputCantidadItem');

    if (!inputProductoId || !inputSubtotal || !inputProductoId.value) return;

    const nombreProducto = h2Element ? h2Element.innerText.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase() : "";
    const esImpresion = nombreProducto.includes("impresion");

    const inputsDinamicos = document.querySelectorAll('.input-dinamico');
    let detalles = {};

    inputsDinamicos.forEach(input => {
        let valor = null;
        if (input.type === 'checkbox') {
            valor = input.checked ? "on" : null;
        } else if (input.type === 'radio') {
            if (input.checked) valor = input.value;
        } else {
            valor = input.value;
        }

        if (valor !== null && valor !== "") {
            const key = input.name.replace('detalles[', '').replace(']', '');
            detalles[key] = valor;
        }
    });

    if (esImpresion && detalles["cantidad_producto"]) {
        const paginas = parseInt(detalles["cantidad_producto"]) || 0;
        const limite = (detalles["tipo_faz"] === "DOBLE FAZ") ? 100 : 50;
        detalles["rango_impresion"] = (paginas <= limite) ? "1-LIMITE" : "LIMITE-MAS";
    }

    fetch('/api/catalogo/buscar-precio', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productoId: parseInt(inputProductoId.value), detalles: detalles })
    })
    .then(response => response.json())
    .then(precioRecibido => {
        if (precioRecibido === 0) {
            inputSubtotal.readOnly = false;
            return;
        }

        let precioFinal;
        const vCantFinal = parseInt(inputCantidadReal.value) || 1;

        if (esImpresion) {
            const paginas = parseInt(detalles["cantidad_producto"]) || 1;
            const costoHojas = precioRecibido * paginas;
            let costoAnillado = (detalles["es_anillado"] === "on") ? calcularLogicaAnillado(paginas, detalles["tipo_faz"]) : 0;
            precioFinal = costoHojas + costoAnillado;
        } else {
            const radioCantProd = document.querySelector('input[name="detalles[cantidad_producto]"]:checked');
            // Si es manual puro (Agenda) o se eligió OTRA/Rango, multiplicamos precio unitario por cantidad real
            precioFinal = (!radioCantProd || radioCantProd.value === "OTRA" || radioCantProd.value.includes("-"))
                ? precioRecibido * vCantFinal : precioRecibido;
        }

        inputSubtotal.value = Math.ceil(precioFinal);
        inputSubtotal.readOnly = true;
        recalcular();
    })
    .catch(() => { inputSubtotal.readOnly = false; });
}

function manejarLogicaCantidadDinamica() {
    const inputsRadioCant = document.querySelectorAll('input[name="detalles[cantidad_producto]"]');
    const inputNumberCant = document.querySelector('input[name="detalles[cantidad_producto]"][type="number"]');
    const contenedorManual = document.getElementById('contenedor-cantidad-item');
    const inputManual = document.getElementById('inputCantidadItem');

    if (!contenedorManual || !inputManual) return;

    // CASO A: No existe el campo dinámico (Agendas)
    if (inputsRadioCant.length === 0 && !inputNumberCant) {
        contenedorManual.style.display = 'block';
        return;
    }

    // CASO B: Es un input number (Impresiones)
    if (inputNumberCant) {
        contenedorManual.style.display = 'none';
        inputManual.value = inputNumberCant.value || 1;
        return;
    }

    // CASO C: Son Radios (Merchandising)
    const seleccionado = document.querySelector('input[name="detalles[cantidad_producto]"]:checked');
    if (seleccionado) {
        if (seleccionado.value === "OTRA" || seleccionado.value.includes("-")) {
            contenedorManual.style.display = 'block';
        } else {
            contenedorManual.style.display = 'none';
            inputManual.value = seleccionado.value; // Sincronizamos el valor fijo al input real
        }
    } else {
        contenedorManual.style.display = 'none';
    }
}

function recalcular() {
    const pSubtotal = document.getElementById('inputPrecioProd');
    const pDis = document.getElementById('inputPrecioDisenio');
    const pImp = document.getElementById('inputPrecioImpuestos');
    const total = document.getElementById('inputTotal');
    const abonado = document.getElementById('inputAbonado');
    const resta = document.getElementById('inputResta');
    const cDis = document.getElementById('checkDisenio');
    const cFac = document.getElementById('checkFactura');
    const pBaseHidden = document.getElementById('precioDisenioBase');
    const checkCC = document.getElementById('esCC');

    if (!pSubtotal) return;

    const vSubtotalProd = parseFloat(pSubtotal.value) || 0;
    const vBaseFijaDisenio = pBaseHidden ? parseFloat(pBaseHidden.value) : 0;

    if (cDis && cDis.checked) {
        if ((parseFloat(pDis.value) || 0) === 0 && vBaseFijaDisenio > 0) {
            pDis.value = Math.ceil(vBaseFijaDisenio);
            pDis.readOnly = true;
        } else {
            pDis.readOnly = false;
        }
    } else if (pDis) {
        pDis.value = 0;
        pDis.readOnly = true;
    }

    const subtotalBase = vSubtotalProd + (parseFloat(pDis?.value) || 0);
    let totalCorriendo = subtotalBase;
    let impuestosAcumulados = 0;

    if (cFac && cFac.checked) {
        const iva = subtotalBase * 0.21;
        impuestosAcumulados += iva;
        totalCorriendo += iva;
    }

    const radioPago = document.querySelector('input[name="idMedioPago"]:checked');
    if (radioPago && parseInt(radioPago.value) === 2) {
        const recargo = totalCorriendo * 0.10;
        impuestosAcumulados += recargo;
        totalCorriendo += recargo;
    }

    const totalFinal = Math.ceil(totalCorriendo);
    if (pImp) pImp.value = Math.ceil(impuestosAcumulados);
    if (total) total.value = totalFinal;
    const vAbo = parseFloat(abonado?.value) || 0;
    if (resta) resta.value = Math.ceil(totalFinal - vAbo);

    // Validación visual de seña
    if (checkCC && !checkCC.checked && totalFinal > 0 && abonado) {
        vAbo < (totalFinal / 2) ? abonado.classList.add('border-danger', 'text-danger') : abonado.classList.remove('border-danger', 'text-danger');
    }
}

function inicializarLogicaCondicional() {
    const contenedor = document.getElementById('contenedor-dinamico');
    if (!contenedor) return;

    contenedor.addEventListener('input', (e) => {
        if (e.target.classList.contains('input-dinamico')) {
            const nombrePadre = e.target.name.replace('detalles[', '').replace(']', '');
            let valorActual = e.target.type === 'checkbox' ? (e.target.checked ? "on" : "off") : e.target.value;

            document.querySelectorAll(`[data-depends-on="${nombrePadre}"]`).forEach(bloque => {
                const valorReq = bloque.getAttribute('data-show-if');
                bloque.style.display = (String(valorActual) === String(valorReq)) ? 'block' : 'none';
            });

            if (e.target.name === 'detalles[cantidad_producto]') manejarLogicaCantidadDinamica();
            debounceBuscarPrecio();
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    inicializarLogicaCondicional();
    manejarLogicaCantidadDinamica();
    recalcular();

    document.addEventListener('input', function (e) {
        const ids = ['inputPrecioProd', 'inputPrecioDisenio', 'inputAbonado', 'inputCantidadItem'];
        if (ids.includes(e.target.id)) {
            if (e.target.id === 'inputCantidadItem') debounceBuscarPrecio();
            recalcular();
        }
    });

    document.addEventListener('change', function (e) {
        if (e.target.id === 'checkMuestra') {
            const fila = document.getElementById('filaFechaMuestra');
            if (fila) e.target.checked ? fila.classList.remove('d-none') : fila.classList.add('d-none');
        }
        if (['checkDisenio', 'checkFactura', 'esCC'].includes(e.target.id) || e.target.name === 'idMedioPago') {
            recalcular();
        }
    });
});