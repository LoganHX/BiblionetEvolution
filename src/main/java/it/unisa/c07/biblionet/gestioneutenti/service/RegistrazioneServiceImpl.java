package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.gestioneutenti.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneutenti.repository.EspertoDAO;
import it.unisa.c07.biblionet.gestioneutenti.repository.LettoreDAO;
import it.unisa.c07.biblionet.entity.Biblioteca;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.entity.UtenteRegistrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta.
 */
@Service
@RequiredArgsConstructor
public class RegistrazioneServiceImpl implements RegistrazioneService {

    /**
     * Si occupa di gestire le operazioni CRUD dell'Esperto.
     */
    private final EspertoDAO espertoDAO;

    /**
     * Si occupa di gestire le operazioni CRUD della Biblioteca.
     */
    private final BibliotecaDAO bibliotecaDAO;

    /**
     * Si occupa delle operazioni CRUD.
     */
    private final LettoreDAO lettoreDAO;

    /**
     * Inject del service per simulare
     * le operazioni.
     */

    /**
     * Implementa la funzionalità di registrazione un Esperto.
     * @param esperto L'Esperto da registrare
     * @return L'utente registrato
     */
    @Override
    public final UtenteRegistrato registraEsperto(final Esperto esperto) {
        return espertoDAO.save(esperto);
    }

    /**
     * Implementa la funzionalità di registrazione una Biblioteca.
     * @param biblioteca La Biblioteca da registrare
     * @return L'utente registrato
     */
    @Override
    public UtenteRegistrato registraBiblioteca(final Biblioteca biblioteca) {
        return bibliotecaDAO.save(biblioteca);
    }

    /**
     * Implementa la funzionalità di registrare un Lettore.
     * @param lettore Il lettore da registrare
     * @return Il lettore registrato
     */
    @Override
    public final UtenteRegistrato registraLettore(final Lettore lettore) {
        return lettoreDAO.save(lettore);
    }

    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata ad un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    @Override
    public boolean isEmailRegistrata(final String email) {

       /*
        * Utilizzo il LettoreDAO, ma potrei usare qualsiasi altro DAO
        * degli utenti, poiché data la generalizzazione, la findAll
        * restituisce tutti gli utenti del sistema
        */
        for (UtenteRegistrato u: lettoreDAO.findAll()) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }





}
