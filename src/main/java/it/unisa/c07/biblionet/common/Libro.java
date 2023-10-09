package it.unisa.c07.biblionet.common;

import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import javax.persistence.*;
import java.util.Set;


//todo lo sto usando come un DTO

/**
 * Questa classe rappresenta un libro.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Libro {

    /**
     * Rappresenta l'ID autogenerato di un libro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idLibro;

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
    @Column
    private String annoDiPubblicazione;

    /**
     * Rappresenta la descrizione di un libro.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_144)
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

    public Libro(LibroDTO dto){
        this.titolo = dto.getTitolo();
        this.autore = dto.getAutore();
        this.isbn = dto.getIsbn();
        this.annoDiPubblicazione = dto.getAnnoDiPubblicazione();
        this.descrizione = dto.getDescrizione();
        this.casaEditrice = dto.getCasaEditrice();
        this.generi = dto.getGeneri();
        this.immagineLibro = dto.getImmagineLibro();
    }
}
