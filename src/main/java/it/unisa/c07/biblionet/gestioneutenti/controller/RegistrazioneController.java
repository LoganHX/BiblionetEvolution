package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.events.CreateBiblioteca;
import it.unisa.c07.biblionet.events.CreateLettore;
import it.unisa.c07.biblionet.events.MiddleEsperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/registrazione")
public final class RegistrazioneController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final RegistrazioneService registrazioneService;
    private final ApplicationEventPublisher events;

    @PostMapping(value = "/esperto")
    @ResponseBody
    @CrossOrigin
    public void registrazioneEsperto(final @Valid @ModelAttribute EspertoDTO esperto,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password") String password,
                                                  final @RequestParam("email_biblioteca") String bibliotecaEmail) {

        String s = controlliPreliminari(bindingResult, password, esperto);
        if (!s.isEmpty()) {
            return;
        }
        events.publishEvent(new MiddleEsperto(esperto, bibliotecaEmail));
    }

    /**
     * Implementa la funzionalità di registrazione di una biblioteca.
     *
     * @param biblioteca la biblioteca da registrare
     * @param password   la password di conferma
     */
    @PostMapping(value = "/biblioteca")
    @ResponseBody
    @CrossOrigin
    public void registrazioneBiblioteca(@Valid @ModelAttribute BibliotecaDTO biblioteca,
                                                     BindingResult bindingResult,
                                                     @RequestParam("conferma_password") String password
    ) {
        String s = controlliPreliminari(bindingResult, password, biblioteca);
        if (!s.isEmpty()) {
            return;
        }
        events.publishEvent(new CreateBiblioteca(biblioteca));
    }


    /**
     * Implementa la funzionalità di registrazione di
     * un lettore.
     * Gestisce la chiamata POST
     * per creare un nuovo lettore.
     *
     * @param lettore  Il lettore da registrare
     * @param password il campo conferma password del form per controllare
     *                 il corretto inserimento della stessa.
     */
    @PostMapping(value = "/lettore")
    @ResponseBody
    @CrossOrigin
    public void registrazioneLettore(@Valid @ModelAttribute LettoreDTO lettore,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password")
                                                  String password
    ) {
        String s = controlliPreliminari(bindingResult, password, lettore);
        if (!s.isEmpty()) return;

        events.publishEvent(new CreateLettore(lettore));
    }

    private String controlliPreliminari(BindingResult bindingResult, String password, UtenteRegistratoDTO utenteRegistrato) {
        if (bindingResult.hasErrors()) {
            return "Errore di validazione";
        }
        if (registrazioneService.isEmailRegistrata(utenteRegistrato.getEmail())) {
            return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
        }
        if (!BiblionetConstraints.passwordRispettaVincoli(utenteRegistrato.getPassword(), password)) {
            return "Password non adeguata";
        }
        return "";
    }

}
