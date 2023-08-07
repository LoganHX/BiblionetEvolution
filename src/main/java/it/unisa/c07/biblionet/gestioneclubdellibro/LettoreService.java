package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;

public interface LettoreService {
    Lettore findLettoreByEmail(String email);

    Boolean partecipaClub(ClubDelLibro club,
                          Lettore lettore);

    Boolean abbandonaClub(ClubDelLibro club,
                          Lettore lettore);

    Lettore aggiornaLettore(Lettore utente);

    Lettore findLettoreByEmailAndPassword(String email, byte[] password);


    Lettore creaLettoreDaModel(LettoreDTO form);

    Lettore aggiornaLettoreDaModel(LettoreDTO form);
}
