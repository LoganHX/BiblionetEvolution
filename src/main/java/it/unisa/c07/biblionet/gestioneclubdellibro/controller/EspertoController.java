package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoDTO;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.utils.BiblionetConstraints;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/esperto")
public class EspertoController {

    private final EspertoService espertoService;


    /**
     * Implementa la funzionalità di visualizzazione dei clubs
     * che l'esperto gestisce.
     *
     * @return La view di visualizzazione dei clubs che gestisce
     */
    @GetMapping(value = "area-utente/visualizza-clubs-esperto")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubsEsperto(final @RequestHeader(name = "Authorization") String token) {
        if (!Utils.isUtenteEsperto(Utils.getSubjectFromToken(token))) return new ArrayList<>();
        return espertoService.findEspertoByEmail(Utils.getSubjectFromToken(token)).getClubs();
    }
    @GetMapping(value = "/visualizza-esperti-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<Esperto> visualizzaEspertiBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        return espertoService.getEspertiByBiblioteca(emailBiblioteca);
    }

    @GetMapping(value = "/visualizza-clubs-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<ClubDelLibro> visualizzaClubBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        Set<ClubDelLibro> clubs= new HashSet<>();
        List<Esperto> esperti = espertoService.getEspertiByBiblioteca(emailBiblioteca);
        for(Esperto esperto: esperti){
            clubs.addAll(esperto.getClubs());
        }
        return new ArrayList<>(clubs);
    }



}
