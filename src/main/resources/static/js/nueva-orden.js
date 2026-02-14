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
        .catch(error => console.error('Error:', error));
}

function buscarPrecioCatalogo() {
    const inputProductoId = document.getElementById('currentProductoId');
    const inputPrecioProd = document.getElementById('inputPrecioProd');
    const cantInput = document.getElementById('inputCantidad');
    
    if (!inputProductoId || !inputPrecioProd) return;

    const productoId = inputProductoId.value;
    const vCant = cantInput ? (parseInt(cantInput.value) || 1) : 1;
    const inputsDinamicos = document.querySelectorAll('.input-dinamico');
    let detalles = {};

    inputsDinamicos.forEach(input => {
        let valor = (input.type === 'radio' || input.type === 'checkbox') ? (input.checked ? input.value : null) : input.value;
        if (valor) {
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
        if (!response.ok) throw new Error();
        return response.json();
    })
    .then(precioUnitario => {
        // Multiplicamos el precio del catálogo por la cantidad actual
        inputPrecioProd.value = Math.ceil(precioUnitario * vCant);
        inputPrecioProd.readOnly = true;
        recalcular();
    })
    .catch(() => { 
        inputPrecioProd.readOnly = false; 
    });
}

function recalcular() {
    const pProd = document.getElementById('inputPrecioProd');
    const pDis = document.getElementById('inputPrecioDisenio');
    const cantInput = document.getElementById('inputCantidad');
    const pImp = document.getElementById('inputPrecioImpuestos');
    const total = document.getElementById('inputTotal');
    const abonado = document.getElementById('inputAbonado');
    const resta = document.getElementById('inputResta');
    const cDis = document.getElementById('checkDisenio');
    const cFac = document.getElementById('checkFactura');
    const pBaseHidden = document.getElementById('precioDisenioBase');
    const checkCC = document.getElementById('esCC');
    const radiosMedioPago = document.querySelectorAll('input[name="idMedioPago"]');

    if (!pProd || !pDis) return;

    // Ahora vSubtotalProd ya es (Precio * Cantidad)
    const vSubtotalProd = parseFloat(pProd.value) || 0;
    const vBaseFijaDisenio = pBaseHidden ? parseFloat(pBaseHidden.value) : 0;

    // --- 1. LÓGICA DE DISEÑO ---
    if (cDis && cDis.checked) {
        if (vBaseFijaDisenio > 0) {
            pDis.value = Math.ceil(vBaseFijaDisenio);
            pDis.readOnly = true;
        } else {
            pDis.readOnly = false;
        }
    } else {
        pDis.value = 0;
        pDis.readOnly = true;
    }

    // --- 2. CÁLCULO DEL SUBTOTAL ---
    const vDisenioActual = parseFloat(pDis.value) || 0;
    // Ya no multiplicamos por cantidad aquí porque pProd ya lo incluye
    const subtotalBase = vSubtotalProd + vDisenioActual;

    let totalCorriendo = subtotalBase;
    let impuestosAcumulados = 0;

    // --- 3. RECARGO POR FACTURA (21%) ---
    if (cFac && cFac.checked) {
        const iva = subtotalBase * 0.21;
        impuestosAcumulados += iva;
        totalCorriendo += iva;
    }

    // --- 4. RECARGO POR CRÉDITO (10%) ---
    const radioSeleccionado = document.querySelector('input[name="idMedioPago"]:checked');
    if (radioSeleccionado && parseInt(radioSeleccionado.value) === 2) {
        const recargoCredito = totalCorriendo * 0.10;
        impuestosAcumulados += recargoCredito;
        totalCorriendo += recargoCredito;
    }

    // --- 5. TOTALES FINALES ---
    const totalFinal = Math.ceil(totalCorriendo);
    if (pImp) pImp.value = Math.ceil(impuestosAcumulados);
    if (total) total.value = totalFinal;

    const vAbo = parseFloat(abonado.value) || 0;
    if (resta) resta.value = Math.ceil(totalFinal - vAbo);

    // --- 6. VALIDACIONES VISUALES ---
    radiosMedioPago.forEach(r => { r.required = (vAbo > 0); });

    if (checkCC && !checkCC.checked && totalFinal > 0) {
        if (vAbo < (totalFinal / 2)) {
            abonado.classList.add('border-danger', 'text-danger');
        } else {
            abonado.classList.remove('border-danger', 'text-danger');
        }
    } else {
        abonado.classList.remove('border-danger', 'text-danger');
    }
}

function inicializarLogicaCondicional() {
    const contenedor = document.getElementById('contenedor-dinamico');
    if (!contenedor) return;
    contenedor.addEventListener('input', function(e) {
        if (e.target.classList.contains('input-dinamico')) {
            const nombreCampoPadre = e.target.name.replace('detalles[', '').replace(']', '');
            const valorSeleccionado = e.target.value.trim();
            document.querySelectorAll(`[data-depends-on="${nombreCampoPadre}"]`).forEach(bloqueHijo => {
                if (valorSeleccionado === bloqueHijo.getAttribute('data-show-if')) {
                    bloqueHijo.style.display = 'block';
                } else {
                    bloqueHijo.style.display = 'none';
                    const input = bloqueHijo.querySelector('.input-dinamico');
                    if (input) {
                        if (input.type === 'checkbox' || input.type === 'radio') input.checked = false;
                        else input.value = '';
                    }
                }
            });
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    document.addEventListener('input', function (e) {
        const idsRecalcular = ['inputPrecioProd', 'inputPrecioDisenio', 'inputAbonado'];
        if (idsRecalcular.includes(e.target.id)) recalcular();
        
        // Si cambia la cantidad, re-disparamos la búsqueda de catálogo para actualizar el subtotal
        if (e.target.id === 'inputCantidad') {
            buscarPrecioCatalogo();
            recalcular();
        }
    });

    document.addEventListener('change', function (e) {
        if (e.target.classList.contains('input-dinamico')) buscarPrecioCatalogo();

        const id = e.target.id;
        if (['checkDisenio', 'checkFactura', 'checkMuestra', 'esCC'].includes(id) || e.target.name === 'idMedioPago') {
            if (id === 'checkMuestra') {
                const fila = document.getElementById('filaFechaMuestra');
                if (fila) fila.classList.toggle('d-none', !e.target.checked);
            }
            recalcular();
        }
    });

    document.addEventListener('submit', function (e) {
        const checkCC = document.getElementById('esCC');
        const totalInput = document.getElementById('inputTotal');
        const abonadoInput = document.getElementById('inputAbonado');
        
        const total = totalInput ? (parseFloat(totalInput.value) || 0) : 0;
        const abonado = abonadoInput ? (parseFloat(abonadoInput.value) || 0) : 0;

        if (checkCC && !checkCC.checked && total > 0) {
            if (abonado < (total / 2)) {
                e.preventDefault();
                alert("Atención: Para órdenes que no son Cuenta Corriente, se requiere una seña mínima del 50%.");
            }
        }
    });
});