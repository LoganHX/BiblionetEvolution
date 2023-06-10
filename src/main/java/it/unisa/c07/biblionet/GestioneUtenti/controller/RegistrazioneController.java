package it.unisa.c07.biblionet.GestioneUtenti.controller;

import it.unisa.c07.biblionet.entity.Biblioteca;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.entity.UtenteRegistrato;
import it.unisa.c07.biblionet.GestioneUtenti.RegistrazioneService;
import it.unisa.c07.biblionet.utils.validazione.RispettoVincoli;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Implementa la funzionalità di visualizzare
     * la scelta di registrazione.
     *
     * @return La pagina di visualizzazione

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String visualizzaRegistrazione() {
        return "registrazione/registrazione";
    }
    */

    /**
     * Implementa la funzionalità di registrazione di
     * scegliere il tipo di utente da registrare.
     *
     * @return La view che visualizza il form di registrazione scelto.

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Boolean effettuaRegistrazione(final @RequestBody
                                       RequestRegister request) {

        // controlla che la mail non sia registrata
        if(registrazioneService.isEmailRegistrata(request.getEmail()))
            return false;

        if(request.getRole().equalsIgnoreCase("biblioteca")){
                registrazioneService.registraBiblioteca(new Biblioteca(request.getEmail(),request.getPassword(),request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(),request.getNome()));
                return true;
        }

        else if(request.getRole().equalsIgnoreCase("esperto")){
            registrazioneService.registraEsperto(new Esperto(request.getEmail(), request.getPassword(), request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(), request.getUsername(), request.getNome(), request.getCognome(), registrazioneService.getBibliotecaByEmail("")));
            return true;
        }

        else if(request.getRole().equalsIgnoreCase("lettore")){
            //replica per lettore e controlla che non ci siano altre mail
            registrazioneService.registraLettore(new Lettore(request.getEmail(), request.getPassword(), request.getProvincia(),request.getCitta(),request.getVia(),request.getRecapitoTelefonico(), request.getUsername(), request.getNome(), request.getCognome()));
        }
        return false;
    }


    /**
     * Implementa la funzionalità di registrazione di un esperto.
     *
     * @param bibliotecaEmail la mail dell'account della biblioteca
     *                        dove l'esperto lavora
     * @return la view per effettuare il login
     */
    @RequestMapping(value = "/esperto", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> registrazioneEsperto(final @Valid @ModelAttribute Esperto esperto,
                                                       BindingResult bindingResult,
                                                       final @RequestParam("conferma_password") String password,
                                                       final @RequestParam("bibliotecaEmail") String bibliotecaEmail) {

        String s = controlliPreliminari(bindingResult, password, esperto);
        if(!s.isEmpty()){
            return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
        }
        esperto.setBiblioteca(registrazioneService.getBibliotecaByEmail(bibliotecaEmail));

        registrazioneService.registraEsperto(esperto);
        return new ResponseEntity<>("Registrazione ok", HttpStatus.OK);
    }

    /**
     * Implementa la funzionalità di registrazione di una biblioteca.
     *
     * @param biblioteca la biblioteca da registrare
     * @param password   la password di conferma
     * @return la view di login
     */
    @RequestMapping(value = "/biblioteca", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> registrazioneBiblioteca(@Valid @ModelAttribute Biblioteca biblioteca,
                                                          BindingResult bindingResult,
                                                          @RequestParam("conferma_password") String password
    ) {
        String s = controlliPreliminari(bindingResult, password, biblioteca);
        if(!s.isEmpty()){
            return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
        }
        registrazioneService.registraBiblioteca(biblioteca);
        return new ResponseEntity<>("Registrazione effettuata correttamente", HttpStatus.OK);
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
    @RequestMapping(value = "/lettore", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<String> registrazioneLettore(@Valid @ModelAttribute Lettore lettore,
                                       BindingResult bindingResult,
                                       final @RequestParam("conferma_password")
                                               String password
    ) {
        String s = controlliPreliminari(bindingResult, password, lettore);
        if(!s.isEmpty()){
            return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
        }

        registrazioneService.registraLettore(lettore);
        return new ResponseEntity<>("Registrazione effettuata correttamente", HttpStatus.OK);
    }

private String controlliPreliminari(BindingResult bindingResult, String password, UtenteRegistrato utenteRegistrato){
    if(bindingResult.hasErrors()){
        return "Errore di validazione";
    }
    if (registrazioneService.isEmailRegistrata(utenteRegistrato.getEmail())) {
        return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
    }
    if(!RispettoVincoli.passwordRispettaVincoli(utenteRegistrato, password)){
        return "Password non adeguata";
    }
    return "";
}

}
