let debounceTimer;
let itemIndexElement = document.querySelectorAll('.item-producto');
let indiceActual = itemIndexElement.length > 0 ? (itemIndexElement.length - 1) : 0;

// --- 1. INICIALIZACIÓN Y CARGA ---

function seleccionarProducto(id) {
    const contenedor = document.getElementById('contenedor-formulario-dinamico');

    fetch(`/ordenes/formulario-producto/${id}?index=0`)
        .then(response => response.text())
        .then(html => {
            contenedor.innerHTML = html;
            document.getElementById('selector-productos').classList.add('d-none');
            contenedor.classList.remove('d-none');

            inicializarLogicaItem(0);
            // IMPORTANTE: Buscamos precio inicial apenas carga
            buscarPrecioCatalogo(0);
            recalcular();
        })
        .catch(error => console.error('Error:', error));
}

function inicializarLogicaItem(index) {
    const item = document.querySelector(`.item-producto[data-index="${index}"]`);
    if (!item) return;

    // 1. Mostrar campos dependientes
    item.querySelectorAll('.bloque-campo[data-depends-on]').forEach(bloque => {
        const dependeDe = bloque.getAttribute('data-depends-on');
        const valorRequerido = bloque.getAttribute('data-show-if');
        const inputOrigen = item.querySelector(`[name*="detalles[${dependeDe}]"]:checked`) ||
                            item.querySelector(`[name*="detalles[${dependeDe}]"]`);

        if (inputOrigen) {
            const valorActual = inputOrigen.type === 'checkbox' ? (inputOrigen.checked ? "on" : "off") : inputOrigen.value;
            if (String(valorActual) === String(valorRequerido)) bloque.style.display = 'block';
        }
    });

    manejarLogicaCantidadDinamica(index);

    // 2. Listeners de Diseño
    const inputDisenio = item.querySelector('.input-disenio-item');
    if (inputDisenio) {
        inputDisenio.addEventListener('focus', function() { if (this.value == "0") this.value = ""; });
        inputDisenio.addEventListener('blur', function() {
            if (this.value == "" || isNaN(this.value)) this.value = 0;
            recalcular();
        });
    }

    // 3. Listener principal de inputs
    item.addEventListener('input', (e) => {
        const target = e.target;

        // Si cambia un campo de "detalles" (ej: Modelo de Sello)
        if (target.classList.contains('input-dinamico')) {
            const match = target.name.match(/detalles\[(.*?)\]/);
            if (match) {
                const nombreCampo = match[1];
                let valorActual = target.type === 'checkbox' ? (target.checked ? "on" : "off") : target.value;

                item.querySelectorAll(`[data-depends-on="${nombreCampo}"]`).forEach(bloque => {
                    const valorReq = bloque.getAttribute('data-show-if');
                    bloque.style.display = (String(valorActual) === String(valorReq)) ? 'block' : 'none';
                });

                if (nombreCampo === 'cantidad_producto') manejarLogicaCantidadDinamica(index);
                debounceBuscarPrecio(index);
            }
        }

        // Si cambia el precio manual o la CANTIDAD manual (para multiplicar)
        if (target.classList.contains('input-precio-item') ||
            target.classList.contains('input-cantidad-real') ||
            target.classList.contains('input-disenio-item') ||
            target.classList.contains('check-disenio-item')) {

            if (target.classList.contains('input-cantidad-real')) {
                debounceBuscarPrecio(index); // Re-calcula con la nueva cantidad
            }
            recalcular();
        }
    });
}

// --- 2. LÓGICA DE AGREGAR, CONFIRMAR Y ELIMINAR ---

