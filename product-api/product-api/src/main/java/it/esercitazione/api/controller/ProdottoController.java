/**
 * RestController per l'entity `Animal`.
 * Espone le rotte sotto {@code /api/animals}.
 * 
 * @author Nicolas Currà
 * @since 07/10/2026
 */

package it.esercitazione.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import it.esercitazione.api.dto.ProdottoResponse;
import it.esercitazione.api.dto.ProdottoRequest;
    import it.esercitazione.api.service.ProdottoService;

@RestController
@RequestMapping(path = "/api/animals")
@Tag(name = "Animal", description = "Gestione degli animali e delle relative informazioni.")
public class ProdottoController {

    /**
     * Recupera l'elenco completo dei prodotti registrati.
     * Rotta riservata allo staff (veterinario, segreteria, amministratore):
     * un cliente non può visualizzare i prodotti di altri proprietari.
     *
     * @return una {@link ResponseEntity} con status 200 e la lista dei prodotti.
     */
    @Operation(
        summary     = "Elenco di tutti i prodotti",
        description = "Restituisce tutti i prodotti registrati. Accesso riservato allo staff."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Elenco dei prodotti restituito con successo.",
        content      = @Content(
            mediaType   = "application/json",
            array       = @ArraySchema(schema = @Schema(implementation = ProdottoResponse.class))
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<List<ProdottoResponse>> getAllProdotto() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Recupera un prodotto in base al suo id.
     * Rotta riservata allo staff (veterinario, segreteria, amministratore e utente se il prodotto gli appartiene):
     * un cliente non può visualizzare i prodotti di altri proprietari.
     *
     * @return una {@link ResponseEntity} con status 200 e il prodotto se esiste.
     */
    @Operation(
        summary     = "Le informazioni di un prodotto.",
        description = "Restituisce un singolo prodotto."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Le informazioni del prodotto.",
        content      = @Content(
            mediaType   = "application/json",
            schema      = @Schema(implementation = ProdottoResponse.class)
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST') or @ownership.isAnimalOwner(#id, principal)")
    public ResponseEntity<ProdottoResponse> getProdottoById(@PathVariable int id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Recupera l'elenco completo dei prodotti registrati.
     * Rotta riservata allo staff (veterinario, segreteria, amministratore):
     * un cliente non può visualizzare i prodotti di altri proprietari.
     *
     * @return una {@link ResponseEntity} con status 200 e la lista dei prodotti.
     */
    @Operation(
        summary     = "Elenco di tutti i prodotti di un cliente.",
        description = "Restituisce tutti i prodotti registrati appartenenti ad un cliente."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Elenco dei prodotti restituito con successo.",
        content      = @Content(
            mediaType   = "application/json",
            array       = @ArraySchema(schema = @Schema(implementation = ProdottoResponse.class))
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("/registry/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST') or @ownership.isRegistryOwner(#id, principal)")
    public ResponseEntity<List<ProdottoResponse>> getAllByRegistryId(@PathVariable int id) {
        return ResponseEntity.ok(service.getAllByRegistryId(id));
    }

    /**
     * Recupera tutti i prodotti di una data categoria.
     * Trattandosi di un elenco che attraversa più proprietari, la rotta è
     * riservata allo staff (veterinario, segreteria, amministratore).
     *
     * @param id l'id della categoria.
     * @return una {@link ResponseEntity} con status 200 e la lista dei prodotti.
     */
    @Operation(
        summary     = "Elenco dei prodotti per categoria",
        description = "Restituisce tutti i prodotti di una categoria. Accesso riservato allo staff."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Elenco dei prodotti restituito con successo.",
        content      = @Content(
            mediaType = "application/json",
            array     = @ArraySchema(schema = @Schema(implementation = ProdottoResponse.class))
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("/race/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<List<ProdottoResponse>> getAllByRaceId(@PathVariable int id) {
        return ResponseEntity.ok(service.getAllByRaceId(id));
    }

    /**
     * Recupera tutti i prodotti di una data specie.
     * Trattandosi di un elenco che attraversa più proprietari, la rotta è
     * riservata allo staff (veterinario, segreteria, amministratore).
     *
     * @param id l'id della specie.
     * @return una {@link ResponseEntity} con status 200 e la lista dei prodotti.
     */
    @Operation(
        summary     = "Elenco dei prodotti per specie",
        description = "Restituisce tutti i prodotti di una specie. Accesso riservato allo staff."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Elenco dei prodotti restituito con successo.",
        content      = @Content(
            mediaType = "application/json",
            array     = @ArraySchema(schema = @Schema(implementation = ProdottoResponse.class))
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("/species/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<List<ProdottoResponse>> getAllBySpeciesId(@PathVariable int id) {
        return ResponseEntity.ok(service.getAllBySpeciesId(id));
    }

    /**
     * Recupera tutti i prodotti di un dato sesso.
     * Trattandosi di un elenco che attraversa più proprietari, la rotta è
     * riservata allo staff (veterinario, segreteria, amministratore).
     *
     * @param id l'id del sesso.
     * @return una {@link ResponseEntity} con status 200 e la lista dei prodotti.
     */
    @Operation(
        summary     = "Elenco dei prodotti per sesso",
        description = "Restituisce tutti i prodotti di un sesso. Accesso riservato allo staff."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Elenco dei prodotti restituito con successo.",
        content      = @Content(
            mediaType = "application/json",
            array     = @ArraySchema(schema = @Schema(implementation = ProdottoResponse.class))
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @GetMapping("/sex/{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<List<ProdottoResponse>> getAllBySexId(@PathVariable int id) {
        return ResponseEntity.ok(service.getAllBySexId(id));
    }

    /**
     * Crea un nuovo prodotto. I dati della richiesta vengono validati
     * automaticamente da Bean Validation ({@code @Valid}).
     * Ogni utente autenticato può creare un prodotto, ma un cliente può
     * associarlo solo alla propria anagrafica; lo staff può crearlo per
     * qualsiasi anagrafica.
     *
     * @param request   i dati validati del prodotto da creare.
     * @param principal l'utente autenticato che effettua la richiesta.
     * @return una {@link ResponseEntity} con status 201 e il prodotto creato.
     */
    @Operation(
        summary     = "Crea un nuovo prodotto",
        description = "Crea un prodotto collegato a un'anagrafica. Il cliente può creare solo per la propria anagrafica."
    )
    @ApiResponse(
        responseCode = "201",
        description  = "Prodotto creato con successo.",
        content      = @Content(
            mediaType = "application/json",
            schema    = @Schema(implementation = ProdottoResponse.class)
        )
    )
    @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi.", content = @Content)
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente non autorizzato a creare per questa anagrafica.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Sesso, specie, razza o anagrafica non trovati.", content = @Content)
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProdottoResponse> createProdotto(
            @Valid @RequestBody CreateProdottoRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {

        // Un cliente può creare un prodotto solo per la propria anagrafica.
        if (!ownership.isStaff(principal) && !ownership.isRegistryOwner(request.registryId(), principal)) {
            throw new AccessDeniedException("Non puoi creare un prodotto per un'altra anagrafica.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.addProdotto(request));
    }

    /**
     * Aggiorna in modo parziale i dati anagrafici di un animale.
     * Solo il veterinario può modificare questi dati; note e stato sono gestiti
     * da rotte dedicate.
     *
     * @param id            l'id dell'animale da aggiornare.
     * @param animalRequest i dati validati da aggiornare (campi opzionali).
     * @return una {@link ResponseEntity} con status 200 e l'animale aggiornato.
     */
    @Operation(
        summary     = "Aggiorna un animale",
        description = "Aggiorna i dati anagrafici di un animale. Riservato al veterinario."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Animale aggiornato con successo.",
        content      = @Content(
            mediaType = "application/json",
            schema    = @Schema(implementation = AnimalResponse.class)
        )
    )
    @ApiResponse(responseCode = "400", description = "Dati della richiesta non validi.", content = @Content)
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Animale o entità collegata non trovati.", content = @Content)
    @PutMapping("{id}")
    @PreAuthorize("hasRole('VETERINARIAN')")
    public ResponseEntity<AnimalResponse> updateAnimal(@PathVariable int id, @Valid @RequestBody AnimalRequest animalRequest) {
        return ResponseEntity.ok(service.updateAnimal(id, animalRequest));
    }

    /**
     * Aggiorna le note di un animale.
     * Riservato allo staff clinico e di segreteria (veterinario, segreteria,
     * amministratore).
     *
     * @param id           l'id dell'animale.
     * @param notesRequest le nuove note.
     * @return una {@link ResponseEntity} con status 200 e l'animale aggiornato.
     */
    @Operation(
        summary     = "Aggiorna le note di un animale",
        description = "Aggiorna le note di un animale. Riservato a veterinario e segreteria."
    )
    @ApiResponse(
        responseCode = "200",
        description  = "Note aggiornate con successo.",
        content      = @Content(
            mediaType = "application/json",
            schema    = @Schema(implementation = AnimalResponse.class)
        )
    )
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Animale non trovato.", content = @Content)
    @PutMapping("{id}/notes")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<AnimalResponse> updateNotes(@PathVariable int id, @RequestBody NotesRequest notesRequest) {
        return ResponseEntity.ok(service.updateNotes(id, notesRequest));
    }

    /**
     * Elimina logicamente un animale impostandone lo stato a "eliminato"
     * tramite {@code updateStatus}. Riservato a veterinario e segreteria.
     *
     * @param id l'id dell'animale da eliminare.
     * @return una {@link ResponseEntity} con status 200 e un messaggio di conferma.
     */
    @Operation(
        summary     = "Elimina un animale",
        description = "Eliminazione logica di un animale. Riservato a veterinario e segreteria."
    )
    @ApiResponse(responseCode = "200", description = "Animale eliminato con successo.")
    @ApiResponse(responseCode = "401", description = "Utente non autenticato.", content = @Content)
    @ApiResponse(responseCode = "403", description = "Utente autenticato ma non autorizzato.", content = @Content)
    @ApiResponse(responseCode = "404", description = "Animale non trovato.", content = @Content)
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('VETERINARIAN', 'RECEPTIONIST')")
    public ResponseEntity<String> deleteAnimalById(@PathVariable int id) {
        service.deleteAnimalById(id);
        return ResponseEntity.ok("L'animale con id " + id + " è stato eliminato con successo.");
    }
}