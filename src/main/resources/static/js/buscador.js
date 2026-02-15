/**
 * Lógica del Buscador de Ordenes
 */

// 1. Delegación de eventos: Escuchamos cualquier cambio en el documento
document.addEventListener('change', function(evento) {
    // Si lo que cambió es el checkbox maestro
    if (evento.target.id === 'checkAll') {
        const listaCheckboxesOrden = document.querySelectorAll('.checkOrden');
        listaCheckboxesOrden.forEach(checkbox => {
            checkbox.checked = evento.target.checked;
        });
        actualizarEstadoBotonMasivo();
    }

    // Si lo que cambió es un checkbox individual
    if (evento.target.classList.contains('checkOrden')) {
        actualizarEstadoBotonMasivo();
    }
});

/**
 * Controla la visibilidad del botón de eliminación masiva y el contador.
 */
function actualizarEstadoBotonMasivo() {
    const listaSeleccionados = document.querySelectorAll('.checkOrden:checked');
    const botonEliminarMasivo = document.getElementById('btnEliminarMasivo');
    const etiquetaCantidad = document.getElementById('cantSeleccionadas');
    const checkboxMaestro = document.getElementById('checkAll');

    if (!botonEliminarMasivo) return;

    if (listaSeleccionados.length > 0) {
        botonEliminarMasivo.style.setProperty('display', 'inline-block', 'important');
        if (etiquetaCantidad) {
            etiquetaCantidad.innerText = listaSeleccionados.length;
        }
    } else {
        botonEliminarMasivo.style.setProperty('display', 'none', 'important');
        if (checkboxMaestro) {
            checkboxMaestro.checked = false;
        }
    }
}

/**
 * Recopila los IDs y los envía por POST
 */
window.eliminarSeleccionados = function() {
    const listaSeleccionados = document.querySelectorAll('.checkOrden:checked');
    const listaDeIds = Array.from(listaSeleccionados).map(checkbox => checkbox.value);

    if (listaDeIds.length === 0) return;

    const mensajeConfirmacion = `¿Borrar las ${listaDeIds.length} órdenes seleccionadas?\nEsta acción es irreversible.`;

    if (confirm(mensajeConfirmacion)) {
        const formularioTemporal = document.createElement('form');
        formularioTemporal.method = 'POST';
        formularioTemporal.action = '/ordenes/eliminar-varias';

        const campoIdsOculto = document.createElement('input');
        campoIdsOculto.type = 'hidden';
        campoIdsOculto.name = 'ids';
        campoIdsOculto.value = listaDeIds.join(',');

        formularioTemporal.appendChild(campoIdsOculto);
        document.body.appendChild(formularioTemporal);
        formularioTemporal.submit();
    }
}

// Mantener las funciones de Detalle y Confirmar Individual
window.verDetalle = function(ordenId) {
    const contenedorModalBody = document.getElementById('modalBody');
    contenedorModalBody.innerHTML = `<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>`;

    const elementoModal = document.getElementById('ordenModal');
    const instanciaModal = new bootstrap.Modal(elementoModal);
    instanciaModal.show();

    fetch(`/ordenes/detalle-fragmento/${ordenId}`)
        .then(res => res.text())
        .then(html => contenedorModalBody.innerHTML = html)
        .catch(err => contenedorModalBody.innerHTML = `<div class="alert alert-danger">${err.message}</div>`);
}

window.confirmarEliminar = function(ordenId) {
    if (confirm(`¿Eliminar orden #${ordenId}?`)) {
        window.location.href = `/ordenes/eliminar/${ordenId}`;
    }
}