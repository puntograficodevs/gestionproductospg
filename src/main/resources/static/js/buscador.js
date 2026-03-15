document.addEventListener('change', function(evento) {
    if (evento.target.id === 'checkbox-seleccionar-todas-las-ordenes') {
        const listaCheckboxesOrden = document.querySelectorAll('.checkbox-seleccionar-orden');
        listaCheckboxesOrden.forEach(checkbox => {
            checkbox.checked = evento.target.checked;
        });
        actualizarEstadoBotonMasivo();
    }

    if (evento.target.classList.contains('checkbox-seleccionar-orden')) {
        actualizarEstadoBotonMasivo();
    }
});

function actualizarEstadoBotonMasivo() {
    const listaSeleccionados = document.querySelectorAll('.checkbox-seleccionar-orden:checked');
    const botonEliminarMasivo = document.getElementById('btn-eliminar-masivo');
    const etiquetaCantidad = document.getElementById('cantSeleccionadas');
    const checkboxMaestro = document.getElementById('checkbox-seleccionar-todas-las-ordenes');

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

window.eliminarSeleccionados = function() {
    const listaSeleccionados = document.querySelectorAll('.checkbox-seleccionar-orden:checked');
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

window.verDetalle = function(ordenId) {
    const contenedorModalBody = document.getElementById('modalBody');
    contenedorModalBody.innerHTML = `<div class="text-center py-5"><div class="spinner-border text-primary"></div></div>`;

    const elementoModalDetalle = document.getElementById('ordenModal');
    const instanciaModalDetalle = new bootstrap.Modal(elementoModalDetalle);
    instanciaModalDetalle.show();

    fetch(`/ordenes/detalle-fragmento/${ordenId}?esModal=true`)
        .then(res => res.text())
        .then(html => {
            contenedorModalBody.innerHTML = html;

            const modalPago = contenedorModalBody.querySelector('.modal');
            if (modalPago) {
                document.body.appendChild(modalPago);
            }
        })
        .catch(err => contenedorModalBody.innerHTML = `<div class="alert alert-danger">${err.message}</div>`);
}

window.confirmarEliminar = function(ordenId) {
    if (confirm(`¿Eliminar orden #${ordenId}?\nEsta acción es irreversible.`)) {
        window.location.href = `/ordenes/eliminar/${ordenId}`;
    }
}