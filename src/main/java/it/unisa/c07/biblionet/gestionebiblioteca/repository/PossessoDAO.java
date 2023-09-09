package it.unisa.c07.biblionet.gestionebiblioteca.repository;

import it.unisa.c07.biblionet.common.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Possesso.
 */
@Repository
public interface PossessoDAO extends JpaRepository<Possesso, PossessoId> {

    /**
     * Query custom per il recuper dal DB di una lista dei possessi
     * di una determinata biblioteca identificata dall'ID.
     * @param bibliotecaID L'ID della biblioteca
     * @return La lista di possessi di quella biblioteca
     */
    @Query("SELECT p FROM Possesso p WHERE p.possessoID.bibliotecaID=?1")
    List<Possesso> findByBibliotecaID(String bibliotecaID);


    @Query("SELECT p FROM Possesso p WHERE p.possessoID.bibliotecaID=?1 AND p.possessoID.libroID=?2")
    Possesso findByBibliotecaIDAndLibroID(String bibliotecaID, int libroID);

    /**
     * Query custom per il recuper dal DB di una lista dei possessi
     * di un determinato libro entificato dall'ID.
     * @param libroID L'ID della biblioteca
     * @return La lista di possessi di quella biblioteca
     */
    @Query("SELECT p FROM Possesso p WHERE p.possessoID.libroID=?1")
    List<Possesso> findByLibroID(int libroID);

}
