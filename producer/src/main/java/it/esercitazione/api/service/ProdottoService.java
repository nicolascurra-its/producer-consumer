package it.esercitazione.api.service;

import it.esercitazione.api.dto.ProdottoRequest;
import it.esercitazione.api.dto.ProdottoResponse;
import it.esercitazione.api.exception.ProdottoNotFoundException;
import it.esercitazione.api.model.Prodotto;
import it.esercitazione.api.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdottoService {

    /** Campi per cui e' consentito l'ordinamento: chiave in minuscolo -> nome della proprieta' dell'entity. */
    private static final Map<String, String> CAMPI_ORDINABILI = Map.of(
            "prezzo", "prezzo",
            "nome", "nome",
            "categoria", "categoria",
            "quantita", "quantita",
            "datacreazione", "dataCreazione"
    );

    private final ProdottoRepository prodottoRepository;

    // ------------------------------------------------------------------ CRUD

    @Transactional
    public ProdottoResponse creaProdotto(ProdottoRequest request) {
        Prodotto prodotto = new Prodotto();
        copiaCampi(request, prodotto);
        return toResponse(prodottoRepository.save(prodotto));
    }

    public List<ProdottoResponse> trovaTutti() {
        return prodottoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(this::toResponse).toList();
    }

    public ProdottoResponse trovaPerId(Long id) {
        return toResponse(cercaEntity(id));
    }

    @Transactional
    public ProdottoResponse aggiornaProdotto(Long id, ProdottoRequest request) {
        Prodotto prodotto = cercaEntity(id);
        copiaCampi(request, prodotto);
        return toResponse(prodottoRepository.save(prodotto));
    }

    @Transactional
    public void eliminaProdotto(Long id) {
        if (!prodottoRepository.existsById(id)) {
            throw new ProdottoNotFoundException(id);
        }
        prodottoRepository.deleteById(id);
    }

    // ---------------------------------------------------------------- Ricerche

    public List<ProdottoResponse> trovaPerCategoria(String categoria) {
        return prodottoRepository.findByCategoriaIgnoreCase(categoria.trim(), Sort.by("id"))
                .stream().map(this::toResponse).toList();
    }

    public List<ProdottoResponse> cercaPerNome(String nome) {
        return prodottoRepository.findByNomeContainingIgnoreCase(nome.trim(), Sort.by("id"))
                .stream().map(this::toResponse).toList();
    }

    public List<ProdottoResponse> ordinaPerPrezzo(String direction) {
        return prodottoRepository.findAll(costruisciSort("prezzo", direction))
                .stream().map(this::toResponse).toList();
    }

    /**
     * Ricerca combinata: categoria, nome e ordinamento sono tutti opzionali e componibili.
     */
    public List<ProdottoResponse> cerca(String categoria, String nome, String sort, String direction) {
        return prodottoRepository
                .cerca(normalizza(categoria), normalizza(nome), costruisciSort(sort, direction))
                .stream().map(this::toResponse).toList();
    }

    // ----------------------------------------------------------------- Helper

    private Prodotto cercaEntity(Long id) {
        return prodottoRepository.findById(id).orElseThrow(() -> new ProdottoNotFoundException(id));
    }

    private Sort costruisciSort(String sort, String direction) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String proprieta = CAMPI_ORDINABILI.get(sort.trim().toLowerCase());
        if (proprieta == null) {
            throw new IllegalArgumentException(
                    "Parametro sort non valido: valori ammessi prezzo, nome, categoria, quantita, dataCreazione");
        }
        Sort.Direction dir = Sort.Direction.ASC;
        if (direction != null && !direction.isBlank()) {
            if (direction.equalsIgnoreCase("desc")) {
                dir = Sort.Direction.DESC;
            } else if (!direction.equalsIgnoreCase("asc")) {
                throw new IllegalArgumentException("Parametro direction non valido: usare 'asc' oppure 'desc'");
            }
        }
        // a parita' di valore, ordine stabile per id
        return Sort.by(dir, proprieta).and(Sort.by(Sort.Direction.ASC, "id"));
    }

    private String normalizza(String valore) {
        return (valore == null || valore.isBlank()) ? null : valore.trim();
    }

    private void copiaCampi(ProdottoRequest request, Prodotto prodotto) {
        prodotto.setNome(request.getNome().trim());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setPrezzo(request.getPrezzo());
        prodotto.setCategoria(request.getCategoria().trim());
        prodotto.setQuantita(request.getQuantita());
    }

    private ProdottoResponse toResponse(Prodotto p) {
        return ProdottoResponse.builder()
                .id(p.getId())
                .nome(p.getNome())
                .descrizione(p.getDescrizione())
                .prezzo(p.getPrezzo())
                .categoria(p.getCategoria())
                .quantita(p.getQuantita())
                .dataCreazione(p.getDataCreazione())
                .build();
    }
}
