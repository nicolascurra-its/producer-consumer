package it.esercitazione.client.client;

import it.esercitazione.client.dto.ProdottoDTO;
import it.esercitazione.client.exception.ApiException;
import it.esercitazione.client.exception.ApiUnavailableException;
import it.esercitazione.client.exception.ProdottoNonTrovatoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unico punto di comunicazione con la Producer Application (solo HTTP, nessun accesso al database).
 */
@Component
public class ProdottoApiClient {

    private static final Logger log = LoggerFactory.getLogger(ProdottoApiClient.class);

    private final RestClient restClient;

    public ProdottoApiClient(RestClient producerRestClient) {
        this.restClient = producerRestClient;
    }

    /** GET /api/prodotti */
    public List<ProdottoDTO> trovaTutti() {
        return cercaProdotti(null, null, null, null);
    }

    /**
     * GET /api/prodotti?nome=..&categoria=..&sort=..&direction=..
     * I parametri nulli o vuoti non vengono inviati.
     */
    public List<ProdottoDTO> cercaProdotti(String nome, String categoria, String sort, String direction) {
        try {
            List<ProdottoDTO> risultato = restClient.get()
                    .uri(uriBuilder -> {
                        Map<String, Object> variabili = new HashMap<>();
                        uriBuilder.path("/prodotti");
                        aggiungiParametro(uriBuilder, variabili, "nome", nome);
                        aggiungiParametro(uriBuilder, variabili, "categoria", categoria);
                        aggiungiParametro(uriBuilder, variabili, "sort", sort);
                        aggiungiParametro(uriBuilder, variabili, "direction", direction);
                        return uriBuilder.build(variabili);
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProdottoDTO>>() { });
            return risultato != null ? risultato : List.of();
        } catch (ResourceAccessException e) {
            throw nonRaggiungibile(e);
        } catch (RestClientResponseException e) {
            throw erroreApi(e);
        }
    }

    /** GET /api/prodotti/{id} */
    public ProdottoDTO trovaPerId(Long id) {
        try {
            ProdottoDTO prodotto = restClient.get()
                    .uri("/prodotti/{id}", id)
                    .retrieve()
                    .body(ProdottoDTO.class);
            if (prodotto == null) {
                throw new ProdottoNonTrovatoException(id, null);
            }
            return prodotto;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProdottoNonTrovatoException(id, e);
        } catch (ResourceAccessException e) {
            throw nonRaggiungibile(e);
        } catch (RestClientResponseException e) {
            throw erroreApi(e);
        }
    }

    /** Elenco delle categorie presenti (ricavate dai prodotti restituiti dalla API), in ordine alfabetico. */
    public List<String> trovaCategorie() {
        return trovaTutti().stream()
                .map(ProdottoDTO::getCategoria)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    // ---------------------------------------------------------------- helper

    private void aggiungiParametro(UriBuilder builder, Map<String, Object> variabili, String nome, String valore) {
        if (valore != null && !valore.isBlank()) {
            builder.queryParam(nome, "{" + nome + "}");
            variabili.put(nome, valore.trim());
        }
    }

    private ApiUnavailableException nonRaggiungibile(ResourceAccessException e) {
        log.warn("Producer API non raggiungibile: {}", e.getMessage());
        return new ApiUnavailableException("Il servizio API non è attualmente disponibile.", e);
    }

    private ApiException erroreApi(RestClientResponseException e) {
        log.warn("Errore dalla Producer API: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
        return new ApiException("La API ha risposto con un errore (HTTP " + e.getStatusCode().value() + ").", e);
    }
}
