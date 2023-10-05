package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Implementa il controller per il sottosistema
 * ClubDelLibro.
 *
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/club-del-libro")
public class ClubDelLibroController {

    private final ClubDelLibroService clubService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final GestioneEventiService eventiService;
    private final Utils utils;



    /**
     * Implementa la funzionalità che permette
     * di visualizzare i Club del Libro
     * presenti nel Database.
     *
     * @param generi Un Optional che contiene una lista di generi per cui
     *               filtrare
     * @param citta  Un Optional che contiene una lista di possibili città
     * @return La pagina di visualizzazione
     */
    @GetMapping(value = "")
    @ResponseBody
    @CrossOrigin
    public List <Object> visualizzaListaClubs(@RequestParam(value = "generi") final Optional <List<String >> generi,
                                                @RequestParam(value = "citta") final Optional<List<String>> citta) {

        Predicate <ClubDelLibro> filtroGenere = x -> true;

        if (generi.isPresent()) {
            filtroGenere = x -> false;

            var generiDaDB = generi.get();

            for (String genere: generiDaDB) {
                filtroGenere = filtroGenere.or(c -> c.getGeneri().contains(genere));
            }
        }

        Predicate < ClubDelLibro > filtroCitta = x -> true;

        if (citta.isPresent()) {
            filtroCitta = x -> false;
            for (String cittaSingola: citta.get()) {
                filtroCitta = filtroCitta.or(c -> clubService.getCittaFromClubDelLibro(c).equals(cittaSingola));
            }
        }

        List <ClubDelLibro> listaClubs = clubService.visualizzaClubsDelLibro(filtroCitta.and(filtroGenere));

        return clubService.dettagliClub(listaClubs);

    }


    /**
     * Implementa la funzionalità di creazione di un club del libro.
     *
     * @param clubDTO Il club che si vuole creare
     * @return la pagina del Club
     */
    @PostMapping(value = "/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaClubDelLibro(final @Valid @ModelAttribute ClubDTO clubDTO,
                                              BindingResult bindingResult,
                                              @RequestHeader(name = "Authorization") final String token,
                                              @RequestParam(required = false) MultipartFile immagineCopertina,
                                              BindingResult grandezzaImmagine) throws IOException {


        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }


        if (immagineCopertina != null) {
            if (!utils.immagineOk(immagineCopertina)) {
                return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
            }
            clubDTO.setCopertina(Utils.getBase64Image(immagineCopertina));
        }


