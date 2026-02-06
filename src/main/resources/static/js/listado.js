document.addEventListener('DOMContentLoaded', () => {
    const botonesVer = document.querySelectorAll('.ver-btn');
    const modalElement = document.getElementById('ordenModal');
    const cuerpoModal = document.getElementById("modalBody");
    const modalFooter = document.getElementById("modalFooter");

    const modal = new bootstrap.Modal(modalElement);

    botonesVer.forEach(boton => {
        boton.addEventListener('click', async () => {
            const idOrden = boton.dataset.idorden;
            const tipoProducto = boton.dataset.tipoproducto;
            const tieneCorreccion = boton.dataset.correccion === 'true';
            const rolEmpleado = boton.dataset.rolempleado;

            cuerpoModal.innerHTML = '<p class="text-center">Cargando los datos de la orden...</p>';
            modalFooter.innerHTML = '';
            modal.show();

            try {
                const html = await fetch(
                    `/modal/mostrar-odt-producto/${idOrden}`
                ).then(r => r.text());

                cuerpoModal.innerHTML = html;

                if (tieneCorreccion && rolEmpleado != 3) {
                    const btn = document.createElement('button');
                    btn.className = 'btn btn-primary';
                    btn.textContent = 'Corregir';

                    btn.addEventListener('click', () => {
                        renderizarEdicionCorrespondiente(idOrden, tipoProducto);
                    });

                    modalFooter.appendChild(btn);
                }

            } catch (error) {
                cuerpoModal.innerHTML =
                    '<p class="text-danger text-center">Error al cargar la orden.</p>';
                console.error(error);
            }
        });
    });


    modalElement.addEventListener('hidden.bs.modal', () => {
        cuerpoModal.innerHTML = '';
    });

    function renderizarEdicionCorrespondiente(idOrden, tipoProducto) {
    switch (tipoProducto) {
      case "agenda":
        window.location.href = `/crear-odts/crear-odt-agenda/${idOrden}`;
        break;
      case "anotador":
        window.location.href = `/crear-odts/crear-odt-anotador/${idOrden}`;
        break;
      case "carpeta con solapas":
        window.location.href = `/crear-odts/crear-odt-carpeta-solapa/${idOrden}`;
        break;
      case "catálogo":
        window.location.href = `/crear-odts/crear-odt-catalogo/${idOrden}`;
        break;
      case "cierra bolsas":
        window.location.href = `/crear-odts/crear-odt-cierra-bolsas/${idOrden}`;
        break;
      case "combo":
        window.location.href = `/crear-odts/crear-odt-combo/${idOrden}`;
        break;
      case "cuaderno anillado":
        window.location.href = `/crear-odts/crear-odt-cuaderno-anillado/${idOrden}`;
        break;
      case "entrada":
        window.location.href = `/crear-odts/crear-odt-entrada/${idOrden}`;
        break;
      case "etiqueta":
        window.location.href = `/crear-odts/crear-odt-etiqueta/${idOrden}`;
        break;
      case "flybanner":
        window.location.href = `/crear-odts/crear-odt-flybanner/${idOrden}`;
        break;
      case "folleto":
        window.location.href = `/crear-odts/crear-odt-folleto/${idOrden}`;
        break;
      case "goma de polimero":
        alert("se hizo click acá");
        window.location.href = `/crear-odts/crear-odt-goma-polimero/${idOrden}`;
        break;
      case "hojas membretadas":
        window.location.href = `/crear-odts/crear-odt-hojas-membreteadas/${idOrden}`;
        break;
      case "impresion":
        window.location.href = `/crear-odts/crear-odt-impresion/${idOrden}`;
        break;
      case "lona común":
        window.location.href = `/crear-odts/crear-odt-lona-comun/${idOrden}`;
        break;
      case "lona publicitaria":
        window.location.href = `/crear-odts/crear-odt-lona-publicitaria/${idOrden}`;
        break;
      case "sin categoría":
        window.location.href = `/crear-odts/crear-odt-otro/${idOrden}`;
        break;
      case "rifa o bono":
        window.location.href = `/crear-odts/crear-odt-rifas-bonos-contribucion/${idOrden}`;
        break;
      case "rotulación":
        window.location.href = `/crear-odts/crear-odt-rotulacion/${idOrden}`;
        break;
      case "sello automático":
        window.location.href = `/crear-odts/crear-odt-sello-automatico/${idOrden}`;
        break;
      case "sello automático escolar":
        window.location.href = `/crear-odts/crear-odt-sello-automatico-escolar/${idOrden}`;
        break;
      case "sello de madera":
        window.location.href = `/crear-odts/crear-odt-sello-madera/${idOrden}`;
        break;
      case "sobre":
        window.location.href = `/crear-odts/crear-odt-sobre/${idOrden}`;
        break;
      case "sticker":
        window.location.href = `/crear-odts/crear-odt-sticker/${idOrden}`;
        break;
      case "sublimación":
        window.location.href = `/crear-odts/crear-odt-sublimacion/${idOrden}`;
        break;
      case "talonario":
        window.location.href = `/crear-odts/crear-odt-talonario/${idOrden}`;
        break;
      case "tarjeta":
        window.location.href = `/crear-odts/crear-odt-tarjeta/${idOrden}`;
        break;
      case "turnero":
        window.location.href = `/crear-odts/crear-odt-turnero/${idOrden}`;
        break;
      case "vinilo":
        window.location.href = `/crear-odts/crear-odt-vinilo/${idOrden}`;
        break;
      case "vinilo de corte":
        window.location.href = `/crear-odts/crear-odt-vinilo-de-corte/${idOrden}`;
        break;
      case "vinilo con plástico corrugado":
        window.location.href = `/crear-odts/crear-odt-vinilo-plastico-corrugado/${idOrden}`;
        break;
      case "voucher":
        window.location.href = `/crear-odts/crear-odt-voucher/${idOrden}`;
        break;
      default:
        console.warn("Tipo de producto no manejado:", tipoProducto);
    }
  }
    
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
