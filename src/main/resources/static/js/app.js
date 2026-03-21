import * as api from "./api.js";
import { routes } from "./router.js";
import { toast } from "./ui.js";

console.log("JS CARREGOU");

const view = document.getElementById("view");
const backBtn = document.getElementById("backBtn");
const menuBtn = document.getElementById("menuBtn");
const bottomNav = document.getElementById("bottomNav");
const whatsBtn = document.getElementById("whatsBtn");

let __isRendering = false;
let __pendingRender = false;

// WhatsApp
const WHATS_NUMBER = "5581994328093";
const defaultMsg = encodeURIComponent("Olá! Quero agendar um horário na Bellator Barbearia. 👊");
whatsBtn.href = `https://wa.me/${WHATS_NUMBER}?text=${defaultMsg}`;

const ctx = {
  wizard: { serviceId: null, barberId: null, datetimeISO: null, __created: false }
};

function getPath() {
  const raw = location.hash.replace(/^#/, "");
  return raw || "/auth";
}

function setTopbar({ showBack, showMenu }) {
  backBtn.style.visibility = showBack ? "visible" : "hidden";
  menuBtn.style.visibility = showMenu ? "visible" : "hidden";
}

function isAuthenticated() {
  return !!localStorage.getItem("token");
}

function guard(path) {
  const route = routes[path];
  if (!route) return { redirect: "/home" };

  const me = api.me();
  const isAuthed = !!me || isAuthenticated();

  if (route.public) return { ok: true, me };
  if (!isAuthed) return { redirect: "/auth" };

  if (route.role && me?.user?.role !== route.role) {
    return { redirect: "/home" };
  }

  return { ok: true, me };
}

function resetWizardIfLeavingBook(path) {
  if (!path.startsWith("/book/")) {
    ctx.wizard = { serviceId: null, barberId: null, datetimeISO: null, __created: false };
  }
}

async function render() {
  const path = getPath();
  const route = routes[path];

  if (!route) {
    location.hash = "#/home";
    return;
  }

  const g = guard(path);
  if (g.redirect) {
    location.hash = "#" + g.redirect;
    return;
  }

  const me = g.me || api.me();
  const navOn = !!route.nav && !!me;

  bottomNav.style.display = navOn ? "flex" : "none";
  whatsBtn.style.display = me ? "grid" : "none";

  setTopbar({
    showBack: !route.nav && path !== "/auth" && path !== "/home",
    showMenu: me && route.nav,
  });

  if (__isRendering) {
    __pendingRender = true;
    return;
  }
  __isRendering = true;

  const swap = () => {
    view.innerHTML = "";
    const pageNode = route.page({ ...ctx, ...(me || {}) });
    view.appendChild(pageNode);

    try {
      window.lucide?.createIcons?.();
    } catch (e) {}

    requestAnimationFrame(() => {
      view.classList.remove("is-fading");
      __isRendering = false;
      if (__pendingRender) {
        __pendingRender = false;
        render();
      }
    });
  };

  view.classList.add("is-fading");
  setTimeout(swap, 170);

  if (route.nav) {
    const key = path.replace("/", "");
    for (const a of bottomNav.querySelectorAll("[data-route]")) {
      a.classList.toggle("is-active", a.getAttribute("data-route") === key);
    }
  }

  resetWizardIfLeavingBook(path);
}

window.addEventListener("hashchange", render);

window.addEventListener("load", async () => {
  try {
    await api.bootstrap();
  } catch (e) {}

  const me = api.me();

  if (!location.hash) {
    location.hash = me ? "#/home" : "#/auth";
  } else {
    render();
  }
});

backBtn.addEventListener("click", () => {
  history.length > 1 ? history.back() : (location.hash = "#/home");
});

menuBtn.addEventListener("click", () => {
  const me = api.me();
  if (!me) return;
  openMenu(me.user);
});

function openMenu(user) {
  let backdrop = document.querySelector(".sheet-backdrop");
  let sheet = document.querySelector(".sheet");

  if (!backdrop) {
    backdrop = document.createElement("div");
    backdrop.className = "sheet-backdrop";
    document.body.appendChild(backdrop);
  }

  if (!sheet) {
    sheet = document.createElement("aside");
    sheet.className = "sheet";
    document.body.appendChild(sheet);
  }

  const roleLabel =
    user.role === "admin"
      ? "ADMIN"
      : user.role === "barbeiro"
      ? "BARBEIRO"
      : "CLIENTE";

  sheet.innerHTML = `
    <div class="sheet__top">
      <div>
        <div class="sheet__title">MENU</div>
        <div class="sheet__sub">${user.nome || "Usuário"} • ${roleLabel}</div>
      </div>
      <button class="icon-btn" id="sheetClose" aria-label="Fechar"><i data-lucide="x"></i></button>
    </div>
    <div class="sheet__list">
      <button class="sheet__item" data-go="#/home"><i data-lucide="home"></i><span>Início</span></button>
      <button class="sheet__item" data-go="#/book/service"><i data-lucide="scissors"></i><span>Agendar</span></button>
      <button class="sheet__item" data-go="#/appointments"><i data-lucide="calendar"></i><span>Meus agendamentos</span></button>
      <button class="sheet__item" data-go="#/profile"><i data-lucide="user"></i><span>Perfil</span></button>
      ${user.role === "admin" ? `<button class="sheet__item" data-go="#/admin"><i data-lucide="layout-dashboard"></i><span>Painel Admin</span></button>` : ""}
      ${user.role === "barbeiro" ? `<button class="sheet__item" data-go="#/barber"><i data-lucide="calendar-check"></i><span>Painel Barbeiro</span></button>` : ""}
    </div>
    <div class="sheet__spacer"></div>
    <button class="sheet__item" id="sheetLogout"><i data-lucide="log-out"></i><span>Sair</span></button>
  `;

  function close() {
    sheet.classList.remove("is-open");
    backdrop.classList.remove("is-open");
    document.removeEventListener("keydown", onKey);
  }

  function onKey(e) {
    if (e.key === "Escape") close();
  }

  backdrop.onclick = close;
  sheet.querySelector("#sheetClose").onclick = close;

  sheet.querySelectorAll("[data-go]").forEach((btn) => {
    btn.onclick = () => {
      location.hash = btn.getAttribute("data-go");
      close();
    };
  });

  sheet.querySelector("#sheetLogout").onclick = () => {
    api.logout();
    location.hash = "#/auth";
    close();
  };

  backdrop.classList.add("is-open");
  sheet.classList.add("is-open");
  document.addEventListener("keydown", onKey);

  try {
    window.lucide?.createIcons?.();
  } catch (e) {}
}

async function handleLogin() {
  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  try {
    await api.login(email, senha);
    location.hash = "#/home";
  } catch (e) {
    alert("Erro no login");
  }
}