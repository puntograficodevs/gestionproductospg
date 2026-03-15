window.addEventListener('DOMContentLoaded', () => {
    const existeErrorEnLogin = document.getElementById('error-message');
    if (existeErrorEnLogin) {
        setTimeout(() => {
            existeErrorEnLogin.classList.remove('animate__fadeInUp');
            existeErrorEnLogin.classList.add('animate__fadeOutDown');
            setTimeout(() => existeErrorEnLogin.remove(), 1000);
        }, 3000);
    }
});
