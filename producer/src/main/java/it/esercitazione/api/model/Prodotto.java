package it.esercitazione.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity associata alla tabella "prodotti".
 */
@Entity
@Table(name = "prodotti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "prezzo", nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    @Column(name = "data_creazione", nullable = false, updatable = false)
    private LocalDateTime dataCreazione;

    @PrePersist
    void impostaDataCreazione() {
        if (dataCreazione == null) {
            dataCreazione = LocalDateTime.now();
        }
    }
}
