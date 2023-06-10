package it.unisa.c07.biblionet.entity;

import java.util.Set;

/**
 * Questa è un interfaccia contenente un solo metodo
 * utilizzato per ricevere i generi di un utente.
 */
public interface HaGenere {

    /**
     * Implementa la funzionalità di restituzione dei generi da parte
     * delle entità che li possiedono.
     *
     * @return la lista di generi
     */
    Set<String> getGeneri();
}
