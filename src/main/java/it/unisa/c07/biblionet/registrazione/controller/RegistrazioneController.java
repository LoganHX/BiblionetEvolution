package it.unisa.c07.biblionet.registrazione.controller;

import it.unisa.c07.biblionet.model.entity.utente.Biblioteca;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.registrazione.service.RegistrazioneService;
import it.unisa.c07.biblionet.utils.validazione.RegexTester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@SessionAttributes("loggedUser")
@RequiredArgsConstructor
@RequestMapping("/registrazione")
public final class RegistrazioneController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final RegistrazioneService registrazioneService;
    private static RegexTester regexTester;

    /**
     * Implementa la funzionalità di visualizzare
     * la scelta di registrazione.
     *
     * @return La pagina di visualizzazione
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String visualizzaRegistrazione() {
        return "registrazione/registrazione";
    }

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
    public String registrazioneEsperto(final @ModelAttribute("esperto") Esperto esperto,
                                        final @RequestParam("conferma_password") String password,
                                        final @RequestParam("bibliotecaEmail") String bibliotecaEmail) {

        if (registrazioneService.isEmailRegistrata(esperto.getEmail())) {
            return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
        }
        if (!registrazioneService.isEmailRegistrata(bibliotecaEmail)) {
            return "Non vi è alcuna biblioteca già registrata associata all'indirizzo e-mail fornito";
        }

        if (!RegexTester.testEsperto(esperto)) {
            return "I dati forniti non rispettano il formato atteso.";
        }
        if(!controllaPassword(esperto, password) || password.length() <= 7){
            return "Password non adeguata";
        }

        esperto.setBiblioteca(registrazioneService.getBibliotecaByEmail(bibliotecaEmail));

        registrazioneService.registraEsperto(esperto);
        return "Registrazione effettuata correttamente";
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
    public String registrazioneBiblioteca(@ModelAttribute("biblioteca") Biblioteca biblioteca,
                                           @RequestParam("conferma_password")
                                           String password
                                           ) {

        if (registrazioneService.isEmailRegistrata(biblioteca.getEmail())) {
            return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
        }
        if (!RegexTester.testBiblioteca(biblioteca)) {
            return "I dati forniti non rispettano il formato atteso.";
        }

        if(!controllaPassword(biblioteca, password) || password.length() <= 7){
            return "Password non adeguata";
        }

        registrazioneService.registraBiblioteca(biblioteca);
        return "Registrazione effettuata correttamente";
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
    public String registrazioneLettore(final @ModelAttribute("lettore") Lettore lettore,
                                       final @RequestParam("conferma_password")
                                               String password
    ) {

        if (registrazioneService.isEmailRegistrata(lettore.getEmail())) {
            return "Il sistema presenta un account già registrato per questo indirizzo e-mail.";
        }
        if (!RegexTester.testLettore(lettore)) {
            return "I dati forniti non rispettano il formato atteso.";
        }
        if(!controllaPassword(lettore, password) || password.length() <= 7){
            return "Password non adeguata";
        }

        registrazioneService.registraLettore(lettore);
        return "Registrazione effettuata correttamente";
    }

    private boolean controllaPassword(UtenteRegistrato utente, String password){
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            byte[] arr = md.digest(password.getBytes());

            if (Arrays.compare(arr, utente.getPassword()) != 0) {
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return true;
    }


}
