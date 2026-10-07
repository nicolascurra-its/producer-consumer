package it.esercitazione.client.exception;

/** Errore generico restituito dalla Producer API. */
public class ApiException extends RuntimeException {

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(String message) {
        super(message);
    }
}
