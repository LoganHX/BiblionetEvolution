package it.unisa.c07.biblionet.GestionePreferenzeDiLettura;

import it.unisa.c07.biblionet.GestioneGenere.GenereDTO;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;

import java.util.List;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
public interface PreferenzeDiLetturaService {


    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un esperto.
     * @param generi i generi da inserire
     * @param esperto l'esperto a cui inserirli
     */
    void addGeneriEsperto(Set<GenereDTO> generi, Esperto esperto);

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * ad un lettore.
     * @param generi i generi da inserire
     * @param lettore il lettore a cui inserirli
     */
    void addGeneriLettore(Set<GenereDTO> generi, Lettore lettore);
}
