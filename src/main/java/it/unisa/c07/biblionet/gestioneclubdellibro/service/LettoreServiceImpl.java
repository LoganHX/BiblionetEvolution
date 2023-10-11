package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class LettoreServiceImpl implements LettoreService {

    private final LettoreDAO lettoreDAO;

    /**
     * Implementa la funzionalità di trovare un lettore.
     *
     * @param email La mail dell lettore
     * @return Il lettore se c'è, altrimenti null
     */
    @Override
    public final Lettore findLettoreByEmail(final String email) {
        return lettoreDAO.findLettoreByEmail(email, "Lettore");
    }

    /**
     * Implementa la funzionalità che permette
     * a un lettore di effettuare
     * l'iscrizione a un club del libro.
     *
     * @param club    Il club al quale iscriversi
     * @param lettore Il lettore che si iscrive
     * @return true se è andato a buon fine, false altrimenti
     */
    @Override
    public Boolean partecipaClub(final ClubDelLibro club,
                                 final Lettore lettore) {
        List<ClubDelLibro> listaClubs = lettore.getClubs();
        if (listaClubs == null) {
            listaClubs = new ArrayList<>();
        }
        listaClubs.add(club);
        lettore.setClubs(listaClubs);
        aggiornaLettore(lettore);
        return true;
    }

    /**
     * Implementa la funzionalità che permette
     * a un lettore di effettuare
     * l'iscrizione a un club del libro.
     *
     * @param clubDelLibro    Il club dal quale de-iscriversi
     * @param lettore Il lettore che si disiscrive
     * @return true se è andato a buon fine, false altrimenti
     */
    @Override
    public Boolean abbandonaClub(final ClubDelLibro clubDelLibro,
                                 final Lettore lettore) {
        List<ClubDelLibro> listaClubs = lettore.getClubs();
        if (listaClubs == null || listaClubs.isEmpty()) {
            return true;
        }
        else if(!lettore.getClubs().contains(clubDelLibro)) return true;
        if(lettore.getClubs().contains(clubDelLibro)){
            lettore.getClubs().remove(clubDelLibro);
            lettoreDAO.save(lettore);
            return true;
        }
        return false;
    }


    @Override
    public Lettore aggiornaLettore(final Lettore utente) {
        return lettoreDAO.save(utente);
    }



    @Override
    public Lettore findLettoreByEmailAndPassword(String email, byte[] password) {
        return lettoreDAO.findByEmailAndPassword(email, password);
    }






    @Override
    public Lettore creaLettoreDaModel(LettoreDTO form) {
        return lettoreDAO.save(new Lettore(form));
    }

    @Override
    public Lettore aggiornaLettoreDaModel(LettoreDTO form) {
        return creaLettoreDaModel(form);
    }
}
