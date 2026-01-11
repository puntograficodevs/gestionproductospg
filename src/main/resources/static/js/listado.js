document.addEventListener('DOMContentLoaded', () => {
    const botonesVer = document.querySelectorAll('.ver-btn');
    const modalElement = document.getElementById('ordenModal');
    const cuerpoModal = document.getElementById("modalBody");

    const modal = new bootstrap.Modal(modalElement);

    botonesVer.forEach(boton => {
        boton.addEventListener('click', async () => {
            const idOrden = boton.dataset.idorden;
            const tipoProducto = boton.dataset.tipoproducto;

            cuerpoModal.innerHTML = '<p class="text-center">Cargando los datos de la orden...</p>';

            try {
                const html = await fetch(
                  `/modal/mostrar-odt-producto/${idOrden}`
                ).then(r => r.text());

                cuerpoModal.innerHTML = html;
                modal.show();
            } catch (error) {
                cuerpoModal.innerHTML = '<p class="text-danger text-center">Error al cargar la orden.</p>';
                console.error(error);
            }
        });
    });

    modalElement.addEventListener('hidden.bs.modal', () => {
        cuerpoModal.innerHTML = '';
    });
    
    // CAMBIOS DE ESTADO

    document.querySelectorAll('.pasar-en-proceso-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoAEnProceso(ordenId);
        });
    });

    document.querySelectorAll('.pasar-lista-para-retirar-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoAListaParaRetirar(ordenId);
        });
    });

    document.querySelectorAll('.pasar-retirada-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            const ordenId = boton.dataset.idorden;
            const resta = boton.dataset.resta;

            if (resta != 0) {
                const confirmar = confirm("¡ADVERTENCIA! El producto no fue abonado por completo.\n\n¿Desea continuar de todos modos?");
                if (!confirmar) {
                    return;
                }
            }
            document.getElementById('spinner-overlay').style.display = 'flex';
            cambiarEstadoARetirada(ordenId);
        });
    });

    document.querySelectorAll('.pasar-corregido-btn').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
            cambiarEstadoaSinHacer(ordenId);
        });
    });

    document.querySelectorAll('.btn-lupa').forEach(boton => {
        boton.addEventListener('click', () => {
            document.getElementById('spinner-overlay').style.display = 'flex';
            const ordenId = boton.dataset.idorden;
        });
    });

    function cambiarEstadoACorregir(ordenId) {
        fetch(`/api/orden/cambiar-a-corregir/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoAEnProceso(ordenId) {
        fetch(`/api/orden/cambiar-a-en-proceso/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoAListaParaRetirar(ordenId) {
        fetch(`/api/orden/cambiar-a-lista-para-retirar/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoARetirada(ordenId) {
        fetch(`/api/orden/cambiar-a-retirada/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }
    function cambiarEstadoaSinHacer(ordenId) {
        fetch(`/api/orden/cambiar-a-sin-hacer/${ordenId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                console.error('Error al cambiar estado');
            }
        })
        .catch(error => console.error('Error de red:', error));
    }

    const select = document.getElementById('selector-producto');
      const form = document.getElementById('formulario-selector-producto');

      select.addEventListener('change', () => {
        document.getElementById('spinner-overlay').style.display = 'flex';
        form.submit();
      });
});

window.addEventListener('load', () => {
  document.getElementById('spinner-overlay').style.display = 'none';
});
