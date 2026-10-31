# Esercitazione: Containerizzazione e Rilascio con Docker (Producer-Consumer)

## 1. Collaboratori e AI

| Ruolo | Nome |
| :--- | :--- |
| **Sviluppatore** | Simone Albanese |
| **Sviluppatore** | Nicolas Curra |
| **Sviluppatore** | Gabriele Serrain |

**Utilizzo AI:** Claude 30%, Gemini 30%, sviluppo manuale 40%

---

## 2. Descrizione progetto

L'applicazione è composta da tre servizi isolati che comunicano all'interno di una rete Docker condivisa (`esercitazione-api_default`):

- **Consumer (`consumer-web`)**: Applicazione Web Spring Boot + Thymeleaf che espone la UI sulla porta `8082`. Comunica esclusivamente con il Producer e **non accede mai direttamente al database**.
- **Producer (`producer-api`)**: REST API Spring Boot sulla porta `8081` che gestisce la logica di business e il catalogo prodotti.
- **Database (`esercitazione-db`)**: Database MySQL 8.0 sulla porta `3306`.

---

## 3. Tecnologie Utilizzate

- **Java 21**
- **Spring Boot 4.1.1** (Spring Data JPA, Web, Thymeleaf)
- **MySQL 8.0**
- **Docker**
- **Maven**

---

## 4. Struttura del Progetto

```text
esercitazione-api/
│
├── compose.yaml
├── README.md
│
├── producer/
    ├── README.md
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│
└── consumer/
    ├── README.md
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

---

## 5. Configurazione dei Servizi

### Configurazione MySQL (`db`)
- **Immagine:** `mysql:8.0`
- **Porta Host:** `3306`
- **Variabili d'ambiente:**
  - `MYSQL_ROOT_PASSWORD=root`
  - `MYSQL_DATABASE=esercitazione_api`
- **Inizializzazione:** Mappatura dello script SQL `./producer/sql/esercitazione_api.sql` in `/docker-entrypoint-initdb.d/` per il popolamento automatico del DB.

### Configurazione Producer (`producer`)
- **Immagine:** `simonealbanese/producer-api:1.0`
- **Porta Host:** `8081`
- **Variabili d'ambiente:**
  - `SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/esercitazione_api?useSSL=false&allowPublicKeyRetrieval=true`
  - `SPRING_DATASOURCE_USERNAME=root`
  - `SPRING_DATASOURCE_PASSWORD=root`

### Configurazione Consumer (`consumer`)
- **Immagine:** `simonealbanese/consumer-web:1.0`
- **Porta Host:** `8082`
- **Variabili d'ambiente:**
  - `PRODUCER_API_URL=http://producer:8081`

---

## 6. Guida all'Avvio

### Prerequisiti
- Docker Desktop / Docker Engine installato.

---

### Istruzioni per l'avvio

1. clonare la repository
   ```bash
   git clone https://github.com/nicolascurra-its/producer-consumer.git
   ```
2. Posizionarsi nella radice dove risiede il file `compose.yaml`.
   ```bash
   cd producer-consumer
   ```
3. Scaricare le immagini da Docker Hub:
   ```bash
   docker compose pull
   ```
4. Avviare i container in background:
   ```bash
   docker compose up -d
   ```

---

## 7. Verifica degli Endpoints

Una volta avviati i container, è possibile accedere ai servizi:

- **Interfaccia Grafica Consumer**: http://localhost:8082/prodotti
- **REST API Producer**: http://localhost:8081/api/prodotti