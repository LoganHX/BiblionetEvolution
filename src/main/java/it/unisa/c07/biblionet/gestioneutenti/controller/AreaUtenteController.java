package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;


/**
 * @author Alessio Casolaro, Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
public class AreaUtenteController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final AutenticazioneService autenticazioneService;
    private final ApplicationEventPublisher events;



    private String controlliPreliminari(BindingResult bindingResult, String vecchia, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }

        if (autenticazioneService.login(utenteRegistrato.getEmail(), vecchia) == null) { //usata solo per vedere se la password vecchia corrisponde, non effettua davvero il login
            return "Password errata. Non sei autorizzato a modificare la password.";
        }

        return "";

    }

    private String qualePassword(String vecchia, String nuova, String conferma) {
        if (nuova.isEmpty() && conferma.isEmpty()) return vecchia;
        return BiblionetConstraints.confrontoPassword(nuova, conferma);
    }









}
