package it.unisa.c07.biblionet.gestioneclubdellibro;

import it.unisa.c07.biblionet.DTO.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.utils.RispettoVincoli;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Set;
import javax.validation.constraints.Pattern;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class LettoreDTO extends UtenteRegistratoDTO {
    /**
     * Rappresenta l'ID di un utente registrato.
     */
    @NonNull
    private String email;

    private Set<String> generi;

    /**
     * Rappresenta la password di un utente registrato.
     */
    @NonNull
    private byte[] password;

    /**
     * Rappresente la provincia dove vive l'utente registrato.
     */
    @NonNull
    private String provincia;

    /**
     * Rappresenta la città dove vive l'utente registrato.
     */
    @NonNull
    private String citta;

    /**
     * Rappresenta la via dove vive l'utente registrato.
     */
    @NonNull
    @Pattern(regexp = RispettoVincoli.ADDRESS_REGEX)
    private String via;

    /**
     * Rappresenta il recapito telefonico dell'utente registrato.
     */
    @NonNull
    @Pattern(regexp = RispettoVincoli.PHONE_REGEX)
    private String recapitoTelefonico;
    @NonNull
    private String nome;
    @NonNull
    private String cognome;
    @NonNull
    private String username;

    public LettoreDTO(@NonNull String email, @NonNull byte[] password, @NonNull String provincia, @NonNull String citta, @NonNull @Pattern(regexp = RispettoVincoli.ADDRESS_REGEX) String via, @NonNull @Pattern(regexp = RispettoVincoli.PHONE_REGEX) String recapitoTelefonico) {
        super(email, password, provincia, citta, via, recapitoTelefonico);
    }
}
