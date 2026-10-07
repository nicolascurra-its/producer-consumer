/**DTO per il trasferimento dei dati dell'entity `Prodotto` tra il backend e il frontend.
 * 
 * @author Nicolas Currà
 * @since 01/10/2026
 */

package it.esercitazione.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import it.esercitazione.api.model.Prodotto;

public record ProdottoResponse(
	int id,
	String nome,
    String descrizione,
    BigDecimal prezzo,
    String categoria,
    int quantita,
    LocalDate dataCreazione
) {

	public static ProdottoResponse fromProdotto(Prodotto prodotto) {
		return new ProdottoResponse(
			prodotto.getId(),
			prodotto.getNome(),
			prodotto.getDescrizione(),
			prodotto.getPrezzo(),
			prodotto.getCategoria(),
			prodotto.getQuantita(),
			prodotto.getDataCreazione()
		);
	}

}