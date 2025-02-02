package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementa la classe che esplicita i metodi
 * definiti nell'interfaccia service per il
 * sottosistema Autenticazione.
 *
 * @author Ciro Maiorino, Giulio Triggiani
 */
@Service
@RequiredArgsConstructor
public class AutenticazioneServiceImpl implements AutenticazioneService {

    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final BibliotecaService bibliotecaService;

    /**
     * I.
     */


    /**
     * Implementa la funzionalità di login
     * per un Utente registrato.
     *
     * @param email    dell'utente.
     * @param password dell'utente.
     * @return un utente registrato.
     */
    @Override
    public UtenteRegistrato login(final String email, final String password) {

        byte[] arr = BiblionetConstraints.trasformaPassword(password);
        UtenteRegistrato u;

        if ((u = lettoreService.findLettoreByEmailAndPassword(email, arr)) != null) {
            return u;
        }
        else if ((u = bibliotecaService.findBibliotecaByEmailAndPassword(email, arr)) != null) {
            return u;
        } else {
            u = espertoService.findEspertoByEmailAndPassword(email, arr);
            return u;
        }

    }





}