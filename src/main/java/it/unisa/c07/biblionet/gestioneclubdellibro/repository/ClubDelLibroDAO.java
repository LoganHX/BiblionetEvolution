package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Club del Libro.
 */
@Repository
public interface ClubDelLibroDAO extends JpaRepository<ClubDelLibro, Integer> {

    /**
     * Implementa la funzionalità di prendere una lista di club
     * del libro a cui un lettore partecipa.
     * @param lettore il lettore preso in esame
     * @return la lista dei club del libro a cui partecipa
     */
    List<ClubDelLibro> findAllByLettori(Lettore lettore);

    /**
     * Implementa la funzionalità di prendere una lista di club
     * del libro di cui un esperto è proprietario.
     * @param esperto l' esperto preso in esame
     * @return la lista dei club del libro a cui partecipa
     */
    List<ClubDelLibro> findAllByEsperto(Esperto esperto);

    @Query("SELECT c FROM ClubDelLibro c JOIN c.lettori l WHERE l.email=?1 and c.idClub=?2")
    ClubDelLibro isLettoreIscrittoAClub(String email, int idClub);



}
