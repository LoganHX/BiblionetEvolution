package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosistema ClubDelLibro.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Service
@RequiredArgsConstructor
public class ClubDelLibroServiceImpl implements ClubDelLibroService {

    @Override
    public List<Object> dettagliClub(List<ClubDelLibro> listaClubs){

        // Necessito di un oggetto anonimo per evitare problemi con JS
        return listaClubs.stream().map(club -> new Object() {
            public final String nome = club.getNome();
            public final String descrizione = club.getDescrizione();
            public final String nomeEsperto = club.getEsperto().getNome() + " " + club.getEsperto().getCognome();

            public final String immagineCopertina = club.getImmagineCopertina();
            public final Set <String> generi = club.getGeneri();
            public final int idClub = club.getIdClub();
            public final int iscritti = club.getLettori().size();
            public final String email = club.getEsperto().getEmail();
        }).collect(Collectors.toList());


    }

    /**
     * Si occupa delle operazioni CRUD per un club.
     */
    private final ClubDelLibroDAO clubDAO;


    /**
     * Implementa la funzionalità che permette
     * a un Esperto di creare un Club del Libro.
     *
     * @param clubDTO Il Club del Libro da memorizzare
     * @return Il Club del Libro appena creato
     */
    @Override
    public ClubDelLibro creaClubDelLibro(final ClubDTO clubDTO, Esperto esperto) throws IOException {

        return clubDAO.save(new ClubDelLibro(clubDTO, esperto));
    }

    /**
     * Implementa la funzionalità che permette
     * di visualizzare tutti i club del libro.
     *
     * @return La lista dei club
     */
    @Override
    public List<ClubDelLibro> visualizzaClubsDelLibro() {
        return this.visualizzaClubsDelLibro(x -> true);
    }

    /**
     * Implementa la funzionalità che permette
     * di filtrare tutti i club del libro.
     *
     * @param filtro Un predicato che descrive come filtrare i Club
     * @return La lista dei club
     */
    public List<ClubDelLibro> visualizzaClubsDelLibro(
            final Predicate<ClubDelLibro> filtro) {

        var clubs = this.clubDAO.findAll();

        return clubs.stream().filter(
                filtro
        ).collect(Collectors.toList());

    }


    /**
     * Implementa la funzionalità che permette
     * di modificare ed
     * effettuare l'update di un club.
     *
     * @param club Il club da modificare
     * @return Il club modificato
     */
    @Override
    public ClubDelLibro salvaClub(final ClubDelLibro club) {
        return clubDAO.save(club);
    }

    /**
     * Implementa la funzionalità che permette
     * di recuperare un
     * club dato il suo ID.
     *
     * @param id L'ID del club da recuperare
     * @return Il club recuperato
     */
    @Override
    public ClubDelLibro getClubByID(final int id) {
        Optional<ClubDelLibro> club = clubDAO.findById(id);
        return club.orElse(null);
    }



    /**
     * Funzione di utilità che permette di leggere la città
     * in cui si trova un Club del Libro.
     *
     * @param club il club da cui prendere la città
     * @return la città del club
     */
    @Override
    public String getCittaFromClubDelLibro(final ClubDelLibro club) {
        return club.getEsperto().getBiblioteca().getCitta();
    }


    /**
     * Restituisce tutte le citta nel sistema.
     *
     * @return Tutte le citta nel sistema
     */
    @Override
    public Set<String> getCitta() {
        return this.clubDAO.findAll().stream()
                .map(this::getCittaFromClubDelLibro)
                .collect(Collectors.toSet());
    }

    @Override
    public List<ClubDTO> getInformazioniClubs(List<ClubDelLibro> clubDelLibroList){
        List<ClubDTO> clubDTOS = new ArrayList<>();
        for(ClubDelLibro c: clubDelLibroList){
            clubDTOS.add(new ClubDTO(c));
        }
        return clubDTOS;
    }


}
