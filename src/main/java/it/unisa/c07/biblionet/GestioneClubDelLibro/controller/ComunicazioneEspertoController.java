package it.unisa.c07.biblionet.GestioneClubDelLibro.controller;

import it.unisa.c07.biblionet.GestioneClubDelLibro.ComunicazioneEspertoService;
import it.unisa.c07.biblionet.GestioneUtenti.AutenticazioneService;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Della Porta Antonio
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes("loggedUser")
@RequestMapping("/comunicazione-esperto")
public class ComunicazioneEspertoController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final ComunicazioneEspertoService comunicazioneEspertoService;
    private final AutenticazioneService autenticazioneService;

    /**
     * Implementa la funzionalità di mostrare gli esperti in base
     * ai generi preferiti del lettore.
     * @return la view contenente la lista
     */
    @GetMapping(value = "/visualizza-esperti-genere")
    @ResponseBody
    @CrossOrigin
    public final List<Esperto> visualizzaEspertiGeneri(@RequestHeader(name = "Authorization") final String token) {
        if(!Utils.isUtenteLettore(token)) return null;
        Lettore lettore = autenticazioneService.findLettoreByEmail(Utils.getSubjectFromToken(token));
        return autenticazioneService.findEspertiByGeneri(lettore.getGeneri());
    }

    /**
     * Implementa la funzionalità di visualizzare tutti gli Esperti
     * presenti sulla piattaforma.
     * @param model il model per la richiesta
     * @return la view che visualizza tutti gli Esperti
     */
    @RequestMapping(value = "/lista-esperti", method = RequestMethod.GET)
    public final String visualizzaListaEsperti(final Model model) {
        List<Esperto> listaEsperti =
                autenticazioneService.findAllEsperti();
        model.addAttribute("listaEsperti", listaEsperti);
        return "comunicazione-esperto/lista-completa-esperti";
    }

    /**
     * Implementa la funzionalità di visualizzare tutti gli Esperti
     * presenti sulla piattaforma.
     * @param stringa il contenuto del filtro
     * @param filtro il nome del filtro
     * @return la view che visualizza tutti gli Esperti
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public final List<Esperto> visualizzaListaEspertiFiltrati(
            //todo check se loggato
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {
        switch (filtro) {
            case "nome":
                return autenticazioneService.findEspertiByNome(stringa);
            case "genere":
                return autenticazioneService.findEspertiByGeneri(new HashSet<String>(Collections.singleton(stringa)));
            default:
                return autenticazioneService.findAllEsperti();
        }
    }
}
