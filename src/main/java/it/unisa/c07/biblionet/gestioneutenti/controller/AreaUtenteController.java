package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
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
import java.util.ArrayList;
import java.util.List;


/**
 * @author Alessio Casolaro, Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/area-utente")
public class AreaUtenteController {

    private final AutenticazioneService autenticazioneService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final BibliotecaService bibliotecaService;
    private final Utils utils;

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
    @PostMapping(value = "/modifica-biblioteca")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiBiblioteca(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Biblioteca") BibliotecaDTO biblioteca,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {

        if(!utils.isUtenteBiblioteca(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token))==null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        BiblionetResponse s = controlliPreliminari(bindingResult, vecchia, nuova, conferma, biblioteca, token);
        if (s != null) return s;
        biblioteca.setPassword(conferma);


        Biblioteca b = bibliotecaService.aggiornaBibliotecaDaModel(biblioteca);
        if(b==null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }
    /**
     * Implementa la funzionalità di modifica dati di un esperto.
     *
     * @param esperto         Il dto dell'esperto da modificare.
     * @param vecchia         La vecchia password dell'account.
     * @param nuova           La nuova password dell'account.
     * @param conferma        La password di conferma password dell'account.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_esperto Se la modifica non va a buon fine
     */
    @PostMapping(value = "/modifica-esperto")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiEsperto(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Esperto") EspertoDTO esperto,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {


        if (!utils.isUtenteEsperto(token))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(espertoService.findEspertoByEmail(utils.getSubjectFromToken(token))==null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        if(bibliotecaService.findBibliotecaByEmail(esperto.getEmailBiblioteca()) == null){
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }

        BiblionetResponse s = controlliPreliminari(bindingResult, vecchia, nuova, conferma, esperto, token);
        if (s != null) return s;
        esperto.setPassword(conferma);

        Esperto e = espertoService.aggiornaEspertoDaModel(esperto, bibliotecaService.findBibliotecaByEmail(esperto.getEmailBiblioteca()));
        if(e == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    @PostMapping(value = "/modifica-lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiLettore(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Lettore") LettoreDTO lettore,
            BindingResult bindingResult,
            final @RequestParam("vecchia_password") String vecchia,
            final @RequestParam("nuova_password") String nuova,
            final @RequestParam("conferma_password") String conferma) {


        if (!utils.isUtenteLettore(token))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token))==null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        BiblionetResponse s = controlliPreliminari(bindingResult, vecchia, nuova, conferma, lettore, token);
        if (s != null) return s;
        lettore.setPassword(conferma);

        Lettore l = lettoreService.aggiornaLettoreDaModel(lettore);
        if(l == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    private BiblionetResponse controlliPreliminari(BindingResult bindingResult, String vecchia, String nuova, String conferma, UtenteRegistratoDTO utenteRegistrato, String token) {

        String password = BiblionetConstraints.confrontoPassword(nuova, conferma);
        if(password.isEmpty()) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

        if (!utils.getSubjectFromToken(token).equals(utenteRegistrato.getEmail()))
            return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        if (autenticazioneService.login(utenteRegistrato.getEmail(), vecchia) == null) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        return null;

    }

}
