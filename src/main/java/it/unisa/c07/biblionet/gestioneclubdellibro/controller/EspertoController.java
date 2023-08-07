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
