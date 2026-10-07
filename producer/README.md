# Producer – `product-api`

## Collaboratori

- _[Inserire nome e cognome degli studenti]_
- **AI:** Claude (Anthropic) – generazione del codice e della documentazione

## Descrizione del progetto

Applicazione Spring Boot che gestisce il catalogo prodotti e lo espone tramite **REST API**
(`http://localhost:8081/api/prodotti`). I dati sono persistiti su **MySQL** (database
`esercitazione_api`, tabella `prodotti`) tramite Spring Data JPA / Hibernate.

È la parte *Producer* del sistema: la *Consumer Application* (`../consumer`) legge i dati solo
tramite queste API, senza accedere al database.

```
Controller → Service → Repository → MySQL
```

## Tecnologie utilizzate

Java 21 · Spring Boot 4 · Spring Web (MVC) · Spring Data JPA · Hibernate · Bean Validation ·
MySQL · Lombok · Maven

## Requisiti necessari

- JDK 21
- Maven 3.9+
- MySQL 8+ in esecuzione su `localhost:3306`

## Configurazione MySQL

Creare database, tabella e dati iniziali con lo script fornito:

```bash
mysql -u root -p < sql/esercitazione_api.sql
```

Verifica (devono risultare 20 prodotti):

```sql
USE esercitazione_api;
SELECT * FROM prodotti;
```

> Lo script contiene `DROP TABLE IF EXISTS prodotti`: rieseguirlo riporta la tabella allo stato iniziale.
> In alternativa si può creare solo il database e lasciare che Hibernate crei la tabella
> (`spring.jpa.hibernate.ddl-auto=update`), ma in tal caso la tabella parte vuota.

## Configurazione della Producer

File `src/main/resources/application.properties`:

| Property | Valore |
|---|---|
| `spring.application.name` | `product-api` |
| `server.port` | `8081` |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/esercitazione_api?...` |
| `spring.datasource.username` | `root` |
| `spring.datasource.password` | `${DB_PASSWORD:PASSWORD}` |
| `spring.jpa.hibernate.ddl-auto` | `update` |

Impostare la password MySQL locale, oppure modificando il file, oppure con la variabile d'ambiente:

```bash
export DB_PASSWORD=la_mia_password      # Linux / macOS
set DB_PASSWORD=la_mia_password         # Windows (cmd)
```

## Istruzioni per l'avvio

```bash
cd producer
mvn spring-boot:run
```

Verifica: <http://localhost:8081/api/prodotti>

Il file `http/prodotti.http` contiene tutte le richieste di test (REST Client di IntelliJ IDEA / VS Code).

## REST API disponibili

Base URL: `http://localhost:8081/api/prodotti`

| Metodo | Endpoint | Descrizione | Risposta |
|---|---|---|---|
| GET | `/api/prodotti` | Elenco prodotti (con filtri opzionali) | 200 |
| GET | `/api/prodotti/{id}` | Dettaglio prodotto | 200 / 404 |
| POST | `/api/prodotti` | Crea un prodotto | 201 / 400 |
| PUT | `/api/prodotti/{id}` | Aggiorna un prodotto | 200 / 400 / 404 |
| DELETE | `/api/prodotti/{id}` | Elimina un prodotto | 204 / 404 |

### Parametri di ricerca (`GET /api/prodotti`)

Tutti opzionali e **combinabili**:

| Parametro | Descrizione | Esempio |
|---|---|---|
| `categoria` | Filtro per categoria (case-insensitive) | `?categoria=Informatica` |
| `nome` | Il nome *contiene* il termine (case-insensitive) | `?nome=laptop` |
| `sort` | Campo di ordinamento: `prezzo` (default dell'esercitazione), `nome`, `categoria`, `quantita`, `dataCreazione` | `?sort=prezzo` |
| `direction` | `asc` (default) o `desc` | `?sort=prezzo&direction=desc` |

Esempi combinati:

```
GET /api/prodotti?categoria=Accessori&sort=prezzo&direction=asc
GET /api/prodotti?nome=pro&sort=prezzo&direction=desc
```

### Esempio di body (POST / PUT)

```json
{
  "nome": "Notebook Gaming",
  "descrizione": "Notebook per gaming e sviluppo software",
  "prezzo": 1599.90,
  "categoria": "Informatica",
  "quantita": 10
}
```

Validazione: `nome` e `categoria` obbligatori, `prezzo` obbligatorio e ≥ 0 (max 2 decimali),
`quantita` obbligatoria e ≥ 0.

### Gestione errori

Gli errori sono gestiti in modo centralizzato (`@RestControllerAdvice`) e restituiscono:

```json
{
  "status": 404,
  "message": "Prodotto non trovato",
  "timestamp": "2026-09-28T18:30:00"
}
```

| Caso | Status |
|---|---|
| Prodotto inesistente | 404 |
| Dati non validi, JSON malformato, `sort`/`direction` non validi, id non numerico | 400 |
| Metodo HTTP non supportato | 405 |
| Errore imprevisto | 500 |

## Funzionalità aggiuntive implementate

- DTO dedicati `ProdottoRequest` / `ProdottoResponse`: l'Entity JPA non viene mai esposta dal controller.
- Validazione dei dati in ingresso con Bean Validation.
- Ordinamento anche su `nome`, `categoria`, `quantita`, `dataCreazione`, con ordine stabile (a parità di valore, per `id`).
- Header `Location` nella risposta `201 Created`.

Non implementata: autenticazione (Spring Security), come concordato.
