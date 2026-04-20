import { el } from "../ui.js";
import * as api from "../api.js";

export function HomePage(ctx) {
  const div = document.createElement("div");
  const userName = ctx?.user?.nome?.split(" ")[0] || "Cliente";

  div.innerHTML = `
    <section class="section">
      <h1 class="h1">Olá, ${userName} 👋</h1>
      <p class="sub">Bem-vindo à Bellator Barbearia. Seu agendamento em poucos cliques.</p>
    </section>

    <section class="section grid3">
      <div class="stat">
        <i data-lucide="star"></i>
        <div class="stat__v" id="avgRating">...</div>
        <div class="stat__k">Avaliação</div>
      </div>

      <div class="stat">
        <i data-lucide="clock"></i>
        <div class="stat__v">30 min</div>
        <div class="stat__k">Média</div>
      </div>

      <div class="stat" id="gpsBtn" style="cursor:pointer;" title="Abrir GPS">
        <i data-lucide="map-pin"></i>
        <div class="stat__v" id="gpsDist">Calc...</div>
        <div class="stat__k">Rota Maps</div>
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

    const gpsDist = div.querySelector("#gpsDist");
    const gpsBtn = div.querySelector("#gpsBtn");

    // Coordenadas aproximadas da barbearia: Paulista - PE
    const BARBER_LAT = -7.8967;
    const BARBER_LON = -34.8252;
    const mapsUrl = `https://maps.google.com/?q=${BARBER_LAT},${BARBER_LON}`;

    if (gpsBtn) {
      gpsBtn.addEventListener("click", () => {
        window.open(mapsUrl, "_blank");
      });
    }

    const avgRatingEl = div.querySelector("#avgRating");
    if (avgRatingEl) {
      api.getAverageRating().then(media => {
        avgRatingEl.textContent = media > 0 ? media.toFixed(1) : "N/A";
      }).catch(() => {
        avgRatingEl.textContent = "4.9"; // Fallback
      });
    }

    if (navigator.geolocation && gpsDist) {
      navigator.geolocation.getCurrentPosition(
        (pos) => {
          const lat = pos.coords.latitude;
          const lon = pos.coords.longitude;
          
          // Fórmula de Haversine
          const R = 6371; // Raio da Terra em km
          const dLat = (BARBER_LAT - lat) * Math.PI / 180;
          const dLon = (BARBER_LON - lon) * Math.PI / 180;
          const a = 
            0.5 - Math.cos(dLat)/2 + 
            Math.cos(lat * Math.PI / 180) * Math.cos(BARBER_LAT * Math.PI / 180) * 
            (1 - Math.cos(dLon)) / 2;
          const d = R * 2 * Math.asin(Math.sqrt(a));

          gpsDist.textContent = d.toFixed(1) + " km";
        },
        (err) => {
          gpsDist.textContent = "S/ GPS";
          console.warn("GPS negado ou indisponível:", err);
        }
      );
    } else if (gpsDist) {
      gpsDist.textContent = "S/ GPS";
    }

    window.lucide?.createIcons?.();
  }, 0);

  return div;
}