import { el, toast, mountReveal } from "../ui.js";
import * as api from "../api.js";

export function AuthPage(){
  const wrap = el("div", {});
  wrap.append(
    el("h1", {class:"h1 reveal"}, "Entrar"),
    el("p", {class:"sub reveal"}, "Acesse sua conta para agendar com poucos cliques.")
  );

  const tabs = el("div", {class:"tabs reveal"});
  const tLogin = el("button", {class:"tab is-active", type:"button"}, "ENTRAR");
  const tReg = el("button", {class:"tab", type:"button"}, "CADASTRAR");
  tabs.append(tLogin, tReg);
  wrap.append(tabs);

  const card = el("div", {class:"card reveal"});
  wrap.append(card);

  function renderLogin(){
    card.innerHTML = "";

    const email = el("input", {class:"input", type:"email", placeholder:"seu@email.com", autocomplete:"email"});
    const pass = el("input", {class:"input", type:"password", placeholder:"••••••", autocomplete:"current-password", id:"loginPass"});

    card.append(
      el("div", {class:"field"}, [el("div",{class:"label"},"E-mail"), email]),

      // SENHA COM OLHO
      el("div", {class:"field"}, [
        el("div",{class:"label"},"Senha"),
        el("div",{class:"password-wrapper"},[
          pass,
          el("i", {
            "data-lucide":"eye",
            class:"toggle-password",
            "data-target":"loginPass"
          })
        ])
      ]),

      el("div", {class:"hr"}),

      el("button", {class:"btn btn--primary", type:"button", onClick: async ()=>{
        try{
          await api.login(email.value, pass.value);
          location.hash = "#/home";
          toast("Bem-vindo 👊");
        }catch(e){
          toast(e.message || "Falha ao entrar.");
        }
      }}, "Entrar")
    );
  }

  function renderRegister(){
    card.innerHTML = "";

    const nome = el("input", {class:"input", type:"text", placeholder:"Seu nome completo", autocomplete:"name"});
    const email = el("input", {class:"input", type:"email", placeholder:"seu@email.com", autocomplete:"email"});
    const tel = el("input", {class:"input", type:"tel", placeholder:"(81) 9xxxx-xxxx", autocomplete:"tel", id:"phone"});
    const pass = el("input", {class:"input", type:"password", placeholder:"Crie uma senha", autocomplete:"new-password", id:"password"});
    const confirmPass = el("input", {class:"input", type:"password", placeholder:"Repita a senha", id:"confirmPassword"});

    const terms = el("label", {class:"helper"}, [
      el("input", {type:"checkbox"}), " Ao se cadastrar, você concorda com nossos ",
      el("a", {href:"#/terms", target:"_blank", style:"text-decoration: underline; color: var(--primary);"}, "Termos de uso"), " e ",
      el("a", {href:"#/privacy", target:"_blank", style:"text-decoration: underline; color: var(--primary);"}, "Política de privacidade"), "."
    ]);

    card.append(
      el("h2", {class:"h1", style:"font-size:18px;margin-top:0"}, "Criar conta"),
      el("p", {class:"sub", style:"margin-top:-2px"}, "Preencha seus dados para se cadastrar."),
      el("div", {class:"field"}, [el("div",{class:"label"},"Nome Completo"), nome]),
      el("div", {class:"field"}, [el("div",{class:"label"},"E-mail"), email]),
      el("div", {class:"field"}, [el("div",{class:"label"},"Telefone"), tel]),

      // SENHA COM OLHO
      el("div", {class:"field"}, [
        el("div",{class:"label"},"Senha"),
        el("div",{class:"password-wrapper"},[
          pass,
          el("i", {
            "data-lucide":"eye",
            class:"toggle-password",
            "data-target":"password"
          })
        ])
      ]),

      // CONFIRMAR SENHA
      el("div", {class:"field"}, [
        el("div",{class:"label"},"Confirmar senha"),
        el("div",{class:"password-wrapper"},[
          confirmPass,
          el("i", {
            "data-lucide":"eye",
            class:"toggle-password",
            "data-target":"confirmPassword"
          })
        ])
      ]),

      terms,
      el("div", {class:"hr"}),

      el("button", {class:"btn btn--primary", type:"button", onClick: async ()=>{
        const checked = terms.querySelector("input").checked;

        if(!checked) return toast("Aceite os termos para continuar.");
        if(pass.value.trim().length < 6) return toast("Sua senha deve ter pelo menos 6 caracteres.");
        if(pass.value !== confirmPass.value) return toast("As senhas não coincidem.");

        try{
          await api.register({nome:nome.value, email:email.value, telefone:tel.value, senha:pass.value});
          location.hash = "#/home";
          toast("Conta criada com sucesso ✅");
        }catch(e){
          toast(e.message || "Falha ao cadastrar.");
        }
      }}, "Cadastrar")
    );

    // MÁSCARA TELEFONE
    tel.addEventListener("input", (e)=>{
      let v = e.target.value.replace(/\D/g,'');

      if(!v.startsWith("81")) v = "81" + v;

      v = v.slice(0,11);

      v = v.replace(/^(\d{2})(\d)/g,"($1) $2");
      v = v.replace(/(\d{5})(\d)/,"$1-$2");

      e.target.value = v;
    });
  }

  // OLHO GLOBAL (FUNCIONA PRA LOGIN E CADASTRO)
  card.addEventListener("click", (e)=>{
    if(e.target.classList.contains("toggle-password")){
      const input = document.getElementById(e.target.dataset.target);

      if(input.type === "password"){
        input.type = "text";
        e.target.setAttribute("data-lucide","eye-off");
      }else{
        input.type = "password";
        e.target.setAttribute("data-lucide","eye");
      }

      window.lucide?.createIcons();
    }
  });

  tLogin.addEventListener("click", ()=>{
    tLogin.classList.add("is-active"); tReg.classList.remove("is-active");
    renderLogin();
  });

  tReg.addEventListener("click", ()=>{
    tReg.classList.add("is-active"); tLogin.classList.remove("is-active");
    renderRegister();
  });

  renderLogin();
  mountReveal(wrap);
  return wrap;
}