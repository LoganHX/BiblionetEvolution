package it.unisa.c07.biblionet.GestionePrestitiLibro.service.bookApiAdapter;

import it.unisa.c07.biblionet.entity.LibroBiblioteca;
import it.unisa.c07.biblionet.entity.LibroEvento;

/**
 * Rappresenta l'interfaccia dello Adapter usata
 * dalle classi di BiblioNet per la ricerca di un
 * libro, tramite ISBN, attraverso l'uso di API esterne.
 */
public interface BookApiAdapter {
    /**
     * Implementa la funzionalità che permette
     * di recuperare un Libro dal web tramite
     * chiamata http ad un api di google con l'isbn.
     * @param isbn l'isbn del libro
     * @return il libro
     */
    LibroBiblioteca getLibroDaBookApi(String isbn);
}
