package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Questa classe rappresenta il DAO di un Evento.
 */
@Repository
public interface EventoDAO extends JpaRepository<Evento, Integer> {
    @Query("SELECT e FROM Evento e JOIN e.lettori l WHERE e.idEvento=?1 and l.email=?2")
    Evento isLettoreIscrittoEvento(int idEventi, String emailLettore);

}
