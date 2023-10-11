package it.unisa.c07.biblionet.gestionebiblioteca;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class BibliotecaDTO extends UtenteRegistratoDTO {

    /**
     * Rappresenta l'ID di un utente registrato.
     */
    @NonNull
    @NotNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    @Size(min = BiblionetConstraints.LUNGHEZZA_MINIMA_NOME, max = BiblionetConstraints.LUNGHEZZA_MASSIMA_NOME, message = "Il campo 'nome' può avere al massimo 50 caratteri")
    @Column(length = BiblionetConstraints.LUNGHEZZA_MASSIMA_NOME)
    private String nomeBiblioteca;
    /**
     * Rappresenta la password di un utente registrato.
     */



    public BibliotecaDTO(@NonNull String email, @NonNull String password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nomeBiblioteca) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.nomeBiblioteca = nomeBiblioteca;
    }
    public BibliotecaDTO(@NonNull String email, @NonNull byte[] password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nomeBiblioteca) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.nomeBiblioteca = nomeBiblioteca;
    }
    public BibliotecaDTO(Biblioteca b){
        super(b.getEmail(), b.getPassword(), b.getProvincia(), b.getCitta(), b.getVia(), b.getRecapitoTelefonico());
        this.nomeBiblioteca = b.getNomeBiblioteca();
    }
}
