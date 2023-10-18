package it.unisa.c07.biblionet.gestioneclubdellibro.service;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Lettore effettuaIscrizioneClub(final ClubDelLibro club,
                                          final Lettore lettore) {
        List<ClubDelLibro> listaClubs = lettore.getClubs();
        if (listaClubs == null) {
            listaClubs = new ArrayList<>();
        }
        listaClubs.add(club);
        lettore.setClubs(listaClubs);
        return aggiornaLettore(lettore);
        //return lettore;
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
    public Lettore abbandonaClub(final ClubDelLibro clubDelLibro,
                                 final Lettore lettore) {
        List<ClubDelLibro> listaClubs = lettore.getClubs();
        if (listaClubs == null || listaClubs.isEmpty()) {
            return lettore;
        }
        else if(!lettore.getClubs().contains(clubDelLibro)) return lettore;
        if(lettore.getClubs().contains(clubDelLibro)){
            lettore.getClubs().remove(clubDelLibro);
            lettoreDAO.save(lettore);
            return lettore;
        }
        return null;
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
    public List<LettoreDTO> getInformazioniLettori(List<Lettore> lettori) {
        List<LettoreDTO> DTOList = new ArrayList<>(lettori.size());

        return lettori.stream()
                .filter(Objects::nonNull)
                .map(LettoreDTO::new)
                .collect(Collectors.toCollection(() -> DTOList));
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
