# Modulo Consumer (`consumer-web`)

## 1. Descrizione

Il servizio **Consumer** è un'applicazione Web Spring Boot basata su Thymeleaf che espone l'interfaccia utente grafica. Recupera i dati inviando richieste HTTP al servizio Producer e **non si collega mai direttamente al database**.

---

## 2. Tecnologie

- **Java 21**
- **Spring Boot 4.1.1** (Spring Web, Thymeleaf)
- **Maven**

---

## 3. Immagine Docker Hub

- `simonealbanese/consumer-web:1.0`

---

## 4. Configurazione e Porte

- **Porta esposta:** `8082`
- **Variabili d'Ambiente:**
  - `PRODUCER_API_URL=http://producer:8081`

---

## 5. Rotte Web Utente

| Metodo HTTP | URL Browser | Descrizione |
| :--- | :--- | :--- |
| `GET` | `/prodotti` | Mostra la pagina HTML con la lista dei prodotti visualizzati a schermo |