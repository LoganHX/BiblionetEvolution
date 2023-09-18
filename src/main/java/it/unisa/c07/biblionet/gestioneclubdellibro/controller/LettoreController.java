package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lettore")
public class LettoreController {
    private final LettoreService lettoreService;
    private final ClubDelLibroService clubDelLibroService;
    private final Utils utils;
    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * a cui il lettore é iscritto.
     *
     * @return La view di visualizzazione dei clubs a cui é iscritto
     */
    @PostMapping(value = "/visualizza-clubs-lettore")
    @ResponseBody
    @CrossOrigin
    public List<Object> visualizzaClubsLettore(
            final @RequestHeader(name = "Authorization") String token
    ) {
        if (!utils.isUtenteLettore(token)) return null;
        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        if(l == null) return null;
        return clubDelLibroService.dettagliClub(l.getClubs());

        //return lettore.getClubs();
    }

    @PostMapping(value = "/informazioni")
    @ResponseBody
    @CrossOrigin
    public LettoreDTO getInformazioniLettore(final @RequestHeader(name = "Authorization") String token) {
        if (!utils.isUtenteLettore(token)) return null;
        return new LettoreDTO(lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token)));
    }






}
