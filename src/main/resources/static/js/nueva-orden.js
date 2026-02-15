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
            recalcular();
        })
        .catch(error => console.error('Error al cargar el formulario:', error));
}

/**
 * Consulta al API de catálogo si los detalles elegidos tienen un precio definido.
 */
function buscarPrecioCatalogo() {
    const inputProductoId = document.getElementById('currentProductoId');
    const inputSubtotal = document.getElementById('inputPrecioProd');
    const cantInput = document.getElementById('inputCantidad');

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
    .then(precioUnitario => {
        inputSubtotal.value = Math.ceil(precioUnitario * vCant);
        inputSubtotal.readOnly = true;
        recalcular();
    })
    .catch(() => {
        inputSubtotal.readOnly = false;
    });
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

    // 1. Manejo del Subtotal Producto
    if (!pSubtotal.value || parseFloat(pSubtotal.value) === 0) {
        pSubtotal.readOnly = false;
    }

    const vSubtotalProd = parseFloat(pSubtotal.value) || 0;
    const vBaseFijaDisenio = pBaseHidden ? parseFloat(pBaseHidden.value) : 0;

    // 2. Lógica de Diseño (MEJORADA PARA EDICIÓN)
    if (cDis && cDis.checked) {
        const vDisenioActual = parseFloat(pDis.value) || 0;

        // Si el campo está en 0 o vacío (es nueva o se activó recién) y hay base fija
        if (vDisenioActual === 0 && vBaseFijaDisenio > 0) {
            pDis.value = Math.ceil(vBaseFijaDisenio);
            pDis.readOnly = true; // Bloqueamos porque es el sugerido
        } else {
            // Si ya tenía valor (edición) o no hay base fija, permitimos editar
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

    // 3. IVA (21%)
    if (cFac && cFac.checked) {
        const iva = subtotalBase * 0.21;
        impuestosAcumulados += iva;
        totalCorriendo += iva;
    }

    // 4. Recargo Tarjeta (10%)
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

    // Alerta visual de seña
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

    // Carga inicial de visibilidad (para edición)
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
        if (e.target.classList.contains('input-dinamico')) buscarPrecioCatalogo();

        if (e.target.id === 'checkMuestra') {
            const fila = document.getElementById('filaFechaMuestra');
            if (fila) e.target.checked ? fila.classList.remove('d-none') : fila.classList.add('d-none');
        }

        if (['checkDisenio', 'checkFactura', 'esCC'].includes(e.target.id) || e.target.name === 'idMedioPago') {
            recalcular();
        }
    });

    document.addEventListener('submit', function (e) {
        const total = parseFloat(document.getElementById('inputTotal')?.value) || 0;
        const abonado = parseFloat(document.getElementById('inputAbonado')?.value) || 0;
        const esCC = document.getElementById('esCC')?.checked;

        if (!esCC && total > 0 && abonado < (total / 2)) {
            e.preventDefault();
            alert("Se requiere una seña mínima del 50% para continuar.");
        }
    });
});