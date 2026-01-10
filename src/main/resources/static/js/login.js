window.addEventListener('DOMContentLoaded', () => {
    const errorDiv = document.getElementById('error-message');
    if (errorDiv) {
        setTimeout(() => {
            errorDiv.classList.remove('animate__fadeInUp');
            errorDiv.classList.add('animate__fadeOutDown');
            setTimeout(() => errorDiv.remove(), 1000); // Lo borra del DOM después de la animación
        }, 3000);
    }
});
