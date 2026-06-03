let debounceTimer;
let itemsProductoIniciales = document.querySelectorAll('.item-producto');
let indiceActualItem = itemsProductoIniciales.length > 0 ? (itemsProductoIniciales.length - 1) : 0;
const ID_PRODUCTO_BIROMES = 25;
const PRECIO_DISENIO_BIROMES = 6500;

// --- 1. INICIALIZACIÓN Y CARGA ---

function seleccionarProducto(productoId) {
    const contenedor = document.getElementById('contenedor-formulario-dinamico');

    fetch(`/ordenes/formulario-producto/${productoId}?index=0`)
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

function inicializarLogicaItem(indiceItem) {
    const item = document.querySelector(`.item-producto[data-index="${indiceItem}"]`);
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

    manejarLogicaCantidadDinamica(indiceItem);

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
    item.addEventListener('input', (evento) => {
        const target = evento.target;

        // Si cambia un campo de "detalles" (ej: Modelo de Sello)
        if (target.classList.contains('input-dinamico')) {
            const match = target.name.match(/detalles\[(.*?)\]/);
            if (match) {
                const nombreCampo = match[1];
                let valorActual = target.type === 'checkbox' ? (target.checked ? "on" : "off") : target.value;

                item.querySelectorAll(`[data-depends-on="${nombreCampo}"]`).forEach(bloque => {
                    const valorRequerido = bloque.getAttribute('data-show-if');
                    bloque.style.display = (String(valorActual) === String(valorRequerido)) ? 'block' : 'none';
                });

                if (nombreCampo === 'cantidad_producto') manejarLogicaCantidadDinamica(indiceItem);
                if (nombreCampo === 'objeto_a_sublimar') recalcular();
                debounceBuscarPrecio(indiceItem);
            }
        }

        // Si cambia el precio manual o la CANTIDAD manual (para multiplicar)
        if (target.classList.contains('input-precio-item') ||
            target.classList.contains('input-cantidad-real') ||
            target.classList.contains('input-disenio-item') ||
            target.classList.contains('check-disenio-item')) {

            if (target.classList.contains('input-cantidad-real')) {
                debounceBuscarPrecio(indiceItem); // Re-calcula con la nueva cantidad
            }
            recalcular();
        }
    });
}

function correspondeDisenioBirome(item) {
    const productoId = parseInt(document.getElementById('currentProductoId')?.value);
    if (productoId !== ID_PRODUCTO_BIROMES) return false;

    const objetoASublimar = item.querySelector('[name*="detalles[objeto_a_sublimar]"]:checked')?.value || "";
    return objetoASublimar.toUpperCase() === "BIROME";
}

// --- 2. LÓGICA DE AGREGAR, CONFIRMAR Y ELIMINAR ---

function confirmarItem(indiceItem) {
    const item = document.querySelector(`.item-producto[data-index="${indiceItem}"]`);
    const inputPrecioProducto = item.querySelector('.input-precio-item');
    const inputProductoId = document.getElementById('currentProductoId');

    if ((parseFloat(inputPrecioProducto.value) || 0) <= 0) {
        alert("Cargá un precio de producto válido antes de confirmar.");
        return;
    }

    // 1. Colapsamos la tarjeta
    const collapseEl = item.querySelector('.collapse');
    if (collapseEl) bootstrap.Collapse.getOrCreateInstance(collapseEl).hide();

    document.getElementById('btn-agregar-otro').classList.remove('d-none');

    // 2. Calculamos el total (Producto + Diseño)
    const precioProducto = parseFloat(inputPrecioProducto.value) || 0;
    const precioDisenio = parseFloat(item.querySelector('.input-disenio-item')?.value) || 0;
    const precioTotalItem = Math.ceil(precioProducto + precioDisenio);

    // 3. Lógica para el TÍTULO DINÁMICO
    const tituloItem = item.querySelector('.titulo-item');
    let nombreAMostrar = "";

    // Si es Copias Escolares (ID 34), intentamos sacar el nombre del material
    if (inputProductoId.value == "34") {
        const selectMaterial = item.querySelector('.selector-material-final');
        const materialSeleccionado = selectMaterial.options[selectMaterial.selectedIndex]?.text;

        // Si eligió un material, usamos ese nombre. Si no, el genérico.
        nombreAMostrar = (materialSeleccionado && selectMaterial.value !== "")
                         ? materialSeleccionado
                         : "Copias Escolares";
    } else {
        // Para el resto de los productos, usamos el nombre base que ya tenía
        if (!tituloItem.dataset.baseText) {
            tituloItem.dataset.baseText = tituloItem.innerText.split(' - $')[0];
        }
        nombreAMostrar = tituloItem.dataset.baseText;
    }

    // 4. Actualizamos el HTML con el nombre y el precio (con el salto de línea)
    // Usamos innerHTML para que tome el <br>
    tituloItem.innerHTML = `${nombreAMostrar} - <span class="text-success">Precio: $${precioTotalItem}</span>`;

    recolorarTarjetaConfirmada(item); // Opcional: una pintadita para saber que está OK
    recalcular();
}

function recolorarTarjetaConfirmada(item) {
    const encabezadoTarjeta = item.querySelector('.card-header');

    // Le quitamos las clases por defecto y le ponemos un verde sutil
    encabezadoTarjeta.classList.remove('bg-light');
    encabezadoTarjeta.style.backgroundColor = '#e8f5e9'; // Un verde muy clarito (Material Design)
    encabezadoTarjeta.style.borderBottom = '2px solid #a5d6a7'; // Una línea verde un poco más fuerte

    // Cambiamos el ícono de "flechita" por un tilde de éxito (opcional)
    const iconoEncabezado = encabezadoTarjeta.querySelector('i');
    if (iconoEncabezado) {
        iconoEncabezado.classList.remove('bi-chevron-down');
        iconoEncabezado.classList.add('bi-check-circle-fill', 'text-success');
    }
}

function duplicarItemActual() {
    indiceActualItem++;
    const listaItemsProductos = document.getElementById('lista-items-productos');
    const productoId = document.getElementById('currentProductoId').value;

    fetch(`/ordenes/formulario-producto/${productoId}?index=${indiceActualItem}`)
        .then(response => response.text())
        .then(html => {
            listaItemsProductos.insertAdjacentHTML('beforeend', html);
            const nuevoItem = listaItemsProductos.querySelector(`.item-producto[data-index="${indiceActualItem}"]`);
            const contenedorCollapse = nuevoItem.querySelector('.collapse');
            if (contenedorCollapse) {
                const instanciaCollapse = new bootstrap.Collapse(contenedorCollapse, { toggle: false });
                instanciaCollapse.show();
            }
            document.getElementById('btn-agregar-otro').classList.add('d-none');
            inicializarLogicaItem(indiceActualItem);
            buscarPrecioCatalogo(indiceActualItem); // Buscamos precio para el nuevo ítem
            setTimeout(() => { nuevoItem.scrollIntoView({ behavior: 'smooth', block: 'start' }); }, 300);
        })
        .catch(error => console.error('Error:', error));
}

function eliminarItem(botonEliminar) {
    const item = botonEliminar.closest('.item-producto');
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

function buscarPrecioCatalogo(indiceItem) {
    const item = document.querySelector(`.item-producto[data-index="${indiceItem}"]`);
    const inputProductoId = document.getElementById('currentProductoId');
    const inputPrecioItem = item.querySelector('.input-precio-item');
    const inputCantidadReal = item.querySelector('.input-cantidad-real');
    const tituloPagina = document.querySelector('h2');

    if (inputPrecioItem.hasAttribute('data-precio-base')) {
        recalcular();
        return;
    }

    if (!inputProductoId?.value || !inputPrecioItem) return;

    const nombreProducto = tituloPagina ? tituloPagina.innerText.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase() : "";
    const esImpresion = nombreProducto.includes("impresion");
    const esSello = nombreProducto.includes("sellos madera") || nombreProducto.includes("sellos automaticos");
    let detalles = {};

    // Capturamos los campos dinámicos
    item.querySelectorAll('.input-dinamico').forEach(input => {
        const match = input.name.match(/detalles\[(.*?)\]/);
        if (!match) return;

        const nombreCampo = match[1];
        let valorDetalle = null;

        if (input.type === 'checkbox') {
            // Mandamos true o false SIEMPRE para que el catálogo no falle
            valorDetalle = input.checked;
        } else if (input.type === 'radio') {
            if (input.checked) valorDetalle = input.value;
        } else {
            valorDetalle = input.value;
        }

        // Solo agregamos al JSON si el valor no es nulo
        if (valorDetalle !== null && valorDetalle !== "") {
            detalles[nombreCampo] = valorDetalle;
        }
    });

    // Lógica especial para impresiones
    if (esImpresion && detalles["cantidad_paginas"]) {
        const paginas = parseInt(detalles["cantidad_paginas"]) || 0;
        const limite = (detalles["tipo_faz"] === "DOBLE FAZ") ? 100 : 50;
        detalles["rango_impresion"] = (paginas <= limite) ? "1-LIMITE" : "LIMITE-MAS";
    }

    console.log("Enviando al catálogo:", detalles); // Mirá esto en F12

    fetch('/api/catalogo/buscar-precio', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productoId: parseInt(inputProductoId.value), detalles: detalles })
    })
    .then(response => response.json())
    .then(precioRecibido => {
        // Si no hay precio (0), permitimos edición manual
        if (precioRecibido === 0) {
            inputPrecioItem.readOnly = false;
            return;
        }

        let precioFinal;
        const cantidadFinal = parseInt(inputCantidadReal.value) || 1;

        if (esImpresion) {
            const paginas = parseInt(detalles["cantidad_paginas"]) || 1;
            const costoHojas = precioRecibido * paginas;
            let costoAnillado = (detalles["es_anillado"] === true) ? calcularLogicaAnillado(paginas, detalles["tipo_faz"]) : 0;
            precioFinal = (costoHojas + costoAnillado) * cantidadFinal;
        } else if (esSello) {
            const quiereAlmohadilla = item.querySelector('[name*="detalles[agrega_almohadilla]"]')?.checked;
            const quiereTinta = item.querySelector('[name*="detalles[agrega_tinta]"]')?.checked;
            const quiereRodillo = item.querySelector('[name*="detalles[agrega_rodillo]"]')?.checked;

            const precioAlmohadilla = quiereAlmohadilla ? (parseInt(item.querySelector('[name*="precio_almohadilla"]')?.value) || 0) : 0;
            const precioTinta = quiereTinta ? (parseInt(item.querySelector('[name*="precio_tinta"]')?.value) || 0) : 0;
            const precioRodillo = quiereRodillo ? 3200 : 0;

            precioFinal = (precioRecibido * cantidadFinal) + precioAlmohadilla + precioTinta + precioRodillo;
        } else {
            // Para sellos de madera, automáticos, etc.
            const tienePackDefinido = detalles["cantidad_producto"] && detalles["cantidad_producto"] !== "OTRA";

            if (tienePackDefinido) {
                // Es un pack (ej: 100 tarjetas), el precio es el del pack total.
                precioFinal = precioRecibido;
            } else {
                // Es precio base (ej: 1 sello), multiplicamos por la cantidad manual.
                precioFinal = precioRecibido * cantidadFinal;
            }
        }

        inputPrecioItem.value = Math.ceil(precioFinal);
        inputPrecioItem.readOnly = true;
        recalcular();
    })
    .catch(error => {
        console.error("Error en catálogo:", error);
        inputPrecioItem.readOnly = false;
    });
}

