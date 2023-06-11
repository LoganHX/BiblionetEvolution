package it.unisa.c07.biblionet.gestionepreferenzedilettura;

import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.gestionepreferenzedilettura.repository.Genere;
import it.unisa.c07.biblionet.entity.Lettore;

import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
public interface PreferenzeDiLetturaService {


    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * a un esperto.
     * @param generi i generi da inserire
     * @param esperto l'esperto a cui inserirli
     */
    void addGeneriEsperto(Set<Genere> generi, Esperto esperto);

    /**
     * Implementa la funzionalità di aggiungere una lista di generi
     * a un lettore.
     * @param generi i generi da inserire
     * @param lettore il lettore a cui inserirli
     */
    void addGeneriLettore(Set<Genere> generi, Lettore lettore);
}
