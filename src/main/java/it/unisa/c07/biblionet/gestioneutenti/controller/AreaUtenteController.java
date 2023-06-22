package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.events.CreateBiblioteca;
import it.unisa.c07.biblionet.events.CreateLettore;
import it.unisa.c07.biblionet.events.MiddleEsperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.common.*;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    private final RegistrazioneService registrazioneService;

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
        if (BiblionetConstraints.confrontoPassword(nuova, conferma)) {
            return conferma;
        }
        return vecchia;
    }

    /**
     * Implementa la funzionalità di modifica dati di una biblioteca.
     *
     * @param biblioteca email della biblioteca da modificare.
     * @param vecchia    La vecchia password dell'account.
     * @param nuova      La nuova password dell'account.
     * @param conferma   La password di conferma password dell'account.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_biblioteca Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-biblioteca")
    @ResponseBody
    @CrossOrigin
    public void modificaDatiBiblioteca(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Biblioteca") BibliotecaDTO biblioteca,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(!Utils.isUtenteBiblioteca(token)) return;

        biblioteca.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, biblioteca);
        if (!s.isEmpty()) return;


        events.publishEvent(new CreateBiblioteca(biblioteca));

    }


    /**
     * Implementa la funzionalità di modifica dati di un esperto.
     *
     * @param esperto         Un esperto da modificare.
     * @param vecchia         La vecchia password dell'account.
     * @param nuova           La nuova password dell'account.
     * @param conferma        La password di conferma password dell'account.
     * @param emailBiblioteca L'email della biblioteca scelta.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_esperto Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-esperto")
    @ResponseBody
    @CrossOrigin
    public void modificaDatiEsperto(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Esperto") EspertoDTO esperto,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma,
            final @RequestParam("email_biblioteca") String emailBiblioteca) {



        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token)))
            return;

        if (!Utils.getSubjectFromToken(token).equals(esperto.getEmail()))
            return; //todo

        esperto.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, esperto);
        if (!s.isEmpty()) return;

        //registrazioneService.aggiornaEsperto(esperto, emailBiblioteca); //todo qualche check in più sull'esistenza dell'esperto, anche se se ha il token è autoamticamente registrato

        events.publishEvent(new MiddleEsperto(esperto, emailBiblioteca));
    }

    /**
     * Implementa la funzionalità di modifica dati di un lettore.
     *
     * @param lettore  Un lettore da modificare.
     * @param vecchia  La vecchia password dell'account.
     * @param nuova    La nuova password dell'account.
     * @param conferma La password di conferma password dell'account.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_lettore Se la modifica non va a buon fine
     */
    @PostMapping(value = "/conferma-modifica-lettore")
    @ResponseBody
    @CrossOrigin
    public void confermaModificaLettore(final @RequestHeader(name = "Authorization") String token,
                                                     final @Valid @ModelAttribute LettoreDTO lettore,
                                                     BindingResult bindingResult,
                                                     final @RequestParam("vecchia_password") String vecchia,
                                                     final @RequestParam("nuova_password") String nuova,
                                                     final @RequestParam("conferma_password") String conferma) {

        lettore.setPassword(qualePassword(vecchia, nuova, conferma));
        String s = controlliPreliminari(bindingResult, vecchia, lettore);
        if (!s.isEmpty()) return;


        events.publishEvent(new CreateLettore(lettore));

    }



}
