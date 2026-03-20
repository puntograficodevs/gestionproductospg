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
    const inputProductoId = document.getElementById('currentProductoId');

    if ((parseFloat(inputPrecio.value) || 0) <= 0) {
        alert("Cargá un precio de producto válido antes de confirmar.");
        return;
    }

    // 1. Colapsamos la tarjeta
    const collapseEl = item.querySelector('.collapse');
    if (collapseEl) bootstrap.Collapse.getOrCreateInstance(collapseEl).hide();

    document.getElementById('btn-agregar-otro').classList.remove('d-none');

    // 2. Calculamos el total (Producto + Diseño)
    const pProd = parseFloat(inputPrecio.value) || 0;
    const pDis = parseFloat(item.querySelector('.input-disenio-item')?.value) || 0;
    const sumaItem = Math.ceil(pProd + pDis);

    // 3. Lógica para el TÍTULO DINÁMICO
    const tit = item.querySelector('.titulo-item');
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
        if (!tit.dataset.baseText) {
            tit.dataset.baseText = tit.innerText.split(' - $')[0];
        }
        nombreAMostrar = tit.dataset.baseText;
    }

    // 4. Actualizamos el HTML con el nombre y el precio (con el salto de línea)
    // Usamos innerHTML para que tome el <br>
    tit.innerHTML = `${nombreAMostrar} - <span class="text-success">Precio: $${sumaItem}</span>`;

    recolorarTarjetaConfirmada(item); // Opcional: una pintadita para saber que está OK
    recalcular();
}

function recolorarTarjetaConfirmada(item) {
    const header = item.querySelector('.card-header');

    // Le quitamos las clases por defecto y le ponemos un verde sutil
    header.classList.remove('bg-light');
    header.style.backgroundColor = '#e8f5e9'; // Un verde muy clarito (Material Design)
    header.style.borderBottom = '2px solid #a5d6a7'; // Una línea verde un poco más fuerte

    // Cambiamos el ícono de "flechita" por un tilde de éxito (opcional)
    const icon = header.querySelector('i');
    if (icon) {
        icon.classList.remove('bi-chevron-down');
        icon.classList.add('bi-check-circle-fill', 'text-success');
    }
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

    if (inputPrecioItem.hasAttribute('data-precio-base')) {
        recalcular();
        return;
    }

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
            const paginas = parseInt(detalles["cantidad_paginas"]) || 1;
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
        // 1. Buscamos los elementos necesarios
        const inputPrecioItem = item.querySelector('.input-precio-item');
        const inputCantReal = item.querySelector('.input-cantidad-real');

        // 2. LÓGICA NUEVA: Si es un libro escolar, actualizamos su precio según la cantidad
        const precioBase = parseFloat(inputPrecioItem.getAttribute('data-precio-base'));
        if (!isNaN(precioBase)) {
            const cantidad = parseInt(inputCantReal.value) || 1;
            inputPrecioItem.value = Math.ceil(precioBase * cantidad);
        }

        // 3. Tu lógica de siempre (leer el precio ya actualizado y sumar)
        const pProd = parseFloat(inputPrecioItem.value) || 0;
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
        { hasta: 20, precio: 1500 }, { hasta: 40, precio: 1700 }, { hasta: 60, precio: 1800 },
        { hasta: 100, precio: 1900 }, { hasta: 150, precio: 2200 }, { hasta: 200, precio: 2900 },
        { hasta: 300, precio: 3200 }, { hasta: 400, precio: 3800 }
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
    // 1. LIMPIEZA INICIAL
    document.querySelectorAll('.selector-material-final option').forEach(opt => {
        if (opt.parentElement.tagName === 'SPAN') $(opt).unwrap();
        $(opt).show().prop('disabled', false);
    });

    // 2. INICIALIZAR ITEMS EXISTENTES
    document.querySelectorAll('.item-producto').forEach(item => {
        const idx = item.getAttribute('data-index');
        inicializarLogicaItem(idx);
        $(item).find('.filtro-escolar').first().trigger('change');
    });

    // 3. FORZAR VALOR GUARDADO (El "Salvavidas")
    setTimeout(() => {
        document.querySelectorAll('.item-producto').forEach(item => {
            const valorReal = item.querySelector('.input-material-guardado')?.value;
            const select = item.querySelector('.selector-material-final');

            if (valorReal && valorReal !== "") {
                console.log("Forzando selección de:", valorReal);
                let opt = $(select).find(`option[value="${valorReal}"]`);

                if (opt.length === 0) {
                    opt = $(select).find(`option`).filter(function() {
                        return $(this).text().trim() === valorReal.trim();
                    });
                }

                if (opt.length > 0) {
                    if (opt.parent().is('span')) opt.unwrap();
                    opt.prop('disabled', false).show();
                    $(select).val(opt.val()).trigger('change');
                }
            }
        });
        recalcular();
    }, 400);

    // 4. LISTENERS DE CAMBIOS GENERALES
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

    // 5. VALIDACIÓN ANTES DE GUARDAR
    document.addEventListener('click', (e) => {
        const btn = e.target.closest('#btnGuardar');
        if (btn) {
            const inputAboElement = document.getElementById('inputAbonado');
            if (!inputAboElement.value || isNaN(inputAboElement.value)) inputAboElement.value = 0;

            const total = parseFloat(document.getElementById('inputTotal')?.value) || 0;
            const abonado = parseFloat(inputAboElement.value) || 0;
            const esCC = document.getElementById('esCC')?.checked || false;
            const medioSeleccionado = document.querySelector('input[name="idMedioPago"]:checked');

            if (!esCC && total > 0 && abonado < (total / 2)) {
                e.preventDefault();
                alert(`Falta seña. El mínimo es $${total / 2} (50%).`);
                return;
            }

            if (abonado > 0 && !medioSeleccionado) {
                e.preventDefault();
                alert("Si el cliente abonó, tenés que elegir el Medio de Pago.");
            }
        }
    });
});

