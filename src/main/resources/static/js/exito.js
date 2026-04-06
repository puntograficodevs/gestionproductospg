$(document).ready(function() {
    inicializarTooltipImprimir();
    escucharClickEnBotonImprimir();
});

function inicializarTooltipImprimir() {
    $('body').tooltip({
        selector: '[data-bs-toggle="tooltip"]'
    });
}
function escucharClickEnBotonImprimir() {
    $(document).on('click', '#btnImprimir', function() {
        window.print();
    });
}