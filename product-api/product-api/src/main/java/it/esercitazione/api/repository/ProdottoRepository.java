/**
 * Interfaccia che definisce la repository dell'entity `Prodotto`.
 * I metodi principali sono definiti da `JpaRepository`.
 * 
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import it.aftermath.entity.Prodotto;

/**
 * Utilizzo:
 * extends JpaRepository<Entity, TipoChiavePrimaria>
 */
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer, String, LocalDate> {

	List<Prodotto> findAllById(int id);

	List<Prodotto> findAllByName(String nome);

	List<Prodotto> findAllByCategory(String categoria);

	List<Prodotto> findAllByCreation_Date(LocalDate dataCreazine);

}