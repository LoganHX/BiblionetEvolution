package it.unisa.c07.biblionet.DTO;

import it.unisa.c07.biblionet.utils.RispettoVincoli;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.Pattern;

@Getter
@Data
@SuperBuilder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public abstract class UtenteRegistratoDTO {


    //todo tecnicamente è un DTO credo

    /**
     * Rappresenta l'ID di un utente registrato.
     */
    @NonNull
    private String email;


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
}
