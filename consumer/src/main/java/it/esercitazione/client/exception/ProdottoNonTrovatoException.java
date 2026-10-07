package it.esercitazione.client.exception;

/** La Producer API ha risposto 404 per il prodotto richiesto. */
public class ProdottoNonTrovatoException extends ApiException {

    public ProdottoNonTrovatoException(Long id, Throwable cause) {
        super("Il prodotto con id " + id + " non esiste.", cause);
    }
}