        if (!utils.isUtenteEsperto(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }


        Esperto esperto = espertoService.findEspertoByEmail(utils.getSubjectFromToken(token));

        ClubDelLibro clubDelLibro =  clubService.creaClubDelLibro(clubDTO, esperto);
        List<ClubDelLibro> listaClub = esperto.getClubs();
        listaClub.add(clubDelLibro);
        espertoService.aggiornaEsperto(esperto);
        if(clubDelLibro == null) return new BiblionetResponse(BiblionetResponse.ERRORE, false);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

    /**
     * Implementa la funzionalità per la modifica dei dati di un Club.
     *
     * @param id       Lo Id del Club
     * @param clubDTO Il form dove inserire i nuovi dati
     * @return La schermata del club
     */
    @PostMapping(value = "/modifica")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse modificaDatiClub(final @RequestParam int id,
                                              @RequestHeader(name = "Authorization") final String token,
                                              final @Valid @ModelAttribute ClubDTO clubDTO,
                                              BindingResult bindingResult,
                                              @RequestParam(required = false) MultipartFile immagineCopertina) throws IOException {

        if (immagineCopertina != null) {
            if (!utils.immagineOk(immagineCopertina)) {
                return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
            }
            clubDTO.setCopertina(Utils.getBase64Image(immagineCopertina));
        }


        ClubDelLibro clubPers = this.clubService.getClubByID(id);
        if(clubPers == null) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);

        if (bindingResult.hasErrors()) {
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);
        }
        if (!utils.isUtenteEsperto(token) || !utils.match(utils.getSubjectFromToken(token), clubPers.getEsperto().getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        clubPers.setGeneri(new HashSet<>(clubDTO.getGeneri()));
        clubPers.setNome(clubDTO.getNome());
        clubPers.setDescrizione(clubDTO.getDescrizione());
        this.clubService.salvaClub(clubPers);
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    /**
     * Implementa la funzionalità che permette
     * a un lettore di abbandonare
     * Club del Libro.
     *
     * @param id l'ID del Club a cui iscriversi
     * @return La view che visualizza la lista dei club
     */
    @PostMapping(value = "/abbandono")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse abbandonaClub(final @RequestParam int id, @RequestHeader(name = "Authorization") final String token) {

        if (!utils.isUtenteLettore(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        ClubDelLibro clubDelLibro = this.clubService.getClubByID(id);
        boolean esito = lettoreService.abbandonaClub(clubDelLibro, lettore);
        if (esito) {
            return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
        }
        return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
    }

    /**
     * Implementa la funzionalità che permette
     * l'iscrizione di un lettore a un
     * Club del Libro.
     *
     * @param id l'ID del Club a cui iscriversi
     * @return La view che visualizza la lista dei club
     */
    @PostMapping(value = "/iscrizione")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse partecipaClub(final @RequestParam int id, @RequestHeader(name = "Authorization") final String token) {

        if (!utils.isUtenteLettore(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        ClubDelLibro clubDelLibro = clubService.getClubByID(id);

        if (clubDelLibro.getLettori().contains(lettore)) {
            return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_FALLITA, false);
        }
        lettoreService.partecipaClub(clubDelLibro, lettore);
        return new BiblionetResponse(BiblionetResponse.ISCRIZIONE_OK, true);
    }

    @GetMapping(value = "/partecipazione-lettore")
    @ResponseBody
    @CrossOrigin
    public boolean visualizzaPartecipazioneClubsLettore(
            final @RequestHeader(name = "Authorization") String token, final @RequestParam int idClub
    ) {
        //todo sostituire con una query diretta
        if (!utils.isUtenteLettore(token)) return false;
        List<ClubDelLibro> clubs =  lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token)).getClubs();
        for(ClubDelLibro club: clubs){
            if(idClub == club.getIdClub()) return true;
        }
        return false;
    }

    @GetMapping(value = "/visualizza-esperti-biblioteca")
    @ResponseBody
    @CrossOrigin
    public List<EspertoDTO> visualizzaEspertiBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        List<EspertoDTO> espertiDTO = new ArrayList<>();
        for(Esperto e: espertoService.getEspertiByBiblioteca(emailBiblioteca)){
            espertiDTO.add(new EspertoDTO(e));
        }
        return espertiDTO;
    }

    @GetMapping(value = "/visualizza-clubs-biblioteca")
    @ResponseBody
    @CrossOrigin
    public Set<ClubDTO> visualizzaClubBiblioteca(
            @RequestParam final String emailBiblioteca
    ) {
        Set<ClubDelLibro> clubs = new HashSet<>();
        List<Esperto> esperti = espertoService.getEspertiByBiblioteca(emailBiblioteca);
        for(Esperto esperto: esperti){
            System.err.println(esperto.getClubs());
            clubs.addAll(esperto.getClubs());
        }
        Set<ClubDTO> clubDTOS = new HashSet<>();
        for(ClubDelLibro clubDelLibro: clubs){
            clubDTOS.add(new ClubDTO(clubDelLibro));
        }
        return clubDTOS;
    }

    /**
     * Implementa la funzionalità che permette di gestire
     * la visualizzazione dei dati di un Club del Libro.
     * @param id l'ID del Club di cui visualizzare i dati
     * @return La view che visualizza i dati
    */
     @PostMapping(value = "/info-club")
     @CrossOrigin
     @ResponseBody
     public Map<String, Object> visualizzaDatiClub(final @RequestParam int id) {

         ClubDelLibro club = clubService.getClubByID(id);
         if(club == null) return null;


         Map<String, Object> clubData = new HashMap<>();
         clubData.put("nome", club.getNome());
         clubData.put("descrizione", club.getDescrizione());
         clubData.put("nomeEsperto", club.getEsperto().getNome() + " " + club.getEsperto().getCognome());
         clubData.put("emailEsperto", club.getEsperto().getEmail());
         clubData.put("immagineCopertina", club.getImmagineCopertina());
         clubData.put("generi", club.getGeneri());
         clubData.put("idClub", club.getIdClub());
         clubData.put("iscritti", club.getLettori().size());
         return clubData;
     }


    @PostMapping(value = "/lettori-club")
    @CrossOrigin
    @ResponseBody
    public List<LettoreDTO> visualizzaLettoriClub(final @RequestParam int id) {
         List<LettoreDTO> lettoriDTO = new ArrayList<>();
        for(Lettore l: clubService.getClubByID(id).getLettori()){
                lettoriDTO.add(new LettoreDTO(l));
        }
        return lettoriDTO;
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli iscritti a un club.
     *
     * @param id L'identificativo del club
     * @return La view della lista degli iscritti
     */
    @GetMapping(value = "/{id}/iscritti")
    public ClubDelLibro visualizzaIscrittiClub(final @PathVariable int id) {
        return clubService.getClubByID(id);
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli eventi di un club.
     *
     * @param id l'ID del club
     * @return la view che visualizza gli eventi
     */
    @PostMapping(value = "/eventi-club")
    @CrossOrigin
    @ResponseBody
    public List<EventoDTO> visualizzaListaEventiClub(final @RequestParam int id) {

        ClubDelLibro clubDelLibro = clubService.getClubByID(id);

        if (clubDelLibro == null) {
            return null;
        }

        return eventiService.getInformazioniEventi(clubDelLibro.getEventi());
    }


}