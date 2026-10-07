/**
 * Gestione centralizzata delle eccezioni a livello MVC.
 *
 * @author Tommaso Fatticcioni
 * @since 26/06/2026
 */

package it.esercitazione.api.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Costruisce la risposta d'errore standard.
     *
     * @param status lo stato HTTP da restituire.
     * @param message il messaggio descrittivo.
     * @param request la richiesta corrente (per ricavare la rotta).
     * @return la {@link ResponseEntity} con il corpo {@link ErrorResponse}.
     */
    private ResponseEntity<ErrorResponse> buildResponse(
        HttpStatus status, String message, HttpServletRequest request
    ) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * Violazione di una regola di dominio, es. date incoerenti o orari non validi
     * -> 400 Bad Request.
     *
     * @param exception l'eccezione sollevata dal service.
     * @param request la richiesta corrente.
     * @return risposta 400 con {@link ErrorResponse}.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
        IllegalArgumentException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    /**
     * Risorsa in conflitto con una già esistente, es. email o nome ruolo duplicati
     * -> 409 Conflict.
     *
     * @param exception l'eccezione sollevata dal service.
     * @param request la richiesta corrente.
     * @return risposta 409 con {@link ErrorResponse}.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(
        DuplicateResourceException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    /**
     * Risorsa non trovata -> 404 Not Found.
     *
     * @param exception l'eccezione sollevata dal service.
     * @param request la richiesta corrente.
     * @return risposta 404 con {@link ErrorResponse}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    /**
     * Credenziali di login errate -> 401 Unauthorized.
     *
     * @param exception l'eccezione di credenziali non valide.
     * @param request la richiesta corrente.
     * @return risposta 401 con {@link ErrorResponse}.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials (
        BadCredentialsException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenziali non valide.", request);
    }

    /**
     * Stato del server non valido, es. nessun ruolo di default configurato
     *
     * @param exception l'eccezione di stato non valido.
     * @param request la richiesta corrente.
     * @return risposta 500 con {@link ErrorResponse}.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
        IllegalStateException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    /**
     * Token di reset password inesistente, scaduto o già usato -> 400 Bad Request.
     *
     * @param exception l'eccezione sollevata dal service.
     * @param request la richiesta corrente.
     * @return risposta 400 con {@link ErrorResponse}.
     */
    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResetToken(
        InvalidPasswordResetTokenException exception, HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    /**
     * Corpo della richiesta non valido (violazioni di Bean Validation) -> 400 Bad Request.
     * Raccoglie i messaggi di errore dei singoli campi in un unico testo.
     *
     * @param exception l'eccezione di validazione sollevata da @Valid.
     * @param request la richiesta corrente.
     * @return risposta 400 con {@link ErrorResponse}.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        String message = exception.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Violazione di un vincolo del database (unicità o chiave esterna) -> 409 Conflict.
     *
     * @param exception l'eccezione sollevata dal layer di persistenza.
     * @param request la richiesta corrente.
     * @return risposta 409 con {@link ErrorResponse}.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
        DataIntegrityViolationException exception, HttpServletRequest request
    ) {
        return buildResponse(
            HttpStatus.CONFLICT,
            "Operazione non consentita: viola un vincolo del database (valore duplicato o risorsa ancora referenziata).",
            request
        );
    }


}