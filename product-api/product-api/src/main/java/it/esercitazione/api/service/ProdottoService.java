/**
 * Il service per l'entity `Prodotto`.
 * Implementa le operazioni di lettura, creazione, aggiornamento (parziale e di
 * stato) ed eliminazione.
 *
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import it.esercitazione.api.dto.ProdottoRequest;
import it.esercitazione.api.dto.ProdottoResponse;
import it.esercitazione.api.exception.ResourceNotFoundException;
import it.esercitazione.api.repository.ProdottoRepository;
import it.esercitazione.api.model.Prodotto;

@Service
public class ProdottoService{

    private final ProdottoRepository prodottoRepository;

    public ProdottoService(
        ProdottoRepository prodottoRepository
    ) {
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * Recupera tutti i prodotti registrati.
     *
     * @return la lista di tutti i prodotti, eventualmente vuota.
     */
    public List<ProdottoResponse> getAll() {
        return prodottoRepository.findAll()
            .stream()
            .map(ProdottoResponse::fromProdotto)
            .toList();
    }

    /**
     * Recupera un prodotto dato il suo id.
     *
     * @param id l'id del prodotto.
     * @return il `Prodotto` corrispondente.
     * @throws ResourceNotFoundException se il prodotto non esiste.
     */
    public ProdottoResponse getById(int id) {
        return ProdottoResponse.fromProdotto(findProdottoOrThrow(id));
    }

    /**
     * Recupera tutti i prodotti dato il loro prezzo.
     *
     * @param prezzo il prezzo del prodotto.
     * @return la lista dei prodotti, eventualmente vuota.
     */
    public List<ProdottoResponse> getAllByPrezzo(BigDecimal prezzo) {
        return prodottoRepository.findAllByPrezzo(prezzo)
                .stream()
                .map(ProdottoResponse::fromProdotto)
                .toList();
    }

    /**
     * Recupera tutti i prodotti data una categoria.
     *
     * @param cateoria la categoria del prodotto.
     * @return la lista dei prodotti, eventualmente vuota.
     */
    public List<ProdottoResponse> getAllByCategoria(String categoria) {
        return prodottoRepository.findAllByCategoria(categoria)
                .stream()
                .map(ProdottoResponse::fromProdotto)
                .toList();
    }

    /**
     * Recupera tutti i prodotti data una quantità.
     *
     * @param quantita la quantità di prodotto.
     * @return la lista dei prodotti, eventualmente vuota.
     */
    public List<ProdottoResponse> getAllByQuantita(int quantita) {
        return prodottoRepository.findAllByQuantita(quantita)
                .stream()
                .map(ProdottoResponse::fromProdotto)
                .toList();
    }

    /**
     * Recupera tutti i prodotti con stessa data di creazione.
     *
     * @param dataCreazione la data di creazione del prodotto.
     * @return la lista di prodotti, eventualmente vuota.
     */
    public List<ProdottoResponse> getAllByDataCreazione(LocalDate dataCreazione) {
        return prodottoRepository.findAllByDataCreazione(dataCreazione)
                .stream()
                .map(ProdottoResponse::fromProdotto)
                .toList();
    }

    /**
     * Aggiunge un nuovo Prodotto al database.
     * I campi sono già validati da Bean Validation nel controller; qui vengono
     * risolte le entità collegate a partire dai rispettivi id.
     *
     * @param request i dati validati dell'animale da creare.
     * @return l'`AnimalResponse` dell'animale appena creato.
     * @throws ResourceNotFoundException se sesso, specie, razza o anagrafica non esistono.
     */
    public ProdottoResponse addProdotto(ProdottoRequest request) {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.name());
        prodotto.setPrezzo(request.prezzo());
        prodotto.setCategoria(request.categoria());
        prodotto.setQuantita(request.quantita());
        prodotto.setDataCreazione(request.dataCreazione());
        prodotto.setDeleted(false);

        return ProdottoResponse.fromProdotto(prodottoRepository.save(prodotto));
    }

    /**
     * Aggiorna in modo parziale i dati di un prodotto.
     * Vengono modificati solo i campi valorizzati nella richiesta; i campi
     * {@code null} lasciano invariato il valore esistente. Note e stato non
     * sono modificabili da questa operazione.
     *
     * @param id l'id del prodotto da aggiornare.
     * @param prodottoRequest i dati da aggiornare (campi opzionali).
     * @return l'`ProdottoResponse` del prodotto aggiornato.
     * @throws ResourceNotFoundException se il prodotto o una delle entità collegate indicate non esistono.
     */
    public ProdottoResponse updateProdotto(int id, ProdottoRequest prodottoRequest) {
        Prodotto prodotto = findProdottoOrThrow(id);

        if (prodottoRequest.name() != null)
            prodotto.setNome(prodottoRequest.name());
        
        if (prodottoRequest.prezzo() != null)
            prodotto.setPrezzo(prodottoRequest.prezzo());
        
        if (prodottoRequest.categoria() != null)
            prodotto.setCategoria(prodottoRequest.categoria());
        
        if (prodottoRequest.quantita() != null)
            prodotto.setQuantita(prodottoRequest.quantita());
        
        if (prodottoRequest.dataCreazione() != null)
            prodotto.setDataCreazione(prodottoRequest.dataCreazione());

        return ProdottoResponse.fromProdotto(prodottoRepository.save(prodotto));
    }

    /**
     * Eliminazione logica di un Prodotto dal database.
     *
     * @param id l'id del prodotto da eliminare.
     * @throws ResourceNotFoundException se il prodotto non esiste.
     */
    public void deleteProdottoById(int id) {
        Prodotto prodotto = findProdottoOrThrow(id);
        prodotto.setDeleted(true);
        prodottoRepository.save(prodotto);
    }

    private Prodotto findProdottoOrThrow(int id) {
        return prodottoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con id " + id + "."));
    }

}