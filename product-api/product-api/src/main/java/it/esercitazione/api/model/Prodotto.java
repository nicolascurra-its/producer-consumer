/**
 * File Entity per la tabella `Prodotto`.
 * 
 * @author Nicolas Currà
 * @since  29/09/2026
 */

package it.esercitazione.api.model;

import java.time.LocalDate;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "animals")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione", nullable = true)
    private String descrizione;

    @Column(name = "prezzo", nullable = false)
    private BigDecimal prezzo;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "quantita", nullable = false)
    private int quantita;

    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;
}