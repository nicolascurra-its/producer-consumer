/**
 * DTO per l'aggiornamento parziale di un Prodotto.
 *
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record ProdottoRequest(

        String name,

        @Past(message = "La data di creazione deve essere nel passato.")
        LocalDate dataCreazione,

        @Positive(message = "Il prezzo deve essere maggiore di zero.")
        BigDecimal prezzo,

        @Positive(message = "La categoria deve essere valida.")
        String categoria,

        @Positive(message = "La quantità deve essere maggiore di zero.")
        Integer quantita,

        @Positive(message = "L'id deve essere valido.")
        Integer Id
) {}