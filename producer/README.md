# Esercitazione: Containerizzazione e Rilascio con Docker (Producer-Consumer)


# 1. Architettura dell'Applicazione

L'applicazione è composta da tre servizi isolati che comunicano all'interno di una rete Docker condivisa (`esercitazione-api_default`):

- **Consumer (`consumer-web`)**: Applicazione Web Spring Boot + Thymeleaf che espone la UI sulla porta `8082`. Comunica esclusivamente con il Producer e **non accede mai direttamente al database**.
- **Producer (`producer-api`)**: REST API Spring Boot sulla porta `8081` che gestisce la logica di business e il catalogo prodotti.
- **Database (`esercitazione-db`)**: Database MySQL 8.0 sulla porta `3306`.

---

# 2. Tecnologie Utilizzate

- **Java 21**
- **Spring Boot 4.1.1** (Spring Data JPA, Web, Thymeleaf)
- **MySQL 8.0**
- **Docker**
- **Maven**

---

# 3. Immagini Docker Hub

Le immagini containerizzate sono state pubblicate su Docker Hub:

- **Producer API**: `simonealbanese/producer-api:1.0`
- **Consumer Web**: `simonealbanese/consumer-web:1.0`

---

# 4. Struttura del Progetto

```text
esercitazione-api/
│
├── compose.yaml
├── README.md
│
├── producer/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│
└── consumer/
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

---

# 5. Database (MySQL)

Il servizio MySQL viene eseguito in un container Docker dedicato e configurato tramite le seguenti credenziali predefinite (modificabili via variabili d'ambiente nel `compose.yaml`):

- **Host (locale):** `localhost:3306`
- **Host (interno alla rete Docker):** `db:3306`
- **Database:** `esercitazione_api`
- **Username:** `root`
- **Password:** `root`

# 6. Guida all'Avvio e al Rilascio

## Prerequisiti
- Docker Desktop / Docker Engine installato.

---

## Comandi

## utilizzo delle immagini
1. Posizionarsi nella radice dove risiede il file `compose.yaml`.
2. Scaricare le immagini da Docker Hub:
   ```bash
   docker compose pull
   ```
3. Avviare i container in background:
   ```bash
   docker compose up -d
   ```

---

## 7. Verifica degli Endpoints

Una volta avviati i container, è possibile accedere ai servizi:

- **Interfaccia Grafica Consumer**: http://localhost:8082/prodotti
- **REST API Producer**: http://localhost:8081/api/prodotti

---

## 8. Configurazione Rete e Persistenza

- **Docker Network**: I container comunicano tra loro utilizzando i nomi dei servizi definiti nel file `compose.yaml` (`producer` per le API REST e `db` per il database MySQL).
- **Docker Volume**: I dati del database vengono mantenuti nel volume gestito `mysql-data` caricato nel percorso `/var/lib/mysql`. Ciò garantisce che i dati sopravvivano anche fermando o distruggendo i container.

---

## 9. Collaboratori e AI

| Ruolo | Nome |
| :--- | :--- |
| **Sviluppatore** | Simone Albanese |
| **Sviluppatore** | Nicolas Curra |
| **Sviluppatore** | Gabriele Serrain |

**Utilizzo AI:** Claude 30%, Gemini 30%, sviluppo manuale 40%