function recalcular() {
    const inputSubtotalProductos = document.getElementById('inputPrecioProd');
    const inputSubtotalDisenio = document.getElementById('inputPrecioDisenio');
    const inputRecargoMedioPago = document.getElementById('inputRecargoMedioPago');
    const inputTotal = document.getElementById('inputTotal');
    const inputAbonado = document.getElementById('inputAbonado');
    const inputResta = document.getElementById('inputResta');
    const checkCuentaCorriente = document.getElementById('esCC');
    const precioDisenioBase = parseFloat(document.getElementById('precioDisenioBase')?.value) || 0;

    let sumaProductos = 0;
    let sumaDisenios = 0;

    document.querySelectorAll('.item-producto').forEach(item => {
        // 1. Buscamos los elementos necesarios
        const inputPrecioItem = item.querySelector('.input-precio-item');
        const inputCantidadReal = item.querySelector('.input-cantidad-real');

        // 2. LÓGICA NUEVA: Si es un libro escolar, actualizamos su precio según la cantidad
        const precioBase = parseFloat(inputPrecioItem.getAttribute('data-precio-base'));
        if (!isNaN(precioBase)) {
            const cantidad = parseInt(inputCantidadReal.value) || 1;
            inputPrecioItem.value = Math.ceil(precioBase * cantidad);
        }

        // 3. Tu lógica de siempre (leer el precio ya actualizado y sumar)
        const precioProducto = parseFloat(inputPrecioItem.value) || 0;
        const checkDisenio = item.querySelector('.check-disenio-item');
        const inputDisenio = item.querySelector('.input-disenio-item');

        const tieneAdicionalDisenio = checkDisenio && checkDisenio.checked;
        const esDisenioBirome = correspondeDisenioBirome(item);
        const precioDisenioAplicable = esDisenioBirome ? PRECIO_DISENIO_BIROMES : precioDisenioBase;

        if (inputDisenio && tieneAdicionalDisenio) {
            inputDisenio.readOnly = esDisenioBirome;
            if (document.activeElement !== inputDisenio && (esDisenioBirome || parseFloat(inputDisenio.value) === 0 || !inputDisenio.value)) {
                inputDisenio.value = Math.ceil(precioDisenioAplicable);
            }
        } else if (inputDisenio) {
            inputDisenio.value = 0;
            inputDisenio.readOnly = true;
        }

        sumaProductos += precioProducto;
        sumaDisenios += parseFloat(inputDisenio?.value) || 0;
    });

    if (inputSubtotalProductos) inputSubtotalProductos.value = Math.ceil(sumaProductos);
    if (inputSubtotalDisenio) inputSubtotalDisenio.value = Math.ceil(sumaDisenios);

    const subtotalBase = sumaProductos + sumaDisenios;
    let totalCorriendo = subtotalBase;
    let recargoMedioPago = 0;

    const radioPago = document.querySelector('input[name="idMedioPago"]:checked');
    if (radioPago && parseInt(radioPago.value) === 2) {
        recargoMedioPago = totalCorriendo * 0.10;
        totalCorriendo += recargoMedioPago;
    }

    const totalFinal = Math.ceil(totalCorriendo);
    const valorAbonado = parseFloat(inputAbonado?.value) || 0;

    if (inputRecargoMedioPago) inputRecargoMedioPago.value = Math.ceil(recargoMedioPago);
    if (inputTotal) inputTotal.value = totalFinal;
    if (inputResta) inputResta.value = totalFinal - valorAbonado;

    // Validación visual de seña
    if (inputAbonado && checkCuentaCorriente && !checkCuentaCorriente.checked && totalFinal > 0) {
        valorAbonado < (totalFinal / 2) ? inputAbonado.classList.add('border-danger', 'text-danger') : inputAbonado.classList.remove('border-danger', 'text-danger');
    }
}