// 1. Filtrar el selector de Material
// Filtra el select de Materiales según Escuela, Docente, etc.
// --- NUEVA LÓGICA DE FILTRADO CRUZADO ---
$(document).on('change', '.filtro-escolar', function() {
    const card = $(this).closest('.item-producto');
    const todosLosSelectsFiltro = card.find('.filtro-escolar');
    const selectMaterial = card.find('.selector-material-final');

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
        let det = {};
        try { det = JSON.parse($(this).attr('data-detalles')); } catch (e) { return; }

        let cumpleConFiltros = true;
        for (let campo in filtrosActivos) {
            let valFiltro = String(filtrosActivos[campo]).trim().toUpperCase();
            let valCatalogo = String(det[campo] || "").trim().toUpperCase();
            if (valFiltro !== valCatalogo) { cumpleConFiltros = false; break; }
        }

        if (cumpleConFiltros) {
            Object.keys(opcionesValidas).forEach(key => {
                if (det[key]) opcionesValidas[key].add(String(det[key]).trim().toUpperCase());
            });
        }
    });

    // 3. ACTUALIZAMOS LOS SELECTS DE FILTRO (Mostrar/Ocultar opciones)
    todosLosSelectsFiltro.each(function() {
        const selectActual = $(this);
        const campoActual = selectActual.data('campo');
        const valorSeleccionadoActual = selectActual.val();

        selectActual.find('option').each(function() {
            const opt = $(this);
            const optVal = opt.val();
            if (optVal === "") return;

            const optValNormalizado = optVal.trim().toUpperCase();
            if (opcionesValidas[campoActual].has(optValNormalizado) || optVal === valorSeleccionadoActual) {
                if (opt.parent().is('span')) opt.unwrap();
                opt.show().prop('disabled', false);
            } else {
                opt.prop('disabled', true).hide();
                if (!opt.parent().is('span')) opt.wrap('<span style="display:none;"></span>');
            }
        });
    });

    // 4. FILTRAR EL SELECT FINAL (El libro)
    selectMaterial.find('option').each(function() {
        const opt = $(this);
        if (opt.val() === "") return;
        let det = {};
        try { det = JSON.parse(opt.attr('data-detalles')); } catch(e) { return; }

        let coincide = true;
        for (let campo in filtrosActivos) {
            if (String(det[campo] || "").trim().toUpperCase() !== String(filtrosActivos[campo]).trim().toUpperCase()) {
                coincide = false;
                break;
            }
        }

        if (coincide || opt.is(':selected')) {
            if (opt.parent().is('span')) opt.unwrap();
            opt.show().prop('disabled', false);
        } else {
            opt.prop('disabled', true).hide();
            if (!opt.parent().is('span')) opt.wrap('<span style="display:none;"></span>');
        }
    });

    // 5. LÓGICA VISUAL: Pintar de verde lo seleccionado (FUERA DE LOS BUCLES ANTERIORES)
    todosLosSelectsFiltro.each(function() {
        const sel = $(this);
        if (sel.val() && sel.val() !== "") {
            sel.addClass('is-valid bg-success-subtle').removeClass('border-secondary');
        } else {
            sel.removeClass('is-valid bg-success-subtle');
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
    const select = $(this);
    const opt = select.find(':selected');
    if (opt.val() === "") return;

    const card = select.closest('.item-producto');
    const index = card.attr('data-index');

    // 1. Extraemos la data del catálogo
    const det = JSON.parse(opt.attr('data-detalles'));
    const precioBase = parseFloat(opt.attr('data-precio'));

    // 2. Limpiamos campos automáticos previos (para que no se acumulen si cambia de libro)
    card.find('.detalle-automatico-catalogo').remove();

    // 3. Procesamos cada detalle del libro
    Object.keys(det).forEach(key => {
        const valor = det[key];

        // Buscamos si ya existe un input en el HTML para este detalle
        // Tu HTML usa el formato: items[0].detalles[nombre]
        let inputExistente = card.find(`[name="items[${index}].detalles[${key}]"]`);

        if (inputExistente.length > 0) {
            // Si ya existe (ej: tipo_faz), lo actualizamos
            if (inputExistente.attr('type') === 'radio') {
                card.find(`[name="items[${index}].detalles[${key}]"][value="${valor}"]`).prop('checked', true);
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
                name: `items[${index}].detalles[${key}]`,
                value: valor,
                class: 'detalle-automatico-catalogo'
            }).appendTo(card);
        }
    });

    // 4. Sincronizamos el precio
    const inputPrecio = card.find('.input-precio-item');
    inputPrecio.attr('data-precio-base', precioBase);

    // Llamamos a recalcular para que aplique Precio * Cantidad
    recalcular();
});