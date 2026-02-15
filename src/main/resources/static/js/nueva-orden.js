/**
 * Carga el fragmento del formulario cuando se selecciona un producto (solo en CREACIÓN)
 */
function seleccionarProducto(id) {
    const contenedor = document.getElementById('contenedor-formulario-dinamico');

    fetch(`/ordenes/formulario-producto/${id}`)
        .then(response => response.text())
        .then(html => {
            contenedor.innerHTML = html;
            document.getElementById('selector-productos').classList.add('d-none');
            contenedor.classList.remove('d-none');
            inicializarLogicaCondicional();
            manejarLogicaCantidadDinamica(); // Oculta o muestra antes de recalcular
            recalcular();
        })
        .catch(error => console.error('Error al cargar el formulario:', error));
}

/*
 * Consulta al API de catálogo con lógica de packs vs unitario
 */
function buscarPrecioCatalogo() {
    const inputProductoId = document.getElementById('currentProductoId');
    const inputSubtotal = document.getElementById('inputPrecioProd');
    const cantInput = document.getElementById('inputCantidad');
    const radioCantProd = document.querySelector('input[name="detalles[cantidad_producto]"]:checked');

    if (!inputProductoId || !inputSubtotal) return;

    const productoId = inputProductoId.value;
    const vCant = cantInput ? (parseInt(cantInput.value) || 1) : 1;
    const inputsDinamicos = document.querySelectorAll('.input-dinamico');
    let detalles = {};

    inputsDinamicos.forEach(input => {
        let valor = null;
        if (input.type === 'radio' || input.type === 'checkbox') {
            if (input.checked) valor = input.value;
        } else {
            valor = input.value;
        }

        if (valor && valor !== "") {
            const key = input.name.replace('detalles[', '').replace(']', '');
            detalles[key] = valor;
        }
    });

    if (Object.keys(detalles).length === 0) return;

    fetch('/api/catalogo/buscar-precio', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productoId: parseInt(productoId), detalles: detalles })
    })
    .then(response => {
        if (!response.ok) throw new Error("No hay coincidencia");
        return response.json();
    })
    .then(precioRecibido => {
        // LÓGICA DE MULTIPLICACIÓN
        let precioFinal;

        // Si hay un radio seleccionado y NO es "OTRA" y NO tiene guion, es un PACK.
        // En ese caso, el precio recibido ya es el total.
        if (radioCantProd && radioCantProd.value !== "OTRA" && !radioCantProd.value.includes("-")) {
            precioFinal = precioRecibido;
        } else {
            // Si es un rango, es "OTRA" o no existe el selector, multiplicamos por cantidad
            precioFinal = precioRecibido * vCant;
        }

        inputSubtotal.value = Math.ceil(precioFinal);
        inputSubtotal.readOnly = true;
        recalcular();
    })
    .catch(() => {
        inputSubtotal.readOnly = false;
    });
}

function manejarLogicaCantidadDinamica() {
    const hayCampoCantidadDinamica = document.querySelector('input[name="detalles[cantidad_producto]"]');
    const radioCantProd = document.querySelector('input[name="detalles[cantidad_producto]"]:checked');
    const contenedorManual = document.getElementById('contenedor-cantidad-manual');
    const inputManual = document.getElementById('inputCantidad');

    if (!contenedorManual || !inputManual) return;

    if (hayCampoCantidadDinamica) {
        // Lógica: Si hay algo marcado Y es ("OTRA" o tiene "-"), lo mostramos.
        // En cualquier otro caso (nada marcado o valor fijo), se oculta.
        if (radioCantProd && (radioCantProd.value === "OTRA" || radioCantProd.value.includes("-"))) {
            contenedorManual.classList.remove('d-none');
            inputManual.required = true;
        } else {
            contenedorManual.classList.add('d-none');
            inputManual.required = false;

            // Si es un valor fijo (ej: 50), sincronizamos para el cálculo
            if (radioCantProd) {
                inputManual.value = parseInt(radioCantProd.value) || 1;
            }
        }
    } else {
        // Si el producto no tiene el campo dinámico, siempre visible
        contenedorManual.classList.remove('d-none');
        inputManual.required = true;
    }
}

