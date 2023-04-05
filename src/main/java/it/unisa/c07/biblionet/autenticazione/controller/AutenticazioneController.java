package it.unisa.c07.biblionet.autenticazione.controller;

import io.swagger.annotations.Api;
import it.unisa.c07.biblionet.autenticazione.service.AutenticazioneService;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;


/**
 * Implementa il controller per il sottosistema
 * Autenticazione.
 * @author Ciro Maiorino , Giulio Triggiani
 */
@RestController
@RequiredArgsConstructor
@Api("/autenticazione")
public class AutenticazioneController {
    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final AutenticazioneService autenticazioneService;

    /**
     * Implementa la funzionalità che permette
     * di visualizzare la view del login.
     * @param model il Model
     * @return la pagina dove è visualizzato
     */
    @GetMapping(value = "")
    public String visualizzaLogin(final Model model) {
        model.addAttribute("loggedUser", null);
        return "autenticazione/login";
    }

    /**
     * Implementa la funzionalità di login come utente.
     * @param email dell'utente.
     * @param password password dell'utente.
     * @param model la sessione in cui salvare l'utente.
     * @return rimanda alla pagina di home.
     */
    @PostMapping(value = "/login")
    public String login(@RequestBody RequestLogin request) {
        String utente = autenticazioneService.login(request.getEmail(),
                                                                request.getPassword());
            if (utente == null) {
                return "autenticazione/login";
            }
            return utente;

    }

    /**
     * Implenta la funzionalità che permette
     * di effettuare il logout dell'utente
     * togliendolo dalla sessione.
     * @param status contiene i dati della sessione.
     * @return Rimanda alla pagina di index.
     */
    @GetMapping(value = "/logout")

    public String logout(final SessionStatus status) {
        status.setComplete();
        return "index";
    }


    /**
     * Implementa la funzionalità che permette
     * di aggiungere un utente alla sessione.
     * @return dell'utente in sessione.
     */
    @ModelAttribute("loggedUser")
    public UtenteRegistrato utenteRegistrato() {
        return new UtenteRegistrato();
    }

}