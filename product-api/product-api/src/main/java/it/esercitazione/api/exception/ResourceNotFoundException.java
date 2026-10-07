/**
 * Eccezione sollevata quando una risorsa richiesta non esiste.
 * Mappata su HTTP 404 dal GlobalExceptionHandler.
 *
 * @author Nicolas Currà
 * @since 07/10/2026
 */

package it.esercitazione.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }

}