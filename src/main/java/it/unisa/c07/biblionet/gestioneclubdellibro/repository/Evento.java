package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.GenerationType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  Questa classe rappresenta un evento relativo a un club del libro.
 *  Un evento ha un id autogenerato, un nome, una descrzione, la data e l'ora
 *  di svolgimento, ed eventualmente un libro.
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Evento {

    /**
     * Rappresenta l'ID di un evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idEvento;

    /**
     * Rappresenta l'ID di un evento.
     */
    @NonNull
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_30)
    private String nomeEvento;

    /**
     * Rappresenta la descrizione di un evento.
     */
    @NonNull
    @Column(nullable = false)
    private String descrizione;

    /**
     * Rappresenta la data e ora di svolgimento di un evento.
     */
    @NonNull
    private LocalDateTime dataOra;

    /**
     * Rappresenta i lettori che partecipano ad un evento.
     */
    @ManyToMany(mappedBy = "eventi")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Lettore> lettori;

    /**
     * Rappresenta il club dove si svolge un evento.
     */
    @NonNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClubDelLibro club;

    /**
     * Rappresenta il libro opzionale di un evento.
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Libro libro;

    @Override
    public boolean equals(final Object obj) throws NullPointerException {
        if (obj.getClass().equals(this.getClass())) {
            Evento evento = (Evento) obj;
            return this.idEvento == evento.getIdEvento();
        }
        return false;
    }

}
