import * as api from "../api.js";
import { el, toast } from "../ui.js";

function toLocalDateAndHorario(iso) {
  const d = new Date(iso);
  if (Number.isNaN(d.getTime())) throw new Error("Data/horário inválidos.");
  const data = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;
  const horario = `${String(d.getHours()).padStart(2, "0")}:${String(d.getMinutes()).padStart(2, "0")}:00`;
  return { data, horario };
}

export async function BookConfirmPage(ctx) {
  const { user } = ctx;
  const wrap = el("div", {});

  const db = api.ensureDB();
  const services = Object.fromEntries(db.services.map((s) => [s.id, s]));
  const barbers = Object.fromEntries(db.barbers.map((b) => [b.id, b]));
  const service = services[ctx.wizard.serviceId];
  const barber = barbers[ctx.wizard.barberId];
  const dt = ctx.wizard.datetimeISO ? new Date(ctx.wizard.datetimeISO) : null;

  let errMsg = null;
  if (!ctx.wizard.__created) {
    try {
      if (!user?.id) throw new Error("Sessão inválida. Entre novamente.");
      if (ctx.wizard.serviceId == null || ctx.wizard.barberId == null || !ctx.wizard.datetimeISO) {
        throw new Error("Dados do agendamento incompletos. Volte e conclua as etapas.");
      }
      const { data, horario } = toLocalDateAndHorario(ctx.wizard.datetimeISO);
      
      if (ctx.wizard.rescheduleId) {
        await api.rescheduleAppointment(ctx.wizard.rescheduleId, { data, horario });
      } else {
        await api.createAppointment({
          servicoId: Number(ctx.wizard.serviceId),
          barbeiroId: Number(ctx.wizard.barberId),
          data,
          horario
        });
      }
      ctx.wizard.__created = true;
    } catch (e) {
      errMsg = e.message || "Não foi possível finalizar.";
      toast(errMsg);
    }
  }

  if (errMsg) {
    wrap.append(
      el("div", { class: "section" }, [
        el("div", { class: "card reveal" }, [
          el("h1", { class: "h1" }, "Não foi possível agendar"),
          el("p", { class: "sub" }, errMsg),
          el("div", { style: "margin-top:16px;display:flex;flex-direction:column;gap:10px" }, [
            el(
              "button",
              { class: "btn btn--primary", type: "button", onClick: () => (location.hash = "#/book/datetime") },
              "Voltar à data e horário"
            ),
            el("button", { class: "btn btn--outline", type: "button", onClick: () => (location.hash = "#/home") }, "Início")
          ])
        ])
      ])
    );
    mountReveal(wrap);
    return wrap;
  }

  wrap.append(
    el("div", { class: "section" }, [
      el("div", { class: "card reveal" }, [
        el("div", { style: "display:flex; align-items:center; justify-content:center; margin:8px 0 6px" }, [
          el("i", { "data-lucide": "check" })
        ]),
        el("h1", { class: "h1", style: "text-align:center;margin-top:0" }, ctx.wizard.rescheduleId ? "Agendamento Reagendado!" : "Agendamento Confirmado!"),
        el("p", { class: "sub", style: "text-align:center;margin-top:-6px" }, ctx.wizard.rescheduleId ? "Seu novo horário foi reservado com sucesso" : "Seu horário foi reservado com sucesso"),
        el("div", { style: "display:grid; gap:10px; margin-top:12px" }, [
          kv("scissors", "Serviço", service?.nome || "-", money(service?.preco || 0)),
          kv("user", "Barbeiro", barber?.nome || "-", barber?.especialidade || ""),
          kv("calendar", "Data", dt && !Number.isNaN(dt.getTime()) ? fmtDate(dt.toISOString()) : "-", ""),
          kv("clock", "Horário", dt && !Number.isNaN(dt.getTime()) ? fmtTime(dt.toISOString()) : "-", "")
        ])
      ])
    ])
  );

  wrap.append(
    el("div", { class: "card reveal" }, [
      el("a", { 
        href: "https://maps.google.com/?q=-7.8967,-34.8252", 
        target: "_blank",
        style: "text-decoration: none; color: inherit; display: flex; align-items: center; gap: 12px; cursor: pointer;" 
      }, [
        el("i", { "data-lucide": "map-pin", style: "color: var(--primary)" }),
        el("div", {}, [
          el("small", {}, "Localização (Abrir no GPS)"),
          el("br"),
          el("strong", {}, "Bellator Barbearia"),
          el("br"),
          el("small", {}, "R. Aristóteles Paes de Azevedo, 145 - Pau Amarelo, Paulista - PE")
        ]),
        el("div", { class: "right" }, "")
      ]),
      el("div", { class: "hr" }),
      el("div", { class: "card card--ghost", style: "border-radius:16px;padding:12px" }, [
        el("div", { style: "display:flex; gap:10px; align-items:flex-start" }, [
          el("i", { "data-lucide": "shield" }),
          el("div", {}, [
            el("div", { style: "font-weight:700; font-size:12px" }, "Importante:"),
            el("div", { class: "helper" }, "Chegue com 5 minutos de antecedência. Cancelamentos devem ser feitos com pelo menos 2 horas de antecedência.")
          ])
        ])
      ])
    ])
  );

  wrap.append(
    el("div", { class: "section reveal" }, [
      el("button", { class: "btn btn--primary", type: "button", onClick: () => (location.hash = "#/appointments") }, "Ver meus agendamentos"),
      el("div", { style: "height:10px" }),
      el("button", { class: "btn btn--outline", type: "button", onClick: () => (location.hash = "#/home") }, "Voltar ao início")
    ])
  );

  mountReveal(wrap);
  return wrap;
}

function kv(icon, label, left, right) {
  return el("div", { class: "kv" }, [
    el("i", { "data-lucide": mapIcon(icon) }),
    el("div", {}, [el("small", {}, label), el("br"), el("strong", {}, left)]),
    el("div", { class: "right" }, right || "")
  ]);
}

function mapIcon(name) {
  const m = { scissors: "scissors", user: "user", calendar: "calendar", clock: "clock", star: "star", pin: "map-pin", check: "check" };
  return m[name] || name || "circle";
}

function money(valor) {
  return valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function fmtDate(iso) {
  const d = new Date(iso);
  return d.toLocaleDateString("pt-BR");
}

function fmtTime(iso) {
  const d = new Date(iso);
  return d.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

function mountReveal(wrap) {
  setTimeout(() => {
    wrap.querySelectorAll(".reveal").forEach((el) => el.classList.add("is-in"));
  }, 10);
}
