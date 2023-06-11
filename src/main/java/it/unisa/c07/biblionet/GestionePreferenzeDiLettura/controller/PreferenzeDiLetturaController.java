package it.unisa.c07.biblionet.GestionePreferenzeDiLettura.controller;

import it.unisa.c07.biblionet.GestioneGenere.GenereService;
import it.unisa.c07.biblionet.GestionePreferenzeDiLettura.PreferenzeDiLetturaService;
import it.unisa.c07.biblionet.GestioneUtenti.AutenticazioneService;
import it.unisa.c07.biblionet.entity.Esperto;
import it.unisa.c07.biblionet.entity.Genere;
import it.unisa.c07.biblionet.entity.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes("loggedUser")
@RequestMapping("/preferenze-di-lettura")
public class PreferenzeDiLetturaController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final PreferenzeDiLetturaService preferenzeDiLetturaService;
    private final GenereService genereService;
    private final AutenticazioneService autenticazioneService;

    /**
     * Implementa la funzionalità di controllare se l'utente in sessione
     * può modificare i generi e restituisce una lista dei generi che può aggiungere
     * ottenuta dalla lista totale dei generi, da cui sono stati rimossi i generi già padroneggiati dall'utente
     *
     * @return la view d'inserimento dei generi se l'utente è
     * Esperto o Lettore, la home altrimenti,
     */
    @RequestMapping("/generi")
    @ResponseBody
    @CrossOrigin
    public List<String> generiLetterari(
            @RequestHeader(name = "Authorization") final String token
    ) {
        List<String> generiUtente;
        if (Utils.isUtenteEsperto(token)) {
            Esperto esperto = autenticazioneService.findEspertoByEmail(Utils.getSubjectFromToken(token));
            generiUtente = new ArrayList<>(esperto.getGeneri());
        } else if (Utils.isUtenteLettore(token)) {
            Lettore lettore = autenticazioneService.findLettoreByEmail(Utils.getSubjectFromToken(token));
            generiUtente = new ArrayList<>(lettore.getGeneri());
        } else return null;


        Set<Genere> allGeneri = genereService.getAllGeneri();
        if (generiUtente != null) {
            for (String genere : generiUtente) {
                allGeneri.remove(genere);
            }
        } else {
            generiUtente = new ArrayList<>();
        }

        return generiUtente;

    }

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

        if (Utils.isUtenteEsperto(token)) {
            preferenzeDiLetturaService
                    .addGeneriEsperto(toAdd, autenticazioneService.findEspertoByEmail(Utils.getSubjectFromToken(token)));
            return new BiblionetResponse("Generi modificati", true);
        }
        if (Utils.isUtenteLettore(token)) {
            preferenzeDiLetturaService
                    .addGeneriLettore(toAdd, autenticazioneService.findLettoreByEmail(Utils.getSubjectFromToken(token)));
            return new BiblionetResponse("Generi modificati", true);
        }

        return new BiblionetResponse("Errore modifica generi utente", false);
    }
}