/**
 * Realiza todos los cálculos de la orden
 */
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
    const radiosMedioPago = document.querySelectorAll('input[name="idMedioPago"]');

    if (!pSubtotal || !pDis) return;

    if (!pSubtotal.value || parseFloat(pSubtotal.value) === 0) {
        pSubtotal.readOnly = false;
    }

    const vSubtotalProd = parseFloat(pSubtotal.value) || 0;
    const vBaseFijaDisenio = pBaseHidden ? parseFloat(pBaseHidden.value) : 0;

    if (cDis && cDis.checked) {
        const vDisenioActual = parseFloat(pDis.value) || 0;
        if (vDisenioActual === 0 && vBaseFijaDisenio > 0) {
            pDis.value = Math.ceil(vBaseFijaDisenio);
            pDis.readOnly = true;
        } else {
            pDis.readOnly = false;
        }
    } else {
        pDis.value = 0;
        pDis.readOnly = true;
    }

    const vDisenioFinal = parseFloat(pDis.value) || 0;
    const subtotalBase = vSubtotalProd + vDisenioFinal;

    let totalCorriendo = subtotalBase;
    let impuestosAcumulados = 0;

    if (cFac && cFac.checked) {
        const iva = subtotalBase * 0.21;
        impuestosAcumulados += iva;
        totalCorriendo += iva;
    }

    const radioSeleccionado = document.querySelector('input[name="idMedioPago"]:checked');
    if (radioSeleccionado && parseInt(radioSeleccionado.value) === 2) {
        const recargoCredito = totalCorriendo * 0.10;
        impuestosAcumulados += recargoCredito;
        totalCorriendo += recargoCredito;
    }

    const totalFinal = Math.ceil(totalCorriendo);
    if (pImp) pImp.value = Math.ceil(impuestosAcumulados);
    if (total) total.value = totalFinal;

    const vAbo = parseFloat(abonado.value) || 0;
    if (resta) resta.value = Math.ceil(totalFinal - vAbo);

    radiosMedioPago.forEach(r => { r.required = (vAbo > 0); });

    if (checkCC && !checkCC.checked && totalFinal > 0) {
        if (vAbo < (totalFinal / 2)) {
            abonado.classList.add('border-danger', 'text-danger');
        } else {
            abonado.classList.remove('border-danger', 'text-danger');
        }
    }
}

/**
 * Maneja visibilidad de campos dependientes
 */
function inicializarLogicaCondicional() {
    const contenedor = document.getElementById('contenedor-dinamico');
    if (!contenedor) return;

    const actualizar = (target) => {
        const nombrePadre = target.name.replace('detalles[', '').replace(']', '');
        const valorActual = target.value.trim();

        document.querySelectorAll(`[data-depends-on="${nombrePadre}"]`).forEach(bloque => {
            const valorReq = bloque.getAttribute('data-show-if');
            if (target.type === 'radio' && !target.checked) return;

            if (valorActual === valorReq) {
                bloque.style.display = 'block';
            } else {
                bloque.style.display = 'none';
            }
        });
    };

    contenedor.addEventListener('input', (e) => {
        if (e.target.classList.contains('input-dinamico')) actualizar(e.target);
    });

    document.querySelectorAll('.input-dinamico').forEach(input => {
        if (input.type === 'radio') {
            if (input.checked) actualizar(input);
        } else if (input.value !== "") {
            actualizar(input);
        }
    });
}

/**
 * Listeners globales
 */
document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById('inputPrecioProd')) {
        inicializarLogicaCondicional();
        manejarLogicaCantidadDinamica();
        recalcular();
    }

    document.addEventListener('input', function (e) {
        const ids = ['inputPrecioProd', 'inputPrecioDisenio', 'inputAbonado', 'inputCantidad'];
        if (ids.includes(e.target.id)) {
            if (e.target.id === 'inputCantidad') buscarPrecioCatalogo();
            recalcular();
        }
    });

    document.addEventListener('change', function (e) {
        if (e.target.classList.contains('input-dinamico')) {
            if (e.target.name === 'detalles[cantidad_producto]') {
                manejarLogicaCantidadDinamica();
            }
            buscarPrecioCatalogo();
        }

        if (e.target.id === 'checkMuestra') {
            const fila = document.getElementById('filaFechaMuestra');
            if (fila) e.target.checked ? fila.classList.remove('d-none') : fila.classList.add('d-none');
        }

        if (['checkDisenio', 'checkFactura', 'esCC'].includes(e.target.id) || e.target.name === 'idMedioPago') {
            recalcular();
        }
    });

    document.addEventListener('submit', function (e) {
        const inputTotal = document.getElementById('inputTotal');
        const inputAbonado = document.getElementById('inputAbonado');
        const inputSubtotal = document.getElementById('inputPrecioProd');
        const inputDisenio = document.getElementById('inputPrecioDisenio');
        const esCC = document.getElementById('esCC')?.checked;

        const campos = [inputTotal, inputAbonado, inputSubtotal, inputDisenio];
        campos.forEach(input => {
            if (input && (input.value === "" || input.value === null)) {
                input.value = 0;
            }
        });

        const totalVal = parseFloat(inputTotal?.value) || 0;
        const abonadoVal = parseFloat(inputAbonado?.value) || 0;

        if (!esCC && totalVal > 0 && abonadoVal < (totalVal / 2)) {
            e.preventDefault();
            alert("Se requiere una seña mínima del 50% para continuar.");
        }
    });
});