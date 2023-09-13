package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/esperto")
public class EspertoController {

    private final EspertoService espertoService;
    private final ClubDelLibroService clubDelLibroService;
    private final Utils utils;

    /**
     * Implementa la funzionalità di visualizzare tutti gli Esperti
     * presenti sulla piattaforma.
     * @return la view che visualizza tutti gli Esperti
     */
    @GetMapping(value = "/lista-esperti")
    @CrossOrigin
    @ResponseBody
    public final List<EspertoDTO> visualizzaListaEsperti() {
        return espertoService.getInformazioniEsperti(espertoService.findAllEsperti());
    }

    @GetMapping(value = "/visualizza-esperti-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<EspertoDTO> visualizzaEspertiBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        return espertoService.getInformazioniEsperti(espertoService.getEspertiByBiblioteca(emailBiblioteca));
    }

    @GetMapping(value = "/visualizza-clubs-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<ClubDTO> visualizzaClubBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        Set<ClubDelLibro> clubs= new HashSet<>();
        List<Esperto> esperti = espertoService.getEspertiByBiblioteca(emailBiblioteca);
        for(Esperto esperto: esperti){
            clubs.addAll(esperto.getClubs());
        }
        return clubDelLibroService.getInformazioniClubs(new ArrayList<>(clubs));
    }

    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * che l'esperto gestisce.
     *
     * @return La view di visualizzazione dei clubs che gestisce
     */
    @PostMapping(value = "/visualizza-clubs-esperto")
    @ResponseBody
    @CrossOrigin
    public List<ClubDTO> visualizzaClubsEsperto(final @RequestHeader(name = "Authorization") String token) {
        if (!utils.isUtenteEsperto(token)) return null;
        return clubDelLibroService.getInformazioniClubs(espertoService.findEspertoByEmail(utils.getSubjectFromToken(token)).getClubs());
    }

//    @PostMapping(value = "/informazioni")
//    @ResponseBody
//    @CrossOrigin
//    public Map<String, Object> getInformazioniEsperto(final @RequestHeader(name = "Authorization") String token) {
//
//        if (!utils.isUtenteEsperto(token)) return null;
//
//        Map map = new HashMap();
//        Esperto e = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));
//        //Biblioteca b = (Biblioteca) e.getBiblioteca();
//        map.put("Esperto", new EspertoDTO(e));
//        //map.put("Biblioteca", b.getNomeBiblioteca());
//
//        return map;
//    }
    @PostMapping(value = "/informazioni")
    @ResponseBody
    @CrossOrigin
    public EspertoDTO getInformazioniEsperto(final @RequestHeader(name = "Authorization") String token) {
        if (!utils.isUtenteLettore(token)) return null;
        return new EspertoDTO(espertoService.findEspertoByEmail(utils.getSubjectFromToken(token)));
    }



}
