document.addEventListener('DOMContentLoaded', () => {
    const botonesVer = document.querySelectorAll('.ver-btn');
    const botonesMarcarFacturacion = document.querySelectorAll('.marcar-facturacion-btn');
    const modalElement = document.getElementById('ordenModal');
    const cuerpoModal = document.getElementById("modalBody");
    const modal = new bootstrap.Modal(modalElement);

    botonesVer.forEach(boton => {
        boton.addEventListener('click', async () => {
            const idOrden = boton.dataset.idorden;

            cuerpoModal.innerHTML = '<p class="text-center">Cargando los datos de la orden...</p>';
            modal.show();

            try {
                const html = await fetch(
                    `/modal/mostrar-odt-producto/${idOrden}`
                ).then(r => r.text());

                cuerpoModal.innerHTML = html;

            } catch (error) {
                cuerpoModal.innerHTML =
                    '<p class="text-danger text-center">Error al cargar la orden.</p>';
                console.error(error);
            }
        });
    });

    botonesMarcarFacturacion.forEach(boton => {
        boton.addEventListener('click', async () => {
            const idOrden = boton.dataset.idorden;

            try {
                const response = await fetch(
                    `/api/orden/marcar-facturada/${idOrden}`,
                    { method: 'POST' }
                );

                if (response.ok) {
                    alert(`La orden N°${idOrden} se marcó como facturada correctamente.`);
                    window.location.reload()
                } else {
                    alert("¡Algo salió mal! Pasarle el ID de la orden a Ben y él la marca facturada desde base de datos.");
                    console.error('Status:', response.status);
                }
            } catch (e) {
                alert("Hubo un error, pasarle esta captura a Ben: " + e);
            }

        });
    });

    modalElement.addEventListener('hidden.bs.modal', () => {
        cuerpoModal.innerHTML = '';
    });
});

window.addEventListener('load', () => {
  document.getElementById('spinner-overlay').style.display = 'none';
});
