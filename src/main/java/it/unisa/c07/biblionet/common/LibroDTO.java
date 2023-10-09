package it.unisa.c07.biblionet.common;

import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.RegEx;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LibroDTO {

    private int id;
    /**
     * Rappresenta il titolo di un libro.
     */
    @NonNull
    @NotNull
    @Column(length = BiblionetConstraints.LENGTH_90)
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LENGTH_90)
    private String titolo;

    /**
     * Rappresenta l'autore di un libro.
     */
    @NonNull
    @NotNull
    @Column(length = BiblionetConstraints.LENGTH_90)
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LENGTH_90)
    private String autore;

    /**
     * Rappresenta il codice ISBN di un libro se presente.
     */
    @Column(unique = true, length = BiblionetConstraints.LENGTH_13)
    @NonNull
    @NotNull
    @Pattern(regexp = BiblionetConstraints.ISBN_REGEX)
    private String isbn;

    /**
     * Rappresenta l'anno di pubblicazione di un libro.
     */
    @Column
    @NotNull
    @Pattern(regexp = BiblionetConstraints.YEAR_REGEX)
    private String annoDiPubblicazione;

    /**
     * Rappresenta la descrizione di un libro.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_144)
    @NotNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LENGTH_144)
    private String descrizione;

    /**
     * Rappresenta la casa editrice di un libro.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    @NonNull
    @NotNull
    @Size(min= BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LENGTH_30)
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

    public LibroDTO(Libro l){
        this.id = l.getIdLibro();
        this.annoDiPubblicazione = l.getAnnoDiPubblicazione();
        this.generi = l.getGeneri();
        this.immagineLibro = l.getImmagineLibro();
        this.titolo = l.getTitolo();
        this.autore = l.getAutore();
        this.casaEditrice = l.getCasaEditrice();
        this.isbn = l.getIsbn();
    }
}