// --- 4. FUNCIONES AUXILIARES ---

function debounceBuscarPrecio(indiceItem) {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(() => buscarPrecioCatalogo(indiceItem), 300);
}

function calcularLogicaAnillado(paginas, tipoFaz) {
    let cantidadHojas = (tipoFaz === "DOBLE FAZ") ? Math.ceil(paginas / 2) : paginas;
    if (cantidadHojas === 0) return 0;
    const escalas = [
        { hasta: 20, precio: 1500 }, { hasta: 40, precio: 1700 }, { hasta: 60, precio: 1800 },
        { hasta: 100, precio: 1900 }, { hasta: 150, precio: 2200 }, { hasta: 200, precio: 2900 },
        { hasta: 300, precio: 3200 }, { hasta: 400, precio: 3800 }
    ];
    const cantidadAnillados = Math.ceil(cantidadHojas / 400);
    const escala = escalas.find(escalaAnillado => Math.ceil(cantidadHojas / cantidadAnillados) <= escalaAnillado.hasta) || escalas[7];
    return escala.precio * cantidadAnillados;
}

function manejarLogicaCantidadDinamica(indiceItem) {
    const item = document.querySelector(`.item-producto[data-index="${indiceItem}"]`);
    const cantidadSeleccionada = item.querySelector(`input[name="items[${indiceItem}].detalles[cantidad_producto]"]:checked`);
    const contenedorCantidadManual = document.getElementById(`contenedor-cantidad-real-${indiceItem}`);
    const inputManual = item.querySelector('.input-cantidad-real');

    if (!cantidadSeleccionada || !contenedorCantidadManual) return;

    if (cantidadSeleccionada.value === "OTRA" || cantidadSeleccionada.value.includes("-")) {
        contenedorCantidadManual.style.display = 'block';
        if (!isNaN(cantidadSeleccionada.value)) inputManual.value = 1;
    } else {
        contenedorCantidadManual.style.display = 'none';
        inputManual.value = parseInt(cantidadSeleccionada.value) || 1;
    }
}

