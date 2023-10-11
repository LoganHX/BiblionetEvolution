package it.unisa.c07.biblionet.gestioneutenti.service;

import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
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

    private final BibliotecaService bibliotecaService;
    private final EspertoService espertoService;
    private final LettoreService lettoreService;


    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata ad un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    @Override
    public boolean isEmailRegistrata(final String email) {
        if (lettoreService.findLettoreByEmail(email) != null ||
        bibliotecaService.findBibliotecaByEmail(email) != null ||
        espertoService.findEspertoByEmail(email) != null)
            return true;

        return false;
    }




}
