package it.unisa.c07.biblionet.gestioneclubdellibro;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
public class EspertoDTO extends UtenteRegistratoDTO {
    /**
     * Rappresenta l'ID di un utente registrato.
     */


    private Set<String> generi;

    @NotNull
    @NonNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String nome;
    @NotNull
    @NonNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String cognome;
    @NotNull
    @Size(max = 30, message = "Il campo 'nome' può avere al massimo 50 caratteri")
    private String username;
    @NotNull
    @NonNull
    private String emailBiblioteca;

    public EspertoDTO(String email, String password, String provincia, String citta, String via, String recapitoTelefonico, String nome, String cognome, String username, String emailBiblioteca) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        //this.generi = generi;
        this.emailBiblioteca = emailBiblioteca;
    }
    public EspertoDTO(String email, byte[] password, String provincia, String citta, String via, String recapitoTelefonico, String nome, String cognome, String username, String emailBiblioteca) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        //this.generi = generi;
        this.emailBiblioteca = emailBiblioteca;

    }

    public EspertoDTO(Esperto esperto) {
        super(esperto.getEmail(), esperto.getPassword(), esperto.getProvincia(), esperto.getCitta(), esperto.getVia(), esperto.getRecapitoTelefonico());
        this.cognome = esperto.getCognome();
        this.nome = esperto.getNome();
        this.username = esperto.getUsername();
        this.generi = esperto.getGeneri();
        this.emailBiblioteca = esperto.getBiblioteca().getEmail();

    }
}