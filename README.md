# Esercitazione: Containerizzazione e Rilascio con Docker (Producer-Consumer)

Progetto di evolutiva per la containerizzazione e il rilascio di un'architettura microservizi Spring Boot (Producer/Consumer) con database MySQL, gestita tramite Docker e Docker Compose.

---

## 1. Architettura dell'Applicazione

L'applicazione è composta da tre servizi isolati che comunicano all'interno di una rete Docker condivisa (`esercitazione-api_default`):

```text
                                Docker Network
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│   ┌──────────────┐          HTTP REST          ┌──────────────┐         │
│   │   Consumer   │ ──────────────────────────> │   Producer   │         │
│   │    :8082     │  http://producer:8081/api   │    :8081     │         │
│   └──────────────┘                             └──────┬───────┘         │
│                                                       │                 │
│                                                       │ JDBC            │
│                                                       v                 │
│                                                ┌──────────────┐         │
│                                                │    MySQL     │         │
│                                                │    :3306     │         │
│                                                └──────┬───────┘         │
│                                                       │                 │
│                                                ┌──────v───────┐         │
│                                                │ Docker Volume│         │
│                                                └──────────────┘         │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

- **Consumer (`consumer-web`)**: Applicazione Web Spring Boot + Thymeleaf che espone la UI sulla porta `8082`. Comunica esclusivamente con il Producer e **non accede mai direttamente al database**.
- **Producer (`producer-api`)**: REST API Spring Boot sulla porta `8081` che gestisce la logica di business e il catalogo prodotti.
- **Database (`esercitazione-db`)**: Database MySQL 8.0 sulla porta `3306`.

---

## 2. Tecnologie Utilizzate

- **Java 21**
- **Spring Boot 4** (Spring Data JPA, Web, Thymeleaf)
- **MySQL 8.0**
- **Docker** & **Docker Compose**
- **Maven**

---

## 3. Immagini Docker Hub

Le immagini containerizzate sono state pubblicate su Docker Hub:

- **Producer API**: `simonealbanese/producer-api:1.0`
- **Consumer Web**: `simonealbanese/consumer-web:1.0`

---

## 4. Struttura del Progetto

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

## 5. Struttura del Database
### specifiche Database
Username: root; Password: root
Porta: 3306

### Struttura tabella utilizzata per il salvataggio di ogni prodotto
CREATE TABLE prodotti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    descrizione TEXT,
    prezzo DECIMAL(10,2) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    quantita INT NOT NULL DEFAULT 0,
    data_creazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

## 6. Guida all'Avvio e al Rilascio

### Prerequisiti
- Docker Desktop / Docker Engine installato e attivo.
- Docker Compose.

---

### Rilascio da Immagini Docker Hub
## link DockerHub per il download delle immagini
docker pull simonealbanese/producer-api:1.0
docker pull simonealbanese/consumer-web:1.0

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

## 9. Comandi di Gestione

- **Avvio del container:**
  ```bash
  docker compose up -d
  ```

- **Visualizzare lo stato dei container:**
  ```bash
  docker ps
  ```

- **Visualizzare i log in tempo reale:**
  ```bash
  docker compose logs -f
  ```

- **Visualizzare i log di un singolo servizio (es. Producer):**
  ```bash
  docker compose logs -f producer
  ```

- **Arrestare i servizi (mantenendo i dati nel volume):**
  ```bash
  docker compose stop
  ```

- **Arrestare e rimuovere container e rete:**
  ```bash
  docker compose down
  ```

## 10. Percentuale di utilizzo delle AI generative
### Utilizzo di AI generative in percentuale rispetto a sè stesso, non sull'influenza nel progetto finito
Claude: 50%; Gemini: 50%.