/**
 * Interfaccia che definisce la repository dell'entity `Prodotto`.
 * I metodi principali sono definiti da `JpaRepository`.
 * 
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import it.esercitazione.api.model.Prodotto;

/**
 * Utilizzo:
 * extends JpaRepository<Entity, TipoChiavePrimaria>
 */
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {

	List<Prodotto> findAllById(int id);

	List<Prodotto> findAllByName(String nome);

	List<Prodotto> findAllByCategoria(String categoria);

	List<Prodotto> findAllByDataCreazione(LocalDate dataCreazione);

	List<Prodotto> findAllByPrezzo(java.math.BigDecimal prezzo);
	
	List<Prodotto> findAllByQuantita(int quantita);

}