import * as api from "../api.js";
import { el, money, mountReveal, toast } from "../ui.js";

export async function BookServicePage(ctx){
  const wrap = el("div", {});
  wrap.append(
    el("h1",{class:"h1 reveal"},"Escolha o Serviço"),
    el("p",{class:"sub reveal"},"Selecione o que deseja fazer")
  );

  let services = [];
  try {
    services = await api.listServices();
  } catch (e) {
    toast(e.message || "Erro ao carregar serviços");
  }

  const list = el("div",{class:"section"});
  wrap.append(list);

  let selected = null;
  let selectedCard = null;

  function render(){
    list.innerHTML = "";
    if (services.length === 0) {
      list.append(
        el("div", { class: "card reveal" }, [
          el("div", { class: "card__title" }, "Nenhum serviço disponível"),
          el(
            "div",
            { class: "card__desc" },
            "Cadastre serviços no painel admin ou reinicie o servidor para carregar os dados iniciais."
          )
        ])
      );
      return;
    }
    for(const s of services){
      const card = el("button",{class:`card reveal ${selected?.id===s.id ? "card--selected":""}`, type:"button", style:"text-align:left; width:100%; cursor:pointer"});
      card.append(
        el("div",{class:"card__row"}, [
          el("div",{}, [
            el("div",{class:"card__title"}, [
              el("i",{"data-lucide": (s.icon || "scissors")}),
              s.nome
            ]),
            el("div",{class:"card__desc"}, s.descricao || "")
          ]),
          s.tag ? el("span",{class:"badge"}, s.tag) : el("span",{})
        ]),
        el("div",{style:"margin-top:10px; display:flex; justify-content:space-between; gap:10px"}, [
          el("div",{class:"helper"}, `${s.duracaoMinutos || s.duracaoMin} min`),
          el("div",{style:"font-weight:700"}, fmtMoney(s.preco))
        ])
      );
      card.addEventListener("click", ()=>{
        selected = s;
        if (selectedCard) selectedCard.classList.remove("card--selected");
        card.classList.add("card--selected");
        selectedCard = card;
      });
      list.append(card);
    }
  }

  const cta = el("button",{class:"btn btn--primary reveal", type:"button", onClick: ()=>{
    if(!selected) return toast("Escolha um serviço para continuar.");
    ctx.wizard.serviceId = selected.id;
    location.hash = "#/book/barber";
  }}, "Continuar");
  wrap.append(cta);

  render();
  mountReveal(wrap);
  return wrap;
}

function fmtMoney(v){
  try{ return new Intl.NumberFormat("pt-BR",{style:"currency",currency:"BRL"}).format(v); }
  catch{ return "R$ "+Number(v).toFixed(2).replace(".",","); }
}
