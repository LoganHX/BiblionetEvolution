package it.unisa.c07.biblionet.gestioneutenti.service;

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

    private final PrenotazioneLibriService prenotazioneLibriService;
    private final EspertoService espertoService;
    private final LettoreService lettoreService;




    @Override
    public final UtenteRegistrato aggiornaEsperto(final EspertoDTO esperto, final String emailBiblioteca) {
        return espertoService.aggiornaEspertoDaModel(esperto, prenotazioneLibriService.findBibliotecaByEmail(emailBiblioteca));
        //todo controllare se sono inutili
    }

    @Override
    public final UtenteRegistrato aggiornaLettore(final LettoreDTO lettore) {
        return lettoreService.aggiornaLettoreDaModel(lettore);
        //todo controllare se inutile
    }


    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata ad un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    @Override
    public boolean isEmailRegistrata(final String email) {
        if (lettoreService.findLettoreByEmail(email) != null ||
        prenotazioneLibriService.findBibliotecaByEmail(email) != null ||
        espertoService.findEspertoByEmail(email) != null)
            return true;

        return false;
    }




}
