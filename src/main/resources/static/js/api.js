export const API = {
    async post(endpoint, data) {
        const res = await fetch(`/api/${endpoint}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async get(endpoint, params = {}) {
        const url = new URL(`/api/${endpoint}`, window.location.origin);
        Object.keys(params).forEach(k => url.searchParams.append(k, params[k]));
        const res = await fetch(url);
        return res.json();
    },

    async put(endpoint, data) {
        const res = await fetch(`/api/${endpoint}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
        return res.json();
    },

    async del(endpoint) {
        const res = await fetch(`/api/${endpoint}`, { method: "DELETE" });
        return res.json();
    },

    async patch(endpoint) {
        const res = await fetch(`/api/${endpoint}`, { method: "PATCH" });
        return res.json();
    }
};
