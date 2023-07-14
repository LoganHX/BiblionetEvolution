package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
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
    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * a cui il lettore é iscritto.
     *
     * @return La view di visualizzazione dei clubs a cui é iscritto
     */
    @GetMapping(value = "visualizza-clubs-lettore")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsLettore(
            final @RequestHeader(name = "Authorization") String token
    ) {
        if (!Utils.isUtenteLettore(token)) return new ArrayList<>();
        Lettore lettore =  lettoreService.getLettoreByEmail(Utils.getSubjectFromToken(token));
        return lettore.getClubs();
    }





}
