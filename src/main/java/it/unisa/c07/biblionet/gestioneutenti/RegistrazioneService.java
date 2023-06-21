
package it.unisa.c07.biblionet.gestioneutenti;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta.
 */
public interface RegistrazioneService {

    UtenteRegistrato aggiornaEsperto(EspertoDTO esperto, String emailBiblioteca);

    UtenteRegistrato aggiornaLettore(LettoreDTO lettore);


    /**
     * Implementa la funzionalità di controllare se una mail è
     * presente già associata a
     * un altro utente nel database.
     * @param email la mail da controllare
     * @return true se la mail è già associata, false altrimenti
     */
    boolean isEmailRegistrata(String email);

}
