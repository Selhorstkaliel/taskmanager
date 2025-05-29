import { API } from 'd:/facudade/taskmanager/src/main/resources/static/js/api.js';

const username = localStorage.getItem("username");
if (!username) window.location.href = "/view/login.html";

window.loadTasks = async function () {
    const status = document.getElementById("filterStatus").value;
    const q = document.getElementById("searchText").value;

    const tasks = await API.get("tasks", { username, status, q });
    renderTasks(tasks);

    const urgent = await API.get("tasks/urgent", { username });
    document.getElementById("notifBadge").classList.toggle("hidden", urgent.length === 0);
};

window.createTask = async function () {
    const title = document.getElementById("taskTitle").value;
    const description = document.getElementById("taskDesc").value;
    const dueDate = document.getElementById("taskDue").value;
    const priority = document.getElementById("taskPriority").value;

    await API.post(`tasks?username=${username}`, {
        title, description, dueDate, priority, status: "pendente"
    });

    loadTasks();
};

window.deleteTask = async function (id) {
    await API.del(`tasks/${id}?username=${username}`);
    loadTasks();
};

window.completeTask = async function (id) {
    await API.patch(`tasks/${id}/complete?username=${username}`);
    loadTasks();
};

window.exportTasks = async function () {
    const data = await API.get("tasks/export", { username });
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = `tarefas-${username}.json`;
    link.click();
};

window.importTasks = async function (e) {
    const file = e.target.files[0];
    if (!file) return;

    const text = await file.text();
    const tasks = JSON.parse(text);
    await API.post(`tasks/import?username=${username}`, tasks);
    loadTasks();
};

function renderTasks(tasks) {
    const list = document.getElementById("taskList");
    list.innerHTML = "";
    tasks.forEach(t => {
        const div = document.createElement("div");
        div.innerHTML = `
            <b>${t.title}</b> (${t.priority})<br>
            ${t.description}<br>
            <small>Para: ${t.dueDate.replace("T", " ").slice(0,16)} | Status: ${t.status}</small><br>
            <button onclick="completeTask('${t.id}')">✔️</button>
            <button onclick="deleteTask('${t.id}')">🗑️</button>
        `;
        list.appendChild(div);
    });
}

window.onload = loadTasks;
