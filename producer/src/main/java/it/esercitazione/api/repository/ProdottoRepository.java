package it.esercitazione.api.repository;

import it.esercitazione.api.model.Prodotto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    /** Filtro per categoria (case-insensitive). */
    List<Prodotto> findByCategoriaIgnoreCase(String categoria, Sort sort);

    /** Ricerca testuale sul nome: contiene il termine, senza distinzione maiuscole/minuscole. */
    List<Prodotto> findByNomeContainingIgnoreCase(String nome, Sort sort);

    /**
     * Query dedicata che combina i criteri: ogni parametro null viene ignorato.
     */
    @Query("""
            SELECT p FROM Prodotto p
            WHERE (:categoria IS NULL OR LOWER(p.categoria) = LOWER(:categoria))
              AND (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
            """)
    List<Prodotto> cerca(@Param("categoria") String categoria,
                         @Param("nome") String nome,
                         Sort sort);
}