function confirmarItem(index) {
    const item = document.querySelector(`.item-producto[data-index="${index}"]`);
    const inputPrecio = item.querySelector('.input-precio-item');

    if ((parseFloat(inputPrecio.value) || 0) <= 0) {
        alert("Cargá un precio válido antes de confirmar.");
        return;
    }

    const collapseEl = item.querySelector('.collapse');
    if (collapseEl) bootstrap.Collapse.getOrCreateInstance(collapseEl).hide();

    document.getElementById('btn-agregar-otro').classList.remove('d-none');

    const pProd = parseFloat(inputPrecio.value) || 0;
    const pDis = parseFloat(item.querySelector('.input-disenio-item')?.value) || 0;
    const sumaItem = Math.ceil(pProd + pDis);

    const tit = item.querySelector('.titulo-item');
    if (!tit.dataset.baseText) tit.dataset.baseText = tit.innerText.split(' - $')[0];
    tit.innerText = `${tit.dataset.baseText} - $${sumaItem}`;

    recalcular();
}

function duplicarItemActual() {
    indiceActual++;
    const lista = document.getElementById('lista-items-productos');
    const idProducto = document.getElementById('currentProductoId').value;

    fetch(`/ordenes/formulario-producto/${idProducto}?index=${indiceActual}`)
        .then(response => response.text())
        .then(html => {
            lista.insertAdjacentHTML('beforeend', html);
            const nuevoItem = lista.querySelector(`.item-producto[data-index="${indiceActual}"]`);
            const collapseDiv = nuevoItem.querySelector('.collapse');
            if (collapseDiv) {
                const bsCollapse = new bootstrap.Collapse(collapseDiv, { toggle: false });
                bsCollapse.show();
            }
            document.getElementById('btn-agregar-otro').classList.add('d-none');
            inicializarLogicaItem(indiceActual);
            buscarPrecioCatalogo(indiceActual); // Buscamos precio para el nuevo ítem
            setTimeout(() => { nuevoItem.scrollIntoView({ behavior: 'smooth', block: 'start' }); }, 300);
        })
        .catch(error => console.error('Error:', error));
}

function eliminarItem(btn) {
    const item = btn.closest('.item-producto');
    if (document.querySelectorAll('.item-producto').length > 1) {
        if (confirm("¿Eliminar este producto de la orden?")) {
            item.remove();
            recalcular();
            document.getElementById('btn-agregar-otro').classList.remove('d-none');
        }
    } else {
        alert("La orden debe tener al menos un producto.");
    }
}

// --- 3. CÁLCULOS Y CATÁLOGO ---

function buscarPrecioCatalogo(index) {
    const item = document.querySelector(`.item-producto[data-index="${index}"]`);
    const inputProductoId = document.getElementById('currentProductoId');
    const inputPrecioItem = item.querySelector('.input-precio-item');
    const inputCantReal = item.querySelector('.input-cantidad-real');
    const h2Element = document.querySelector('h2');

    if (!inputProductoId?.value || !inputPrecioItem) return;

    const nombreProducto = h2Element ? h2Element.innerText.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase() : "";
    const esImpresion = nombreProducto.includes("impresion");

    let detalles = {};

    // Capturamos los campos dinámicos
    item.querySelectorAll('.input-dinamico').forEach(input => {
        const match = input.name.match(/detalles\[(.*?)\]/);
        if (!match) return;

        const nombreCampo = match[1];
        let val = null;

        if (input.type === 'checkbox') {
            // Mandamos true o false SIEMPRE para que el catálogo no falle
            val = input.checked;
        } else if (input.type === 'radio') {
            if (input.checked) val = input.value;
        } else {
            val = input.value;
        }

        // Solo agregamos al JSON si el valor no es nulo
        if (val !== null && val !== "") {
            detalles[nombreCampo] = val;
        }
    });

    // Lógica especial para impresiones
    if (esImpresion && detalles["cantidad_producto"]) {
        const paginas = parseInt(detalles["cantidad_producto"]) || 0;
        const limite = (detalles["tipo_faz"] === "DOBLE FAZ") ? 100 : 50;
        detalles["rango_impresion"] = (paginas <= limite) ? "1-LIMITE" : "LIMITE-MAS";
    }

    console.log("Enviando al catálogo:", detalles); // Mirá esto en F12

    fetch('/api/catalogo/buscar-precio', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productoId: parseInt(inputProductoId.value), detalles: detalles })
    })
    .then(r => r.json())
    .then(precioRecibido => {
        // Si no hay precio (0), permitimos edición manual
        if (precioRecibido === 0) {
            inputPrecioItem.readOnly = false;
            return;
        }

        let precioFinal;
        const vCantFinal = parseInt(inputCantReal.value) || 1;

        if (esImpresion) {
            const paginas = parseInt(detalles["cantidad_producto"]) || 1;
            const costoHojas = precioRecibido * paginas;
            let costoAnillado = (detalles["es_anillado"] === true) ? calcularLogicaAnillado(paginas, detalles["tipo_faz"]) : 0;
            precioFinal = (costoHojas + costoAnillado) * vCantFinal;
        } else {
            // Para sellos de madera, automáticos, etc.
            const tienePackDefinido = detalles["cantidad_producto"] && detalles["cantidad_producto"] !== "OTRA";

            if (tienePackDefinido) {
                // Es un pack (ej: 100 tarjetas), el precio es el del pack total.
                precioFinal = precioRecibido;
            } else {
                // Es precio base (ej: 1 sello), multiplicamos por la cantidad manual.
                precioFinal = precioRecibido * vCantFinal;
            }
        }

        inputPrecioItem.value = Math.ceil(precioFinal);
        inputPrecioItem.readOnly = true;
        recalcular();
    })
    .catch(err => {
        console.error("Error en catálogo:", err);
        inputPrecioItem.readOnly = false;
    });
}

