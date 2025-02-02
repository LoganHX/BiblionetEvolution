package it.unisa.c07.biblionet.gestionebiblioteca.repository;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Questa classe rappresenta una Biblioteca.
 * Una Biblioteca possiede un nome, la lista degli esperti
 * che lavorano presso di essa, la lista di libri che possiede
 * che quindi può prestare a un lettore,
 * e una lista di ticket che rappresentano le richieste di prestito dei lettori.
 */
@Entity
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class Biblioteca extends UtenteRegistrato {


    /**
     * Rappresenta il nome della biblioteca.
     */
    @Column(nullable = false, length = BiblionetConstraints.LENGTH_60)
    @NonNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String nomeBiblioteca;

    /**
     * Rappresenta la lista di ticket riguardanti le richieste di prestito.
     */
    @OneToMany
    @ToString.Exclude
    private List<TicketPrestito> tickets;

    /**
     * Rappresenta la lista di libri posseduti dalla biblioteca.
     */
    @OneToMany(mappedBy = "possessoID.bibliotecaID")
    @ToString.Exclude
    private List<Possesso> possessi;



    /**
     *
     * @param email È la mail della biblioteca.
     * @param password È la password di accesso della biblioteca.
     * @param provincia È la provincia in cui ha sede la biblioteca.
     * @param citta È la città in cui ha sede la biblioteca.
     * @param via È l'indirizzo in cui ha sede la biblioteca.
     * @param recapitoTelefonico È il numero di telefono della biblioteca.
     * @param nomeBiblioteca È il nome della biblioteca.
     */
    public Biblioteca(final String email, final String password,
                      final String provincia, final String citta,
                      final String via, final String recapitoTelefonico,
                      final String nomeBiblioteca) {

        super(email, password, provincia, citta, via, recapitoTelefonico, "Biblioteca");
        this.nomeBiblioteca = nomeBiblioteca;
    }

    public Biblioteca(final BibliotecaDTO dto) {
        super(dto.getEmail(),dto.getPassword(), dto.getProvincia(), dto.getCitta(), dto.getVia(), dto.getRecapitoTelefonico(), "Biblioteca");
        this.nomeBiblioteca = dto.getNomeBiblioteca();
    }


}
