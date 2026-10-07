package it.esercitazione.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Rappresentazione di un prodotto esposta dalla REST API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private String categoria;
    private Integer quantita;
    private LocalDateTime dataCreazione;
}
