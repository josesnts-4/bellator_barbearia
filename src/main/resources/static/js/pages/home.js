import { el } from "../ui.js";

export function HomePage() {
  const div = document.createElement("div");

  div.innerHTML = `
    <section class="section">
      <h1 class="h1">Olá, Cliente 👋</h1>
      <p class="sub">Bem-vindo à Bellator Barbearia. Seu agendamento em poucos cliques.</p>
    </section>

    <section class="section grid3">
      <div class="stat">
        <i data-lucide="star"></i>
        <div class="stat__v">4.9</div>
        <div class="stat__k">Avaliação</div>
      </div>

      <div class="stat">
        <i data-lucide="clock"></i>
        <div class="stat__v">30 min</div>
        <div class="stat__k">Média</div>
      </div>

      <div class="stat">
        <i data-lucide="map-pin"></i>
        <div class="stat__v">2.0 km</div>
        <div class="stat__k">Distância</div>
      </div>
    </section>

    <section class="section">
      <div class="btn-row">
        <button class="btn btn--primary" id="goBook">
          <i data-lucide="scissors"></i>
          Agendar Agora
        </button>

        <button class="btn" id="goAppointments">
          Ver meus agendamentos
        </button>
      </div>
    </section>

    <section class="section">
      <p class="sub">Por que escolher a Bellator?</p>

      <div class="card card--tight">
        <div class="kv">
          <i data-lucide="shield"></i>
          <div>
            <strong>Profissionais de Elite</strong>
            <small>Barbeiros experientes e certificados</small>
          </div>
        </div>
      </div>

      <div class="card card--tight" style="margin-top:10px;">
        <div class="kv">
          <i data-lucide="clock"></i>
          <div>
            <strong>Agendamento Simples</strong>
            <small>Reserve seu horário em poucos cliques</small>
          </div>
        </div>
      </div>

      <div class="card card--tight" style="margin-top:10px;">
        <div class="kv">
          <i data-lucide="scissors"></i>
          <div>
            <strong>Tradição e Excelência</strong>
            <small>Experiência premium desde 2020</small>
          </div>
        </div>
      </div>
    </section>
  `;

  setTimeout(() => {
    const goBook = div.querySelector("#goBook");
    const goAppointments = div.querySelector("#goAppointments");

    if (goBook) {
      goBook.addEventListener("click", () => {
        location.hash = "#/book/service";
      });
    }

    if (goAppointments) {
      goAppointments.addEventListener("click", () => {
        location.hash = "#/appointments";
      });
    }

    window.lucide?.createIcons?.();
  }, 0);

  return div;
}