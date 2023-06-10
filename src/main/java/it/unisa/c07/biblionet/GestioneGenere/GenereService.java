package it.unisa.c07.biblionet.GestioneGenere;

import java.util.List;
import java.util.Set;

public interface GenereService {
    /**
     * Implementa la funzionalità di restituire tutti i generi
     * presenti nel database.
     * @return la lista di tutti i generi presenti nel database
     */
    Set<GenereDTO> getAllGeneri();

    /**
     * Implementa la funzionalità di restituire tutti i generi
     * data una lista di nomi di generi.
     * @param generi i generi da trovare
     * @return la lista di generi contenente solamente i generi effettivamente
     * presenti nel database
     */
    Set<GenereDTO> getGeneriByName(String[] generi);
}
