package it.unisa.c07.biblionet.gestioneutenti.controller;

import it.unisa.c07.biblionet.common.UtenteRegistratoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneutenti.AutenticazioneService;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
     * @return login Se la modifica va a buon fine.
     * modifica_dati_biblioteca Se la modifica non va a buon fine
     */
    @PostMapping(value = "/modifica-biblioteca")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiBiblioteca(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Biblioteca") BibliotecaDTO biblioteca,
            BindingResult bindingResult) {

        if(!utils.isUtenteBiblioteca(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        Biblioteca b = bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));

        if(b == null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        biblioteca.setPasswordDigested(b.getPassword());

        BiblionetResponse s = controlliPreliminari(bindingResult, biblioteca, token);
        if (s != null) return s;

        b = bibliotecaService.aggiornaBibliotecaDaModel(biblioteca);
        if(b==null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }
    /**
     * Implementa la funzionalità di modifica dati di un esperto.
     *
     * @param esperto         Il dto dell'esperto da modificare.
     * @return login Se la modifica va a buon fine.
     * modifica_dati_esperto Se la modifica non va a buon fine
     */
    @PostMapping(value = "/modifica-esperto")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiEsperto(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Esperto") EspertoDTO esperto,
            BindingResult bindingResult) {


        if (!utils.isUtenteEsperto(token))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        Esperto e = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));

        if(e == null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        if(bibliotecaService.findBibliotecaByEmail(esperto.getEmailBiblioteca()) == null){
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }

        BiblionetResponse s = controlliPreliminari(bindingResult, esperto, token);
        if (s != null) return s;
        esperto.setPasswordDigested(e.getPassword());

        e = espertoService.aggiornaEspertoDaModel(esperto, bibliotecaService.findBibliotecaByEmail(esperto.getEmailBiblioteca()));
        if(e == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    @PostMapping(value = "/modifica-lettore")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiLettore(
            final @RequestHeader(name = "Authorization") String token,
            final @Valid @ModelAttribute("Lettore") LettoreDTO lettore,
            BindingResult bindingResult) {


        if (!utils.isUtenteLettore(token))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if(l == null){
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }

        BiblionetResponse s = controlliPreliminari(bindingResult, lettore, token);
        if (s != null) return s;

        lettore.setPasswordDigested(l.getPassword());

        l = lettoreService.aggiornaLettoreDaModel(lettore);
        if(l == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    private BiblionetResponse controlliPreliminari(BindingResult bindingResult, UtenteRegistratoDTO utenteRegistrato, String token) {

        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }

        if(!utils.match(utils.getSubjectFromToken(token), utenteRegistrato.getEmail()))
            return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        return null;

    }


    @PostMapping(value = "/modifica-password")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaPassword(
            final @RequestHeader(name = "Authorization") String token,
            @RequestParam final String vecchiaPassword,
            @RequestParam final String nuovaPassword,
            @RequestParam final String confermaNuovaPassword) {

        if(autenticazioneService.login(utils.getSubjectFromToken(token), vecchiaPassword) == null){
            return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        }
        if(!nuovaPassword.equals(confermaNuovaPassword)) return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);

        if (utils.isUtenteLettore(token)){
            Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
            if(lettore == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
            lettore.setPassword(nuovaPassword);
            lettoreService.aggiornaLettore(lettore);
        }

        else if (utils.isUtenteEsperto(token)){
            Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
            if(espertoService == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
            esperto.setPassword(nuovaPassword);
            espertoService.aggiornaEsperto(esperto);
        }

        else{
            Biblioteca biblioteca = bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));
            if(biblioteca == null )return new BiblionetResponse(BiblionetResponse.ERRORE, false);
            biblioteca.setPassword(nuovaPassword);
            bibliotecaService.aggiornaBiblioteca(biblioteca);
        }

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

}
