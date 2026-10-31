# Modulo Producer (`producer-api`)

## 1. Descrizione

Il servizio **Producer** è un'applicazione Spring Boot che espone le API REST per la gestione del catalogo prodotti. Interagisce direttamente con il database MySQL (`esercitazione-db`) ed è l'unico componente a cui è consentita la persistenza dei dati.

---

## 2. Tecnologie

- **Java 21**
- **Spring Boot 4.1.1** (Spring Data JPA, Spring Web)
- **MySQL Driver**
- **Maven**

---

## 3. Immagine Docker Hub

- `simonealbanese/producer-api:1.0`

---

## 4. Configurazione e Porte

- **Porta esposta:** `8081`
- **Variabili d'Ambiente:**
  - `SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/esercitazione_api?useSSL=false&allowPublicKeyRetrieval=true`
  - `SPRING_DATASOURCE_USERNAME=root`
  - `SPRING_DATASOURCE_PASSWORD=root`

---

## 5. REST API Esposte

| Metodo HTTP | Endpoint | Descrizione |
| :--- | :--- | :--- |
| `GET` | `/api/prodotti` | Restituisce la lista di tutti i prodotti (JSON) |
| `GET` | `/api/prodotti/{id}` | Restituisce i dettagli del singolo prodotto tramite ID (JSON) |