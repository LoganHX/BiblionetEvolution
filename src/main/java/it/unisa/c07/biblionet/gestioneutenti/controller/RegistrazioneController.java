package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneutenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import lombok.RequiredArgsConstructor;
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
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final BibliotecaService bibliotecaService;

    /**
     * Implementa la funzionalità di registrazione di un esperto.*/

     @PostMapping(value = "/esperto")
     @ResponseBody
     @CrossOrigin
     public BiblionetResponse registrazioneEsperto(final @Valid @ModelAttribute EspertoDTO esperto,
     BindingResult bindingResult,
     final @RequestParam("conferma_password") String password,
     final @RequestParam("email_biblioteca") String bibliotecaEmail) {

     if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

     if(!BiblionetConstraints.passwordRispettaVincoli(esperto.getPassword(), password))
     return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

     UtenteRegistrato e = espertoService.creaEspertoDaModel(esperto, bibliotecaService.findBibliotecaByEmail(bibliotecaEmail));
     if(e == null) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
     return new BiblionetResponse("Registrazione ok", true);
     }



    /**
     * Implementa la funzionalità di registrazione di una biblioteca.
     *
     * @param biblioteca la biblioteca da registrare
     * @param password   la password di conferma
     * @return la view di login
     */
    @PostMapping(value = "/registrazione")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneBiblioteca(@Valid @ModelAttribute BibliotecaDTO biblioteca,
                                                     BindingResult bindingResult,
                                                     @RequestParam("conferma_password") String password
    ) {
        if (bindingResult.hasErrors()) {
            return new BiblionetResponse("Errore di validazione", false);
        }
        if(! BiblionetConstraints.passwordRispettaVincoli(biblioteca.getPassword(), password)) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        bibliotecaService.creabibliotecaDaModel(biblioteca);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
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
     * @return La view per effettuare il login
     */
    @PostMapping(value = "/lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse registrazioneLettore(@Valid @ModelAttribute LettoreDTO lettore,
                                                  BindingResult bindingResult,
                                                  final @RequestParam("conferma_password")
                                                  String password
    ) {
        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if(!BiblionetConstraints.passwordRispettaVincoli(lettore.getPassword(), password))
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        lettoreService.creaLettoreDaModel(lettore);
        return new BiblionetResponse("Registrazione effettuata correttamente", true);
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
