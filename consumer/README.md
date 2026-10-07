# Consumer – `product-client`

## Collaboratori

- _[Inserire nome e cognome degli studenti]_
- **AI:** Claude (Anthropic) – generazione del codice e della documentazione

## Descrizione del progetto

Applicazione Spring Boot con interfaccia web **Thymeleaf** che visualizza i prodotti del catalogo.
**Non accede al database**: tutti i dati sono recuperati via HTTP dalla REST API della
*Producer Application* (`../producer`, porta 8081).

```
Browser → Consumer (:8082) → HTTP → Producer REST API (:8081) → Service → Repository → MySQL
```

Pagine disponibili:

| URL | Descrizione |
|---|---|
| `/` | Redirect a `/prodotti` |
| `/prodotti` | Tabella prodotti + ricerca per nome, filtro per categoria, ordinamento per prezzo |
| `/prodotti/{id}` | Dettaglio del prodotto |

## Tecnologie utilizzate

Java 21 · Spring Boot 4 · Spring Web (MVC) · `RestClient` · Thymeleaf · Lombok · Maven

## Requisiti necessari

- JDK 21
- Maven 3.9+
- **Producer Application in esecuzione** su `http://localhost:8081` (e quindi MySQL attivo)

## Configurazione MySQL

Non necessaria: la Consumer non si collega al database. La configurazione MySQL è descritta in `../producer/README.md`.

## Configurazione della Producer

Da avviare per prima, vedere `../producer/README.md`.

## Configurazione della Consumer

File `src/main/resources/application.properties`:

| Property | Valore | Note |
|---|---|---|
| `spring.application.name` | `product-client` | |
| `server.port` | `8082` | |
| `api.base-url` | `http://localhost:8081/api` | Indirizzo della Producer API (non scritto nel codice Java) |
| `api.connect-timeout-seconds` | `3` | Timeout di connessione |
| `api.read-timeout-seconds` | `5` | Timeout di lettura |

L'indirizzo può essere sovrascritto senza modificare il file:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--api.base-url=http://altro-host:8081/api
```

## Istruzioni per l'avvio

1. Avviare MySQL e la **Producer** (`cd producer && mvn spring-boot:run`).
2. Avviare la Consumer:

```bash
cd consumer
mvn spring-boot:run
```

3. Aprire <http://localhost:8082/prodotti>.

## Struttura

```
it.esercitazione.client
├── ProductClientApplication
├── config      RestClientConfig        (RestClient con base URL e timeout da properties)
├── client      ProdottoApiClient       (chiamate HTTP verso la Producer API)
├── dto         ProdottoDTO             (modello lato client, distinto dall'Entity del Producer)
├── controller  ProdottoWebController   (pagine Thymeleaf)
└── exception   ApiException, ApiUnavailableException, ProdottoNonTrovatoException
```

## REST API utilizzate (Producer)

| Chiamata del client | Uso |
|---|---|
| `GET /api/prodotti` | Elenco prodotti e ricavo delle categorie per il menu a tendina |
| `GET /api/prodotti?nome=..&categoria=..&sort=prezzo&direction=asc\|desc` | Ricerca / filtro / ordinamento dalla pagina `/prodotti` |
| `GET /api/prodotti/{id}` | Pagina di dettaglio |

I parametri di ricerca vuoti non vengono inviati alla API.

## Gestione degli errori

- **Producer non raggiungibile** (connessione rifiutata / timeout): la pagina `/prodotti` mostra
  *«Impossibile recuperare i prodotti. Il servizio API non è attualmente disponibile.»* (il dettaglio risponde con HTTP 503).
- **Prodotto inesistente** (404 dalla API): pagina di errore dedicata, HTTP 404.
- **Altri errori HTTP dalla API**: messaggio generico, HTTP 502.

## Funzionalità aggiuntive implementate

- Ordinamento per prezzo (crescente / decrescente) combinabile con nome e categoria.
- Timeout configurabili sulle chiamate verso la API.
- Formattazione italiana di prezzi (`€ 1.299,90`) e date.

Non implementate: cache locale e autenticazione (livello esperto), come concordato per la parte sicurezza.
