package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Della Porta Antonio
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/comunicazione-esperto")
public class ComunicazioneEspertoController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final Utils utils;

    /**
     * Implementa la funzionalità di mostrare gli esperti in base
     * ai generi preferiti del lettore.
     * @return la view contenente la lista
     */
    @GetMapping(value = "/visualizza-esperti-genere")
    @ResponseBody
    @CrossOrigin
    //todo da bypassare
    public final List<EspertoDTO> visualizzaEspertiGeneri(@RequestHeader(name = "Authorization") final String token) {
        if(!utils.isUtenteLettore(token)) return new ArrayList<>();
        Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        return espertoService.getInformazioniEsperti(espertoService.findEspertiByGeneri(lettore.getGeneri()));
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
    public final List<EspertoDTO> visualizzaListaEspertiFiltrati(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {
        switch (filtro) {
            case "nome":
                return espertoService.getInformazioniEsperti(espertoService.findEspertiByNome(stringa));
            case "genere":
                return espertoService.getInformazioniEsperti(espertoService.findEspertiByGeneri(new HashSet<>(Collections.singleton(stringa))));
            default:
                return espertoService.getInformazioniEsperti(espertoService.findAllEsperti());
        }
    }
}
