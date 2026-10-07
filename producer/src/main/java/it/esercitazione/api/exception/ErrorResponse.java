package it.esercitazione.api.exception;

import java.time.LocalDateTime;

/**
 * Corpo JSON restituito in caso di errore.
 */
public record ErrorResponse(int status, String message, LocalDateTime timestamp) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }
}
