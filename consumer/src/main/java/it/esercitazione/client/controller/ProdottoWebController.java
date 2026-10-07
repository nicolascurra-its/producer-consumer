package it.esercitazione.client.controller;

import it.esercitazione.client.client.ProdottoApiClient;
import it.esercitazione.client.dto.ProdottoDTO;
import it.esercitazione.client.exception.ApiException;
import it.esercitazione.client.exception.ApiUnavailableException;
import it.esercitazione.client.exception.ProdottoNonTrovatoException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProdottoWebController {

    private final ProdottoApiClient apiClient;

    @GetMapping("/")
    public String home() {
        return "redirect:/prodotti";
    }

    /**
     * Elenco prodotti con ricerca per nome, filtro per categoria e ordinamento per prezzo.
     * Parametro "ordine": asc | desc | (vuoto = nessun ordinamento).
     */
    @GetMapping("/prodotti")
    public String elenco(@RequestParam(required = false) String nome,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String ordine,
                         Model model) {

        model.addAttribute("nome", nome);
        model.addAttribute("categoria", categoria);
        model.addAttribute("ordine", ordine);

        String sort = (ordine == null || ordine.isBlank()) ? null : "prezzo";

        try {
            List<ProdottoDTO> prodotti = apiClient.cercaProdotti(nome, categoria, sort, ordine);
            model.addAttribute("prodotti", prodotti);
            model.addAttribute("categorie", apiClient.trovaCategorie());
        } catch (ApiUnavailableException e) {
            erroreElenco(model, categoria, e.getMessage());
        } catch (ApiException e) {
            erroreElenco(model, categoria, e.getMessage());
        }
        return "prodotti";
    }

    @GetMapping("/prodotti/{id}")
    public String dettaglio(@PathVariable Long id, Model model) {
        model.addAttribute("prodotto", apiClient.trovaPerId(id));
        return "dettaglio";
    }

    // ------------------------------------------------- gestione errori dalla API

    @ExceptionHandler(ProdottoNonTrovatoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String prodottoNonTrovato(ProdottoNonTrovatoException e, Model model) {
        model.addAttribute("titolo", "Prodotto non trovato");
        model.addAttribute("messaggio", e.getMessage());
        return "errore";
    }

    @ExceptionHandler(ApiUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String apiNonDisponibile(ApiUnavailableException e, Model model) {
        model.addAttribute("titolo", "Impossibile recuperare il prodotto.");
        model.addAttribute("messaggio", e.getMessage());
        return "errore";
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String erroreApi(ApiException e, Model model) {
        model.addAttribute("titolo", "Errore nella comunicazione con il servizio API");
        model.addAttribute("messaggio", e.getMessage());
        return "errore";
    }

    private void erroreElenco(Model model, String categoriaSelezionata, String messaggio) {
        model.addAttribute("prodotti", List.of());
        model.addAttribute("categorie",
                (categoriaSelezionata == null || categoriaSelezionata.isBlank())
                        ? List.of() : List.of(categoriaSelezionata));
        model.addAttribute("erroreTitolo", "Impossibile recuperare i prodotti.");
        model.addAttribute("erroreMessaggio", messaggio);
    }
}
