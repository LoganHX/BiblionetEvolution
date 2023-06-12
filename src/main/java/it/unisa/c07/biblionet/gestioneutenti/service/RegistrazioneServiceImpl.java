package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.common.UtenteRegistratoDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneprestitilibro.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneprestitilibro.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta.
 */
@Service
@RequiredArgsConstructor
public class RegistrazioneServiceImpl implements RegistrazioneService {

    private final PrenotazioneLibriService prenotazioneLibriService;
    private final ClubDelLibroService clubDelLibroService;
    /**
     * Si occupa delle operazioni CRUD.
     */
    private final UtenteRegistratoDAO utenteRegistratoDAO;

    /**
     * Implementa la funzionalità di registrare un Lettore.
     * @param lettore Il lettore da registrare
     * @return Il lettore registrato
     */
    @Override
    public final UtenteRegistrato registraLettore(final LettoreDTO lettore) {
        return clubDelLibroService.creaLettoreDaModel(lettore);
    }

    /**
     * Implementa la funzionalità di registrazione un Esperto.
     * @param esperto L'Esperto da registrare
     * @return L'utente registrato
     */
    @Override
    public final UtenteRegistrato registraEsperto(final EspertoDTO esperto, final UtenteRegistrato biblioteca) {
        return clubDelLibroService.creaEspertoDaModel(esperto, biblioteca);
    }



    /**
     * Implementa la funzionalità di registrazione una Biblioteca.
     * @param biblioteca La Biblioteca da registrare
     * @return L'utente registrato
     */
    @Override
    public UtenteRegistrato registraBiblioteca(final BibliotecaDTO biblioteca, String nomeBiblioteca, String password) {
        return prenotazioneLibriService.creaBibliotecaDaModel(biblioteca, nomeBiblioteca, password);
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
        for (UtenteRegistrato u: utenteRegistratoDAO.findAll()) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }




}
