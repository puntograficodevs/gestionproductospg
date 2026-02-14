function verDetalle(id) {
    const modalBody = document.getElementById('modalBody');
    
    // 1. Mostrar spinner de carga
    modalBody.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status"></div>
            <p class="mt-2">Cargando orden...</p>
        </div>`;
    
    // 2. Inicializar y mostrar el modal de Bootstrap
    const modalElement = document.getElementById('ordenModal');
    const myModal = new bootstrap.Modal(modalElement);
    myModal.show();

    // 3. Fetch del fragmento (la ruta es relativa al dominio)
    fetch(`/ordenes/detalle-fragmento/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('No se pudo encontrar la orden');
            return response.text();
        })
        .then(html => {
            modalBody.innerHTML = html;
        })
        .catch(error => {
            modalBody.innerHTML = `<div class="alert alert-danger m-3">Error: ${error.message}</div>`;
        });
}