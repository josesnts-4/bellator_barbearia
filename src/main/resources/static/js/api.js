const BASE_URL = "http://localhost:8080";

// ================= TOKEN =================
function getToken() {
  return localStorage.getItem("token");
}

// ================= REQUEST =================
async function request(url, options = {}) {
  const token = getToken();

  const res = await fetch(BASE_URL + url, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` })
    }
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text);
  }

  return res.json();
}

// ================= AUTH =================
export async function login(email, senha) {
  const data = await request("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, senha })
  });

  localStorage.setItem("token", data.token);
  if (data.user) {
    localStorage.setItem("user", JSON.stringify(data.user));
  }
  return data;
}

export async function register(user) {
  return request("/auth/register", {
    method: "POST",
    body: JSON.stringify(user)
  });
}

export function logout() {
  localStorage.removeItem("token");
}

export function me() {
  const token = getToken();
  if (!token) return null;
  const user = localStorage.getItem("user");
  if (user) return JSON.parse(user);
  return { token };
}

// ================= SERVICES =================
export function listServices() {
  return request("/servicos");
}

// ================= BARBEIROS =================
export function listBarbers() {
  return request("/usuarios/barbeiros");
}

// ================= AGENDAMENTOS =================
export function createAppointment(data) {
  return request("/agendamentos", {
    method: "POST",
    body: JSON.stringify(data)
  });
}

export function myAppointments() {
  return request("/agendamentos/me");
}

export function cancelAppointment(id) {
  return request(`/agendamentos/${id}/cancelar`, {
    method: "PUT"
  });
}