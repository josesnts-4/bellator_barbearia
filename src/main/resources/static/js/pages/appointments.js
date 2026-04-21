import { el, setActiveNav, fmtDateTime, toast, modal, mountReveal } from "../ui.js";
import * as api from "../api.js";

export function AppointmentsPage(ctx) {
  setActiveNav("appointments");
  const { user } = ctx;

  const wrap = el("div", {});
  wrap.append(
    el("h1", { class: "h1 reveal" }, user.role === "cliente" ? "Meus Agendamentos" : (user.role === "admin" ? "Agendamentos (Admin)" : "Agenda do Dia")),
    el("p", { class: "sub reveal" }, user.role === "cliente"
      ? "Acompanhe seus horários e cancele quando necessário."
      : "Visualize, conclua e gerencie agendamentos.")
  );

  const list = el("div", { class: "section" });
  wrap.append(list);

  const btnNew = user.role === "cliente"
    ? el("button", { class: "btn btn--primary reveal", type: "button", onClick: () => location.hash = "#/book/service" }, "Novo Agendamento")
    : null;

  if (btnNew) wrap.append(btnNew);

  async function render() {
    list.innerHTML = "";
    await api.refreshAppointmentsForUser(user);
    const appts = api.listAppointmentsForUser(user);

    if (appts.length === 0) {
      list.append(el("div", { class: "card reveal" }, [
        el("div", { class: "card__title" }, "Nenhum agendamento por aqui."),
        el("div", { class: "card__desc" }, "Toque em “Novo Agendamento” para reservar seu horário.")
      ]));
      mountReveal(wrap);
      return;
    }

    for (const a of appts) {
      const status = a.status;
      const isCanceled = status === "Cancelado";
      const isDone = status === "Concluído";

      const c = el("div", { class: `card reveal ${isCanceled ? "card--ghost" : ""}` });
      c.append(
        el("div", { class: "card__row" }, [
          el("div", {}, [
            el("div", { class: "card__title" }, a.servicoNome || "Serviço"),
            el("div", { class: "card__desc" }, `${a.barbeiroNome || "Barbeiro"}${a.clienteNome && user.role !== "cliente" ? ` • ${a.clienteNome}` : ""} • ${fmtDateTime(a.dataHora)}`)
          ]),
          el("span", { class: "badge" }, status)
        ])
      );

      const actions = el("div", { style: "margin-top:12px;display:flex;gap:10px;flex-wrap:wrap" });
      if (user.role === "cliente" && status === "Agendado") {
        actions.append(el("button", { class: "btn btn--danger", type: "button", onClick: () => confirmCancel(a.id) }, "Cancelar"));
        actions.append(el("button", {
          class: "btn btn--outline", type: "button", onClick: () => {
            ctx.wizard.serviceId = a.servicoId;
            ctx.wizard.barberId = a.barbeiroId;
            ctx.wizard.rescheduleId = a.id;
            location.hash = "#/book/datetime";
          }
        }, "Reagendar"));
      }
      if (user.role === "barbeiro" && status === "Agendado") {
        actions.append(el("button", {
          class: "btn btn--primary", type: "button", onClick: async () => {
            await api.completeAppointment(a.id);
            toast("Atendimento marcado como concluído ✅");
            render();
          }
        }, "Concluir"));
      }
      if (user.role === "cliente" && status === "Concluído") {
        actions.append(el("button", { class: "btn", type: "button", onClick: () => openReviewModal() }, "Avaliar"));
      }
      if (user.role === "admin") {
        actions.append(el("button", { class: "btn", type: "button", onClick: () => toast("Dica: no painel admin você vê relatórios.") }, "Info"));
      }
      if (actions.children.length) c.append(actions);

      list.append(c);
    }
    mountReveal(wrap);
  }

  function confirmCancel(id) {
    modal({
      title: "Cancelar agendamento?",
      body: "Você tem certeza que deseja cancelar? Cancelamentos devem ser feitos com pelo menos 2 horas de antecedência.",
      actions: [
        { label: "Voltar", variant: "btn--outline", onClick: async () => { } },
        {
          label: "Cancelar Agendamento", variant: "btn--danger", onClick: async () => {
            try {
              await api.cancelAppointment(id);
              toast("Agendamento cancelado.");
              render();
            } catch (e) {
              toast(e.message || "Não foi possível cancelar.");
            }
          }
        }
      ]
    });
  }
  //avaliação

  function openReviewModal() {
    let selectedStars = 5;
    const starRow = el("div", { class: "chips", style: "margin-bottom:14px; justify-content:center" });

    function renderStars() {
      starRow.innerHTML = "";
      for (let i = 1; i <= 5; i++) {
        const isSel = i <= selectedStars;
        const star = el("button", {
          class: `icon-btn ${isSel ? "btn--primary" : ""}`,
          style: "width:40px; height:40px; border-radius:10px",
          onClick: () => {
            selectedStars = i;
            renderStars();
          }
        }, [el("i", { "data-lucide": "star", style: isSel ? "fill:currentColor" : "" })]);
        starRow.append(star);
      }
      window.lucide?.createIcons?.();
    }

    const commentInput = el("textarea", {
      class: "input",
      style: "width:100%; height:80px; border-radius:14px; padding:10px; margin-bottom:10px",
      placeholder: "Deixe um comentário (opcional)..."
    });

    modal({
      title: "Avaliar Barbearia",
      body: "Sua opinião é muito importante para nós!",
      actions: [
        { label: "Agora não", variant: "btn--outline" },
        {
          label: "Enviar Avaliação",
          variant: "btn--primary",
          onClick: async () => {
            try {
              await api.submitReview({
                nota: selectedStars,
                comentario: commentInput.value
              });
              toast("Obrigado pela avaliação! ⭐");
              render();
            } catch (e) {
              toast("Erro ao enviar avaliação.");
            }
          }
        }
      ]
    });

    // Inserir estrelas e input antes dos botões do modal
    const modalDiv = document.querySelector(".modal");
    if (modalDiv) {
      const btnRow = modalDiv.querySelector(".btn-row");
      modalDiv.insertBefore(starRow, btnRow);
      modalDiv.insertBefore(commentInput, btnRow);
      renderStars();
    }
  }

  render();
  mountReveal(wrap);
  return wrap;
}