function recalcular() {
    const pSubProdGral = document.getElementById('inputPrecioProd');
    const pSubDisGral = document.getElementById('inputPrecioDisenio');
    const pImp = document.getElementById('inputPrecioImpuestos');
    const total = document.getElementById('inputTotal');
    const abonado = document.getElementById('inputAbonado');
    const resta = document.getElementById('inputResta');
    const cFac = document.getElementById('checkFactura');
    const checkCC = document.getElementById('esCC');
    const vBaseFijaDisenio = parseFloat(document.getElementById('precioDisenioBase')?.value) || 0;

    let sumaProductos = 0;
    let sumaDisenios = 0;

    document.querySelectorAll('.item-producto').forEach(item => {
        const pProd = parseFloat(item.querySelector('.input-precio-item').value) || 0;
        const checkD = item.querySelector('.check-disenio-item');
        const inputD = item.querySelector('.input-disenio-item');

        if (checkD && checkD.checked) {
            inputD.readOnly = false;
            if (document.activeElement !== inputD && (parseFloat(inputD.value) === 0 || !inputD.value)) {
                inputD.value = Math.ceil(vBaseFijaDisenio);
            }
        } else if (inputD) {
            inputD.value = 0;
            inputD.readOnly = true;
        }

        sumaProductos += pProd;
        sumaDisenios += parseFloat(inputD?.value) || 0;
    });

    if (pSubProdGral) pSubProdGral.value = Math.ceil(sumaProductos);
    if (pSubDisGral) pSubDisGral.value = Math.ceil(sumaDisenios);

    const subtotalBase = sumaProductos + sumaDisenios;
    let totalCorriendo = subtotalBase;
    let impuestos = 0;

    if (cFac?.checked) { impuestos += (subtotalBase * 0.21); totalCorriendo += (subtotalBase * 0.21); }

    const radioPago = document.querySelector('input[name="idMedioPago"]:checked');
    if (radioPago && parseInt(radioPago.value) === 2) {
        const recargo = totalCorriendo * 0.10;
        impuestos += recargo;
        totalCorriendo += recargo;
    }

    const tFinal = Math.ceil(totalCorriendo);
    const vAbo = parseFloat(abonado?.value) || 0;

    if (pImp) pImp.value = Math.ceil(impuestos);
    if (total) total.value = tFinal;
    if (resta) resta.value = tFinal - vAbo;

    // Validación visual de seña
    if (abonado && checkCC && !checkCC.checked && tFinal > 0) {
        vAbo < (tFinal / 2) ? abonado.classList.add('border-danger', 'text-danger') : abonado.classList.remove('border-danger', 'text-danger');
    }
}