// --- 5. EVENTOS INICIALES ---

document.addEventListener("DOMContentLoaded", () => {
    // 1. LIMPIEZA INICIAL
    document.querySelectorAll('.selector-material-final option').forEach(opcionMaterial => {
        if (opcionMaterial.parentElement.tagName === 'SPAN') $(opcionMaterial).unwrap();
        $(opcionMaterial).show().prop('disabled', false);
    });

    // 2. INICIALIZAR ITEMS EXISTENTES
    document.querySelectorAll('.item-producto').forEach(item => {
        const indiceItem = item.getAttribute('data-index');
        inicializarLogicaItem(indiceItem);
        $(item).find('.filtro-escolar').first().trigger('change');
    });

    // 3. FORZAR VALOR GUARDADO (El "Salvavidas")
    setTimeout(() => {
        document.querySelectorAll('.item-producto').forEach(item => {
            const valorReal = item.querySelector('.input-material-guardado')?.value;
            const selectMaterial = item.querySelector('.selector-material-final');

            if (valorReal && valorReal !== "") {
                console.log("Forzando selección de:", valorReal);
                let opcionSeleccionada = $(selectMaterial).find(`option[value="${valorReal}"]`);

                if (opcionSeleccionada.length === 0) {
                    opcionSeleccionada = $(selectMaterial).find(`option`).filter(function() {
                        return $(this).text().trim() === valorReal.trim();
                    });
                }

                if (opcionSeleccionada.length > 0) {
                    if (opcionSeleccionada.parent().is('span')) opcionSeleccionada.unwrap();
                    opcionSeleccionada.prop('disabled', false).show();
                    $(selectMaterial).val(opcionSeleccionada.val()).trigger('change');
                }
            }
        });
        recalcular();
    }, 400);

    // --- VALIDACIÓN DE FECHAS (NUEVO) ---
        const inputEntrega = document.getElementById('fechaEntrega');
        const inputMuestra = document.getElementById('fechaMuestra');

        // 1. Seteamos el mínimo de hoy para ambas fechas al cargar
        const fechaHoy = new Date().toISOString().split('T')[0];
        if (inputEntrega) inputEntrega.setAttribute('min', fechaHoy);
        if (inputMuestra) inputMuestra.setAttribute('min', fechaHoy);

        // 2. Función para que la muestra no se pase de la entrega
        function ajustarLimitesMuestra() {
            if (!inputEntrega || !inputMuestra) return;

            const fechaEntregaSeleccionada = inputEntrega.value;
            if (fechaEntregaSeleccionada) {
                inputMuestra.setAttribute('max', fechaEntregaSeleccionada);

                if (inputMuestra.value && inputMuestra.value > fechaEntregaSeleccionada) {
                    inputMuestra.value = ""; // Limpiamos para obligar a elegir bien
                    inputMuestra.classList.add('border-warning');
                } else {
                    inputMuestra.classList.remove('border-warning');
                }
            }
        }

        if (inputEntrega) {
            inputEntrega.addEventListener('change', ajustarLimitesMuestra);
            // Ejecutamos una vez por si estamos editando una orden vieja
            ajustarLimitesMuestra();
        }
        // --- FIN VALIDACIÓN DE FECHAS ---

    // 4. LISTENERS DE CAMBIOS GENERALES
    document.addEventListener('change', (evento) => {
        if (evento.target.id === 'checkMuestra') {
            const filaFechaMuestra = document.getElementById('filaFechaMuestra');
            if(filaFechaMuestra) evento.target.checked ? filaFechaMuestra.classList.remove('d-none') : filaFechaMuestra.classList.add('d-none');
        }
        if (['checkFactura', 'esCC'].includes(evento.target.id) || evento.target.name === 'idMedioPago') recalcular();
    });

    document.addEventListener('input', (evento) => {
        if (evento.target.id === 'inputAbonado') recalcular();
    });

    // 5. VALIDACIÓN ANTES DE GUARDAR
    document.addEventListener('click', (evento) => {
        const botonGuardar = evento.target.closest('#btnGuardar');
        if (botonGuardar) {
            const inputAbonadoElement = document.getElementById('inputAbonado');
            if (!inputAbonadoElement.value || isNaN(inputAbonadoElement.value)) inputAbonadoElement.value = 0;

            const totalOrden = parseFloat(document.getElementById('inputTotal')?.value) || 0;
            const valorAbonado = parseFloat(inputAbonadoElement.value) || 0;
            const esCuentaCorriente = document.getElementById('esCC')?.checked || false;
            const medioPagoSeleccionado = document.querySelector('input[name="idMedioPago"]:checked');

            if (!esCuentaCorriente && totalOrden > 0 && valorAbonado < (totalOrden / 2)) {
                evento.preventDefault();
                alert(`Falta seña. El mínimo es $${totalOrden / 2} (50%).`);
                return;
            }

            if (valorAbonado > 0 && !medioPagoSeleccionado) {
                evento.preventDefault();
                alert("Si el cliente abonó, tenés que elegir el Medio de Pago.");
            }

            // --- DENTRO DEL LISTENER DE 'click' DEL #btnGuardar ---
            const inputEntrega = document.getElementById('fechaEntrega');
            const inputMuestra = document.getElementById('fechaMuestra');

            if (inputEntrega && inputMuestra && inputMuestra.value) {
                const fechaEntrega = new Date(inputEntrega.value);
                const fechaMuestra = new Date(inputMuestra.value);

                if (fechaMuestra > fechaEntrega) {
                    evento.preventDefault();
                    // Le damos foco y pintamos de rojo para que se note el error
                    inputMuestra.classList.add('is-invalid', 'border-danger');
                    alert("La fecha de MUESTRA no puede ser después de la fecha de ENTREGA.");
                    inputMuestra.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    return;
                } else {
                    inputMuestra.classList.remove('is-invalid', 'border-danger');
                }
            }
        }
    });
});

