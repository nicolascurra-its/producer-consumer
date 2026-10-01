/**
 * Il service per l'entity `Animal`.
 * Implementa le operazioni di lettura, creazione, aggiornamento (parziale e di
 * stato) ed eliminazione, risolvendo le entità collegate a partire dai loro id.
 *
 * @author Ismail Perta, Samuele Querio, Tommaso Fatticcioni
 * @since 29/06/2026
 */

package it.esercitazione.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import it.aftermath.dto.animal.AnimalRequest;
import it.aftermath.dto.animal.AnimalResponse;
import it.aftermath.dto.animal.CreateAnimalRequest;
import it.aftermath.dto.common.NotesRequest;
import it.aftermath.entity.Animal;
import it.aftermath.entity.Race;
import it.aftermath.entity.Registry;
import it.aftermath.entity.Sex;
import it.aftermath.entity.Species;
import it.aftermath.exception.ResourceNotFoundException;
import it.aftermath.repository.AnimalRepository;
import it.aftermath.repository.RaceRepository;
import it.aftermath.repository.RegistryRepository;
import it.aftermath.repository.SexRepository;
import it.aftermath.repository.SpeciesRepository;
import it.aftermath.service.interfaces.AnimalService;

@Service
public class ProdottoService implements AnimalService {

    private final ProdottoRepository prodottoRepository;

    public AnimalServiceImplementation(
        ProdotoRepository prodottoRepository
    ) {
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * Recupera tutti i prodotti registrati.
     *
     * @return la lista di tutti i prodotti, eventualmente vuota.
     */
    @Override
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
    @Override
    public ProdottoResponse getById(int id) {
        return ProdottoResponse.fromProdotto(findProdottoOrThrow(id));
    }

    /**
     * Recupera tutti i prodotti dato il loro prezzo.
     *
     * @param prezzo il prezzo del prodotto.
     * @return la lista dei prodotti, eventualmente vuota.
     */
    @Override
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
    @Override
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
    @Override
    public List<ProdottoResponse> getAllByQuantita(int quantita) {
        return animalRepository.findAllByQuantita(quantita)
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
    @Override
    public List<ProdottoResponse> getAllByDataCreazione(LocalDate dataCreazione) {
        return animalRepository.findAllByDataCreazione(dataCreazione)
                .stream()
                .map(ProdottoResponse::fromProdotto)
                .toList();
    }
//-------------------------------------------------------------------------------------------
    /**
     * Aggiunge un nuovo Prodotto al database.
     * I campi sono già validati da Bean Validation nel controller; qui vengono
     * risolte le entità collegate a partire dai rispettivi id.
     *
     * @param request i dati validati dell'animale da creare.
     * @return l'`AnimalResponse` dell'animale appena creato.
     * @throws ResourceNotFoundException se sesso, specie, razza o anagrafica non esistono.
     */
    @Override
    public AnimalResponse addAnimal(CreateAnimalRequest request) {
        Animal animal = new Animal();
        animal.setName(request.name());
        animal.setBirthDate(request.birthDate());
        animal.setWeight(request.weight());
        animal.setMicrochip(request.microchip());
        animal.setSex(findSexOrThrow(request.sexId()));
        animal.setSpecies(findSpeciesOrThrow(request.speciesId()));
        animal.setRace(findRaceOrThrow(request.raceId()));
        animal.setRegistry(findRegistryOrThrow(request.registryId()));
        animal.setDeleted(false);

        return AnimalResponse.fromAnimal(animalRepository.save(animal));
    }

    /**
     * Aggiorna in modo parziale i dati anagrafici di un animale.
     * Vengono modificati solo i campi valorizzati nella richiesta; i campi
     * {@code null} lasciano invariato il valore esistente. Note e stato non
     * sono modificabili da questa operazione.
     *
     * @param id l'id dell'animale da aggiornare.
     * @param animalRequest i dati da aggiornare (campi opzionali).
     * @return l'`AnimalResponse` dell'animale aggiornato.
     * @throws ResourceNotFoundException se l'animale o una delle entità collegate indicate non esistono.
     */
    @Override
    public AnimalResponse updateAnimal(int id, AnimalRequest animalRequest) {
        Animal animal = findAnimalOrThrow(id);

        if (animalRequest.name() != null)
            animal.setName(animalRequest.name());
        
        if (animalRequest.birthDate() != null)
            animal.setBirthDate(animalRequest.birthDate());
        
        if (animalRequest.weight() != null)
            animal.setWeight(animalRequest.weight());
        
        if (animalRequest.microchip() != null)
            animal.setMicrochip(animalRequest.microchip());
        
        if (animalRequest.sexId() != null)
            animal.setSex(findSexOrThrow(animalRequest.sexId()));
        
        if (animalRequest.speciesId() != null)
            animal.setSpecies(findSpeciesOrThrow(animalRequest.speciesId()));
        
        if (animalRequest.raceId() != null)
            animal.setRace(findRaceOrThrow(animalRequest.raceId()));
        
        if (animalRequest.registryId() != null)
            animal.setRegistry(findRegistryOrThrow(animalRequest.registryId()));
        

        return AnimalResponse.fromAnimal(animalRepository.save(animal));
    }

    /**
     * Aggiorna le note di un animale.
     *
     * @param id           l'id dell'animale.
     * @param notesRequest le nuove note.
     * @return l'`AnimalResponse` dell'animale aggiornato.
     * @throws ResourceNotFoundException se l'animale non esiste.
     */
    @Override
    public AnimalResponse updateNotes(int id, NotesRequest notesRequest) {
        Animal animal = findAnimalOrThrow(id);
        animal.setNotes(notesRequest.notes());
        return AnimalResponse.fromAnimal(animalRepository.save(animal));
    }

    /**
     * Eliminazione logica di un animale dal database.
     *
     * @param id l'id dell'animale da eliminare.
     * @throws ResourceNotFoundException se l'animale non esiste.
     */
    @Override
    public void deleteAnimalById(int id) {
        Animal animal = findAnimalOrThrow(id);
        animal.setDeleted(true);
        animalRepository.save(animal);
    }

    private Animal findAnimalOrThrow(int id) {
        return animalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Animale non trovato con id " + id + "."));
    }

    private Sex findSexOrThrow(int id) {
        return sexRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sesso non trovato con id " + id + "."));
    }

    private Species findSpeciesOrThrow(int id) {
        return speciesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Specie non trovata con id " + id + "."));
    }

    private Race findRaceOrThrow(int id) {
        return raceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Razza non trovata con id " + id + "."));
    }

    private Registry findRegistryOrThrow(int id) {
        return registryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Anagrafica non trovata con id " + id + "."));
    }

}