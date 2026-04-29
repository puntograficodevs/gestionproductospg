$(document).ready(function() {
    filtrarPorProductosAlModificarSelector();
    cambiarDeEstadoSegunClick();
    evaluarFiltroAlRefrescarPagina();
});

function filtrarPorProductosAlModificarSelector() {
    $('#selector-producto').on('change', function() {
        const productoSeleccionado = $(this).val();

        if (productoSeleccionado === "todas") {
            $('.tarjeta-orden').fadeIn();
        } else {
            $('.tarjeta-orden').each(function() {
                const productoTarjeta = $(this).data('producto');
                if (productoTarjeta === productoSeleccionado) {
                    $(this).fadeIn();
                } else {
                    $(this).hide();
                }
            });
        }
    });
}

function cambiarDeEstadoSegunClick() {
    $(document).on('click', '.btn-cambio-estado', function() {
        const botonClickeado = $(this);
        const filtroActual = $('#selector-producto').val();
        const ordenId = botonClickeado.data('idorden');
        const accion = botonClickeado.data('accion');
        const resta = parseFloat(botonClickeado.data('resta') || 0);
        const estados = {
            'sin-hacer': 1,
            'proceso':   2,
            'hecha':     3,
            'entregado': 5
        };

        const estadoId = estados[accion];

        if (estadoId === 2) {
            let rolEmpleadoId = +$('#rolEmpleadoId').val();

            if (rolEmpleadoId === 1) {
                $('#modal-nro-orden').text('#' + ordenId);
                const modal = new bootstrap.Modal(document.getElementById('modalAsignarProduccion'));
                modal.show();

                // Configuramos los botones del modal (usamos .off() para no acumular eventos)
                $('#btn-asignar-si').off().on('click', function() {
                    irAEstado(ordenId, estadoId, filtroActual, true);
                });

                $('#btn-asignar-no').off().on('click', function() {
                    irAEstado(ordenId, estadoId, filtroActual, false);
                });
            } else {
                irAEstado(ordenId, estadoId, filtroActual, true);
            }

            return; // Frenamos la ejecución acá
        }

        if (quierenEntregarPeroFaltaAbonar(accion, resta)) {
            let confirmar = pedirConfirmacionDeEntregaConRestante(ordenId, resta);
            if (!confirmar) return;
        }

        irAEstado(ordenId, estadoId, filtroActual, false);
    });
}

function quierenEntregarPeroFaltaAbonar(accion, resta) {
    return accion === 'entregado' && resta > 0;
}

function pedirConfirmacionDeEntregaConRestante(ordenId, resta) {
    return confirm(`¡Atención! La orden #${ordenId} tiene un saldo pendiente de $${resta}.\n` +
                    `¿Deseás marcarla como ENTREGADA de todas formas?`);
}

function irAEstado(id, estado, filtro, asignar) {
    let url = `/ordenes/cambiar-estado/${id}?nuevoEstado=${estado}&producto=${encodeURIComponent(filtro)}`;
    if (asignar) url += "&asignarEncargado=true";
    window.location.href = url;
}

function evaluarFiltroAlRefrescarPagina() {
    const selector = $('#selector-producto');
    if (selector.val() !== "todas") {
        selector.trigger('change');
    }
}