$(document).on('change', '.filtro-escolar', function() {
    const tarjetaItem = $(this).closest('.item-producto');
    const todosLosSelectsFiltro = tarjetaItem.find('.filtro-escolar');
    const selectMaterial = tarjetaItem.find('.selector-material-final');

    // 1. Capturamos qué filtros eligió el usuario hasta ahora
    const filtrosActivos = {};
    todosLosSelectsFiltro.each(function() {
        const campo = $(this).data('campo');
        const valor = $(this).val();
        if (valor && valor !== "") {
            filtrosActivos[campo] = valor;
        }
    });

    // 2. Analizamos el catálogo completo para ver qué combinaciones existen
    const opcionesValidas = {
        escuela: new Set(),
        docente: new Set(),
        anio: new Set(),
        materia: new Set(),
        orientacion: new Set()
    };

    selectMaterial.find('option').each(function() {
        if ($(this).val() === "") return;
        let detallesMaterial = {};
        try { detallesMaterial = JSON.parse($(this).attr('data-detalles')); } catch (error) { return; }

        let cumpleConFiltros = true;
        for (let campo in filtrosActivos) {
            let valorFiltro = String(filtrosActivos[campo]).trim().toUpperCase();
            let valorCatalogo = String(detallesMaterial[campo] || "").trim().toUpperCase();
            if (valorFiltro !== valorCatalogo) { cumpleConFiltros = false; break; }
        }

        if (cumpleConFiltros) {
            Object.keys(opcionesValidas).forEach(key => {
                if (detallesMaterial[key]) opcionesValidas[key].add(String(detallesMaterial[key]).trim().toUpperCase());
            });
        }
    });

    // 3. ACTUALIZAMOS LOS SELECTS DE FILTRO (Mostrar/Ocultar opciones)
    todosLosSelectsFiltro.each(function() {
        const selectActual = $(this);
        const campoActual = selectActual.data('campo');
        const valorSeleccionadoActual = selectActual.val();

        selectActual.find('option').each(function() {
            const opcionFiltro = $(this);
            const valorOpcion = opcionFiltro.val();
            if (valorOpcion === "") return;

            const valorOpcionNormalizado = valorOpcion.trim().toUpperCase();
            if (opcionesValidas[campoActual].has(valorOpcionNormalizado) || valorOpcion === valorSeleccionadoActual) {
                if (opcionFiltro.parent().is('span')) opcionFiltro.unwrap();
                opcionFiltro.show().prop('disabled', false);
            } else {
                opcionFiltro.prop('disabled', true).hide();
                if (!opcionFiltro.parent().is('span')) opcionFiltro.wrap('<span style="display:none;"></span>');
            }
        });
    });

    // 4. FILTRAR EL SELECT FINAL (El libro)
    selectMaterial.find('option').each(function() {
        const opcionMaterial = $(this);
        if (opcionMaterial.val() === "") return;
        let detallesMaterial = {};
        try { detallesMaterial = JSON.parse(opcionMaterial.attr('data-detalles')); } catch(error) { return; }

        let coincide = true;
        for (let campo in filtrosActivos) {
            if (String(detallesMaterial[campo] || "").trim().toUpperCase() !== String(filtrosActivos[campo]).trim().toUpperCase()) {
                coincide = false;
                break;
            }
        }

        if (coincide || opcionMaterial.is(':selected')) {
            if (opcionMaterial.parent().is('span')) opcionMaterial.unwrap();
            opcionMaterial.show().prop('disabled', false);
        } else {
            opcionMaterial.prop('disabled', true).hide();
            if (!opcionMaterial.parent().is('span')) opcionMaterial.wrap('<span style="display:none;"></span>');
        }
    });

    // 5. LÓGICA VISUAL: Pintar de verde lo seleccionado (FUERA DE LOS BUCLES ANTERIORES)
    todosLosSelectsFiltro.each(function() {
        const selectFiltro = $(this);
        if (selectFiltro.val() && selectFiltro.val() !== "") {
            selectFiltro.addClass('is-valid bg-success-subtle').removeClass('border-secondary');
        } else {
            selectFiltro.removeClass('is-valid bg-success-subtle');
        }
    });

    if (selectMaterial.val() !== "") {
        selectMaterial.addClass('is-valid');
    } else {
        selectMaterial.removeClass('is-valid');
    }
});

