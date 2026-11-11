document.addEventListener("DOMContentLoaded", function() {
    const togglePassword = document.querySelector("#togglePassword");
    const passwordInput = document.querySelector("#password");
    const icon = togglePassword.querySelector("i");

    togglePassword.addEventListener("click", function () {
        // Alternar el tipo de input
        const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);

        // Alternar el ícono de Bootstrap
        icon.classList.toggle("bi-eye-fill");
        icon.classList.toggle("bi-eye-slash-fill");
    });
});