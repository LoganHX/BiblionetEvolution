package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
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

    private final AutenticazioneService autenticazioneService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final BibliotecaService bibliotecaService;

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
    public BiblionetResponse modificaDatiBiblioteca(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Biblioteca") BibliotecaDTO biblioteca,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(!Utils.isUtenteBiblioteca(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(biblioteca.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo


        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        String s = controlliPreliminari(bindingResult, vecchia, biblioteca);
        if (!s.isEmpty()) return new BiblionetResponse(s, false);
        biblioteca.setPassword(password);

        UtenteRegistrato b = bibliotecaService.creaBibliotecaDaModel(biblioteca);
        if(b==null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
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
    @PostMapping(value = "/modifica-esperto")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiEsperto(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Esperto") EspertoDTO esperto,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma,
            final @RequestParam("email_biblioteca") String emailBiblioteca) {


        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(esperto.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un esperto
        if(espertoService.findEspertoByEmailAndPassword(esperto.getEmail(), BiblionetConstraints.trasformaPassword(vecchia)) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        esperto.setPassword(password);

        espertoService.aggiornaEspertoDaModel(esperto, bibliotecaService.findBibliotecaByEmail(emailBiblioteca)); //todo qualche check in più sull'esistenza dell'esperto, anche se se ha il token è autoamticamente registrato

        return new BiblionetResponse("Dati aggiornati", true);
    }

    @PostMapping(value = "/conferma-modifica")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiLettore(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @RequestParam("Lettore") LettoreDTO lettore,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(bindingResult.hasErrors()) return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        if (!Utils.isUtenteLettore(Utils.getSubjectFromToken(token)))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if (!Utils.getSubjectFromToken(token).equals(lettore.getEmail()))
            return new BiblionetResponse("Non puoi cambiare email", false); //todo non si può modificare la mail, va fatto anche per lettore e biblioteca sto controllo

        //todo tecnicamente non controllo se è un lettore
        if(lettoreService.findLettoreByEmailAndPassword(lettore.getEmail(), BiblionetConstraints.trasformaPassword(vecchia)) == null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        String password = (BiblionetConstraints.confrontoPassword(nuova, conferma));
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        lettore.setPassword(password);

        lettoreService.aggiornaLettoreDaModel(lettore);
        return new BiblionetResponse("Dati aggiornati", true);
    }

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
