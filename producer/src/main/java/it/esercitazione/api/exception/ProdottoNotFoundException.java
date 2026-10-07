package it.esercitazione.api.exception;

public class ProdottoNotFoundException extends RuntimeException {

    private final Long id;

    public ProdottoNotFoundException(Long id) {
        super("Prodotto non trovato");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