// --- 4. FUNCIONES AUXILIARES ---

function debounceBuscarPrecio(index) {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => buscarPrecioCatalogo(index), 300);
}

function calcularLogicaAnillado(paginas, faz) {
    let hojas = (faz === "DOBLE FAZ") ? Math.ceil(paginas / 2) : paginas;
    if (hojas === 0) return 0;
    const escalas = [
        { hasta: 20, precio: 1000 }, { hasta: 40, precio: 1200 }, { hasta: 60, precio: 1300 },
        { hasta: 100, precio: 1500 }, { hasta: 150, precio: 1700 }, { hasta: 200, precio: 2500 },
        { hasta: 300, precio: 2800 }, { hasta: 400, precio: 3300 }
    ];
    const cantAnillados = Math.ceil(hojas / 400);
    const escala = escalas.find(p => Math.ceil(hojas / cantAnillados) <= p.hasta) || escalas[7];
    return escala.precio * cantAnillados;
}

function manejarLogicaCantidadDinamica(index) {
    const item = document.querySelector(`.item-producto[data-index="${index}"]`);
    const seleccionado = item.querySelector(`input[name="items[${index}].detalles[cantidad_producto]"]:checked`);
    const contenedorManual = document.getElementById(`contenedor-cantidad-real-${index}`);
    const inputManual = item.querySelector('.input-cantidad-real');

    if (!seleccionado || !contenedorManual) return;

    if (seleccionado.value === "OTRA" || seleccionado.value.includes("-")) {
        contenedorManual.style.display = 'block';
        if (!isNaN(seleccionado.value)) inputManual.value = 1;
    } else {
        contenedorManual.style.display = 'none';
        inputManual.value = parseInt(seleccionado.value) || 1;
    }
}

// --- 5. EVENTOS INICIALES ---

document.addEventListener("DOMContentLoaded", () => {
    // 1. Activar items existentes
    document.querySelectorAll('.item-producto').forEach(item => {
        const idx = item.getAttribute('data-index');
        inicializarLogicaItem(idx);
    });

    recalcular();

    // 2. Escuchar cambios globales (Checkboxes, inputs generales)
    document.addEventListener('change', (e) => {
        if (e.target.id === 'checkMuestra') {
            const fila = document.getElementById('filaFechaMuestra');
            if(fila) e.target.checked ? fila.classList.remove('d-none') : fila.classList.add('d-none');
        }
        if (['checkFactura', 'esCC'].includes(e.target.id) || e.target.name === 'idMedioPago') recalcular();
    });

    document.addEventListener('input', (e) => {
        if (e.target.id === 'inputAbonado') recalcular();
    });

    // 3. BLOQUEO DE ENVÍO (Validación 50% y Medio de Pago)
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('#btnGuardar');
        if (btn) {
            const form = document.getElementById('formOrden');
            if (!form) return;

            // Referencia al elemento
            const inputAboElement = document.getElementById('inputAbonado');

            // Si está vacío o no es un número, lo seteamos en 0 en el HTML
            if (!inputAboElement.value || isNaN(inputAboElement.value)) {
                inputAboElement.value = 0;
            }

            const total = parseFloat(document.getElementById('inputTotal')?.value) || 0;
            const abonado = parseFloat(inputAboElement.value) || 0;
            const checkCC = document.getElementById('esCC');
            const esCC = checkCC ? checkCC.checked : false;
            const medioSeleccionado = document.querySelector('input[name="idMedioPago"]:checked');

            // Validación Seña
            if (!esCC && total > 0) {
                const minimo = total / 2;
                if (abonado < minimo) {
                    e.preventDefault();
                    alert(`Falta seña. El mínimo es $${minimo} (50%).`);
                    return;
                }
            }

            // Validación Medio de Pago
            if (abonado > 0 && !medioSeleccionado) {
                e.preventDefault();
                alert("Si el cliente abonó, tenés que elegir el Medio de Pago.");
                return;
            }
        }
    });
});