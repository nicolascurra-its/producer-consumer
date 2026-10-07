package it.esercitazione.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modello lato client: converte il JSON ricevuto dalla Producer API.
 * E' volutamente separato dall'Entity JPA del Producer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdottoDTO {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
    private String categoria;
    private Integer quantita;
    private LocalDateTime dataCreazione;
}
