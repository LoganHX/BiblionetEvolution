package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.gestionebiblioteca.LibroBibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.LibroBiblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LibroEvento;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LibroEventoDTO {

    /**
     * Rappresenta il titolo di un libro.
     */
    @NonNull
    @Column(length = BiblionetConstraints.LENGTH_90)
    private String titolo;

    /**
     * Rappresenta l'autore di un libro.
     */
    @NonNull
    @Column(length = BiblionetConstraints.LENGTH_60)
    private String autore;

    /**
     * Rappresenta il codice ISBN di un libro se presente.
     */
    @Column(unique = true, length = BiblionetConstraints.LENGTH_13)
    @NonNull
    private String isbn;

    /**
     * Rappresenta l'anno di pubblicazione di un libro.
     */
    @Column(nullable = false)
    @NonNull
    private LocalDateTime annoDiPubblicazione;

    /**
     * Rappresenta la descrizione di un libro.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_144)
    @NonNull
    private String descrizione;

    /**
     * Rappresenta la casa editrice di un libro.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @NonNull
    private String casaEditrice;

    /**
     * Rappresenta l'immagine di copertina del libro.
     */
    @Lob
    @ToString.Exclude
    private String immagineLibro;


    /**
     * Rappresenta i generi di un libro.
     */
    @ElementCollection
    @ToString.Exclude
    private Set<String> generi;

    public LibroEventoDTO(LibroEvento libroEvento){
        this.annoDiPubblicazione = libroEvento.getAnnoDiPubblicazione();
        this.generi = libroEvento.getGeneri();
        this.immagineLibro = libroEvento.getImmagineLibro();
        this.titolo = libroEvento.getTitolo();
        this.autore = libroEvento.getAutore();
        this.casaEditrice = libroEvento.getCasaEditrice();
        this.isbn = libroEvento.getIsbn();
    }


}
