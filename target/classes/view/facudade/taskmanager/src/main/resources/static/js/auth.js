import { API } from 'd:/facudade/taskmanager/src/main/resources/static/js/api.js';

window.login = async function () {
    const login = document.getElementById("loginInput").value;
    const password = document.getElementById("loginPassword").value;

    const res = await API.post("auth/login", { login, password });
    if (res.success) {
        localStorage.setItem("username", res.username);
        localStorage.setItem("isAdmin", res.isAdmin);
        window.location.href = "/view/dashboard.html";
    } else {
        alert("Login inválido");
    }
};

window.register = async function () {
    const name = document.getElementById("regName").value;
    const username = document.getElementById("regUsername").value;
    const email = document.getElementById("regEmail").value;
    const password = document.getElementById("regPassword").value;

    const res = await API.post("auth/register", {
        name, username, email, password
    });

    if (res.success) {
        alert("Cadastro realizado. Faça login.");
    } else {
        alert("Erro no cadastro.");
    }
};
