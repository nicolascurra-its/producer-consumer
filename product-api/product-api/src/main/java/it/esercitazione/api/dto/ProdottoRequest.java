/**
 * DTO per l'aggiornamento di un Prodotto.
 *
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record ProdottoRequest(

        @NotNull(message = "Il nome del prodotto è obbligatorio.")
        @NotBlank(message = "Il nome del prodotto non può essere vuoto.")
        String name,
        
        @Past(message = "La data di creazione deve essere nel passato.")
        LocalDate dataCreazione,

        @NotNull(message = "Il prezzo del prodotto è obbligatorio.")
        @Positive(message = "Il prezzo deve essere maggiore di zero.")
        BigDecimal prezzo,

        @NotNull(message = "La categoria del prodotto è obbligatoria.")
        @Positive(message = "La categoria deve essere valida.")
        String categoria,

        @NotNull(message = "La quantità del prodotto è obbligatoria.")
        @Positive(message = "La quantità deve essere maggiore di zero.")
        Integer quantita,

        @NotNull(message = "L'id del prodotto è obbligatorio.")
        @Positive(message = "L'id deve essere valido.")
        Integer Id
) {}