import { el, toast, fmtDateTime, mountReveal } from "/js/ui.js";
import * as api from "/js/api.js";

export function BarberCard(barber) {
  return el("div", { class: "barber-card" }, [
    el("div", { class: "barber-name" }, barber.nome),
    el("div", { class: "barber-specialty" }, barber.especialidade || "Barbeiro"),
  ]);
}


export function BarberPanelPage(ctx) {
  const wrap = document.createElement("div");
  wrap.append(
    el("h1", { class: "h1 reveal" }, "Painel do Barbeiro"),
    el("p", { class: "sub reveal" }, "Visualize os agendamentos e marque como concluído.")
  );

  const db = api.ensureDB();
  const services = Object.fromEntries(db.services.map((s) => [s.id, s]));

  const list = el("div", { class: "section" });
  wrap.append(list);

  const user = ctx?.user || api.getCurrentUser?.() || {};

  async function render() {
    list.innerHTML = "";

    await api.refreshAppointmentsForUser(user);
    const appts = api.listAppointmentsForUser(user).filter((a) => a.status !== "Cancelado");

    if (appts.length === 0) {
      list.append(
        el("div", { class: "card reveal" }, [
          el("div", { class: "card__title" }, "Sem horários hoje."),
          el("div", { class: "card__desc" }, "Quando um cliente agendar, aparece aqui.")
        ])
      );
      mountReveal(wrap);
      return;
    }

    for (const a of appts) {
      const sNome = a.servicoNome || "Serviço";
      const cNome = a.clienteNome || "Cliente";

      const card = el("div", { class: "card reveal" });

      card.append(
        el("div", { class: "card__row" }, [
          el("div", {}, [
            el(
              "div",
              { class: "card__title" },
              `${sNome} • ${fmtDateTime(a.dataHora)}`
            ),
            el(
              "div",
              { class: "card__desc" },
              `Cliente: ${cNome} • Status: ${a.status}`
            )
          ]),
          el("span", { class: "badge" }, a.status)
        ]),
        el("div", { style: "margin-top:12px; display:flex; gap:10px; flex-wrap:wrap" }, [
          a.status === "Agendado"
            ? el(
              "button",
              {
                class: "btn btn--primary",
                type: "button",
                onClick: async () => {
                  await api.completeAppointment(a.id);
                  toast("Concluído ✅");
                  render();
                }
              },
              "Concluir"
            )
            : el("button", { class: "btn", type: "button", disabled: true }, "Concluído")
        ])
      );

      list.append(card);
    }

    mountReveal(wrap);
  }

  render();
  mountReveal(wrap);
  return wrap;
}
