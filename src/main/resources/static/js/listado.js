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

        if (quierenEntregarPeroFaltaAbonar(accion, resta)) {
            let confirmar = pedirConfirmacionDeEntregaConRestante();
            if (!confirmar) return;
        }

        const rutas = {
            'proceso':   `/ordenes/pasar-en-proceso/${ordenId}`,
            'sin-hacer': `/ordenes/volver-sin-hacer/${ordenId}`,
            'hecha':     `/ordenes/pasar-lista-para-retirar/${ordenId}`,
            'entregado': `/ordenes/pasar-retirada/${ordenId}`
        };

        const urlFinal = rutas[accion];
        if (urlFinal) {
            window.location.href = urlFinal + "?producto=" + encodeURIComponent(filtroActual);
        }
    });
}

function quierenEntregarPeroFaltaAbonar(accion, resta) {
    return accion === 'entregado' && resta > 0;
}

function pedirConfirmacionDeEntregaConRestante() {
    return confirm(`¡Atención! La orden #${ordenId} tiene un saldo pendiente de $${resta}.\n` +
                    `¿Deseás marcarla como ENTREGADA de todas formas?`);
}

function evaluarFiltroAlRefrescarPagina() {
    const selector = $('#selector-producto');
    if (selector.val() !== "todas") {
        selector.trigger('change');
    }
}