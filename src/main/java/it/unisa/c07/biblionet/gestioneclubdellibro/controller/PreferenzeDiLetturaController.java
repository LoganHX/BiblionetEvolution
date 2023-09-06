package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/preferenze-di-lettura")
public class PreferenzeDiLetturaController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final PreferenzeDiLetturaService preferenzeDiLetturaService;
    private final GenereService genereService;
    private final EspertoService espertoService;
    private final LettoreService lettoreService;
    private final Utils utils;


    /**
     * Implementa la funzionalità d'inserire o rimuovere generi a un esperto
     * oppure a un lettore.
     *
     * @param generi i generi che il lettore o l'esperto dovranno possedere
     * @return la pagina home
     */
    @PostMapping(value = "/modifica-generi")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse modificaGeneri(@RequestParam("genere") final String[] generi,
                                            @RequestHeader(name = "Authorization") final String token) {

        Set<Genere> toAdd = genereService.getGeneriByName(generi);

        if (utils.isUtenteEsperto(token)) {
            preferenzeDiLetturaService
                    .addGeneriEsperto(toAdd, espertoService.findEspertoByEmail(utils.getSubjectFromToken(token)));
            return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
        }
        if (utils.isUtenteLettore(token)) {
            preferenzeDiLetturaService
                    .addGeneriLettore(toAdd, lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token)));
            return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
        }

        return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
    }
}
