package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Size;


import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class LettoreDTO extends UtenteRegistratoDTO {

    private Set<String> generi;
    @NonNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String nome;
    @NonNull
    @Pattern(regexp = BiblionetConstraints.NAME_REGEX)
    private String cognome;
    @NonNull
    @Size(max = 30, message = "Il campo 'nome' può avere al massimo 50 caratteri")
    private String username;

    public LettoreDTO(@NonNull String email, @NonNull String password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nome, String cognome, String username) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
    }
    public LettoreDTO(@NonNull String email, @NonNull byte[] password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = BiblionetConstraints.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = BiblionetConstraints.PHONE_REGEX) String recapitoTelefonico, String nome, String cognome, String username) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
    }
    public LettoreDTO(Lettore l){
        super(l.getEmail(), l.getPassword(), l.getProvincia(), l.getCitta(), l.getVia(), l.getRecapitoTelefonico());
        this.cognome = l.getCognome();
        this.nome = l.getNome();
        this.username = l.getUsername();
        this.generi = l.getGeneri();
    }
}
