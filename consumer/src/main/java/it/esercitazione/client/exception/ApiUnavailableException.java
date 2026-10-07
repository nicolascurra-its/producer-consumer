package it.esercitazione.client.exception;

/** La Producer API non e' raggiungibile (connessione rifiutata, timeout, ...). */
public class ApiUnavailableException extends ApiException {

    public ApiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
