package it.esercitazione.api.controller;

import it.esercitazione.api.dto.ProdottoRequest;
import it.esercitazione.api.dto.ProdottoResponse;
import it.esercitazione.api.service.ProdottoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    /**
     * Elenco prodotti con filtri opzionali e componibili:
     * GET /api/prodotti?categoria=Accessori&nome=pro&sort=prezzo&direction=desc
     */
    @GetMapping
    public List<ProdottoResponse> elenco(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction) {
        return prodottoService.cerca(categoria, nome, sort, direction);
    }

    @GetMapping("/{id}")
    public ProdottoResponse dettaglio(@PathVariable Long id) {
        return prodottoService.trovaPerId(id);
    }

    @PostMapping
    public ResponseEntity<ProdottoResponse> crea(@Valid @RequestBody ProdottoRequest request) {
        ProdottoResponse creato = prodottoService.creaProdotto(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creato.getId())
                .toUri();
        return ResponseEntity.created(location).body(creato);
    }

    @PutMapping("/{id}")
    public ProdottoResponse aggiorna(@PathVariable Long id, @Valid @RequestBody ProdottoRequest request) {
        return prodottoService.aggiornaProdotto(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(@PathVariable Long id) {
        prodottoService.eliminaProdotto(id);
        return ResponseEntity.noContent().build();
    }
}
