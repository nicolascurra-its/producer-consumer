package it.esercitazione.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Corpo delle richieste POST / PUT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdottoRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(max = 200, message = "Il nome non può superare 200 caratteri")
    private String nome;

    private String descrizione;

    @NotNull(message = "Il prezzo è obbligatorio")
    @DecimalMin(value = "0.00", message = "Il prezzo non può essere negativo")
    @Digits(integer = 8, fraction = 2, message = "Il prezzo deve avere al massimo 8 cifre intere e 2 decimali")
    private BigDecimal prezzo;

    @NotBlank(message = "La categoria è obbligatoria")
    @Size(max = 100, message = "La categoria non può superare 100 caratteri")
    private String categoria;

    @NotNull(message = "La quantità è obbligatoria")
    @Min(value = 0, message = "La quantità non può essere negativa")
    private Integer quantita;
}