// 2. Al elegir el Material: Pre-cargar campos técnicos y precio
// Cuando elegís un libro, llena el precio y los detalles técnicos de abajo
$(document).on('change', '.selector-material-final', function() {
    const selectMaterial = $(this);
    const opcionSeleccionada = selectMaterial.find(':selected');
    if (opcionSeleccionada.val() === "") {
        recalcular();
        return;
    }

    const tarjetaItem = selectMaterial.closest('.item-producto');
    const indiceItem = tarjetaItem.attr('data-index');

    // 1. Extraemos la data del catálogo
    const detallesMaterial = JSON.parse(opcionSeleccionada.attr('data-detalles'));
    const precioBase = parseFloat(opcionSeleccionada.attr('data-precio'));

    // 2. Limpiamos campos automáticos previos (para que no se acumulen si cambia de libro)
    tarjetaItem.find('.detalle-automatico-catalogo').remove();

    // 3. Procesamos cada detalle del libro
    Object.keys(detallesMaterial).forEach(nombreDetalle => {
        const valor = detallesMaterial[nombreDetalle];

        // Buscamos si ya existe un input en el HTML para este detalle
        // Tu HTML usa el formato: items[0].detalles[nombre]
        let inputExistente = tarjetaItem.find(`[name="items[${indiceItem}].detalles[${nombreDetalle}]"]`);

        if (inputExistente.length > 0) {
            // Si ya existe (ej: tipo_faz), lo actualizamos
            if (inputExistente.attr('type') === 'radio') {
                tarjetaItem.find(`[name="items[${indiceItem}].detalles[${nombreDetalle}]"][value="${valor}"]`).prop('checked', true);
            } else if (inputExistente.attr('type') === 'checkbox') {
                inputExistente.prop('checked', (valor === "on" || valor === true || valor === "true"));
            } else {
                inputExistente.val(valor);
            }
        } else {
            // SI NO EXISTE (ej: escuela, docente), lo creamos oculto
            // Esto hace que Spring lo agregue al Map de detalles automáticamente
            $('<input>').attr({
                type: 'hidden',
                name: `items[${indiceItem}].detalles[${nombreDetalle}]`,
                value: valor,
                class: 'detalle-automatico-catalogo'
            }).appendTo(tarjetaItem);
        }
    });

    // 4. Sincronizamos el precio
    const inputPrecioProducto = tarjetaItem.find('.input-precio-item');
    inputPrecioProducto.attr('data-precio-base', precioBase);

    // Llamamos a recalcular para que aplique Precio * Cantidad
    recalcular();
});
