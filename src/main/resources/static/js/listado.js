/**
 * Gestión del Kanban y Filtros - Punto Gráfico
 */
$(document).ready(function() {

    // --- FILTRO POR PRODUCTO ---
    $('#selector-producto').on('change', function() {
        const productoSeleccionado = $(this).val();

        if (productoSeleccionado === "todas") {
            $('.tarjeta-orden').fadeIn();
        } else {
            $('.tarjeta-orden').each(function() {
                const productoTarjeta = $(this).data('producto');
                // Comparamos el nombre del producto guardado en el data-producto
                if (productoTarjeta === productoSeleccionado) {
                    $(this).fadeIn();
                } else {
                    $(this).hide();
                }
            });
        }
    });

    // --- CAMBIOS DE ESTADO (BOTONES) ---
    $(document).on('click', '.btn-cambio-estado', function() {
        const $boton = $(this);
        const filtroActual = $('#selector-producto').val();
        const ordenId = $boton.data('idorden');
        const accion = $boton.data('accion');
        const resta = parseFloat($boton.data('resta') || 0);

        // Validación de entrega con deuda
        if (accion === 'entregado' && resta > 0) {
            const confirmar = confirm(
                `¡Atención! La orden #${ordenId} tiene un saldo pendiente de $${resta}.\n` +
                `¿Deseás marcarla como ENTREGADA de todas formas?`
            );
            if (!confirmar) return;
        }

        // Mapeo de rutas
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

// --- LÓGICA DE ARRANQUE ---
    const selector = $('#selector-producto');
    if (selector.val() !== "todas") {
        selector.trigger('change');
    }
});