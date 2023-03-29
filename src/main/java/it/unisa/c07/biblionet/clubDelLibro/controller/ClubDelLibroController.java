package it.unisa.c07.biblionet.clubDelLibro.controller;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import it.unisa.c07.biblionet.dto.VisualizzaCreaClubLibroResponse;
import it.unisa.c07.biblionet.dto.VisualizzaListClubResponse;
import it.unisa.c07.biblionet.dto.VisualizzaModificaDatiClub;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import it.unisa.c07.biblionet.clubDelLibro.service.ClubDelLibroService;
import it.unisa.c07.biblionet.model.entity.ClubDelLibro;
import it.unisa.c07.biblionet.model.entity.Evento;
import it.unisa.c07.biblionet.model.entity.Genere;
import it.unisa.c07.biblionet.model.entity.utente.Lettore;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.model.form.ClubForm;
import it.unisa.c07.biblionet.model.form.EventoForm;
import it.unisa.c07.biblionet.utils.validazione.ValidazioneEvento;
import lombok.RequiredArgsConstructor;


/**
 * Implementa il controller per il sottosistema
 * ClubDelLibro.
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/club-del-libro")
//@SessionAttributes("loggedUser")
public class ClubDelLibroController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final ClubDelLibroService clubService;

    /**
     * Il service per effettuare le operazioni di persistenza
     * degli eventi.
     */
//    private final GestioneEventiService eventiService;


    /**
     * Metodo di utilità che modifica o crea un evento, validando
     * i dati.
     * @param eventoForm Il form con i dati da modificare
     * @param view La view da restituire se l'operazione va a buon fine.
     * @param idClub L'id del club del libro in cui inserire l'evento.
     * @param idEvento L'id dell'evento, che può essere vuoto per ottenere
     *                 l'autoassegnazione.
     * @param operazione L'operazione, tra creazione e modifica, che si vuole
     *                   effettuare.
     * @return La view inserita.

    private String modificaCreaEvento(final EventoForm eventoForm,
                                      final String view,
                                      final int idClub,
                                      final Optional<Integer> idEvento,
                                      final Consumer<Evento> operazione) {

        var club = this.clubService.getClubByID(idClub);

        if (club == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Club del Libro Inesistente"
            );
        }

        var evento = new Evento();

        if (idEvento.isPresent()) {
            evento.setIdEvento(idEvento.get());
        }

        evento.setClub(club);

        if (!ValidazioneEvento.isNomeValido(eventoForm.getNome())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Lunghezza del nome non valida."
            );
        }

        evento.setNomeEvento(eventoForm.getNome());

        if (!ValidazioneEvento.
                isDescrizioneValida(eventoForm.getDescrizione())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Lunghezza della descrizione non valida."
            );
        }

        evento.setDescrizione(eventoForm.getDescrizione());

        var dataOra =
                LocalDateTime.of(eventoForm.getData(), eventoForm.getOra());

        if (dataOra.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Data non valida."
            );
        }

        evento.setDataOra(dataOra);

        if (eventoForm.getLibro() != null) {
            var libro =
                    this.eventiService.getLibroById(eventoForm.getLibro());
            if (libro.isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Il libro inserito non è valido."
                );
            }
            evento.setLibro(libro.get());
        }

        operazione.accept(evento);

        return view;

    }*/

    /**
     * Implementa la funzionalità che permette
     * di visualizzare i Club del Libro
     * presenti nel Database.
     * @param generi Un Optional che contiene una lista di generi per cui
     *               filtrare
     * @param citta Un Optional che contiene una lista di possibili città
     * @return La pagina di visualizzazione
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public VisualizzaListClubResponse visualizzaListaClubs(@RequestParam(value = "generi")
                                                final Optional<List<String>>
                                                generi,
                                       @RequestParam(value = "citta")
                                                final Optional<List<String>>
                                                citta) {

        VisualizzaListClubResponse response= new VisualizzaListClubResponse();
        // Molto più pulito della concatenazione con gli stream
        Predicate<ClubDelLibro> filtroGenere = x -> true;

        if (generi.isPresent()) {
                filtroGenere = x -> false;

                var generiDaDB =
                        clubService.getGeneri(generi.get());

                for (Genere genere: generiDaDB) {
                        filtroGenere = filtroGenere.or(
                                c -> c.getGeneri().contains(genere)
                        );
                }
        }

        Predicate<ClubDelLibro> filtroCitta = x -> true;

        if (citta.isPresent()) {
                filtroCitta = x -> false;
                for (String cittaSingola: citta.get()) {
                        filtroCitta = filtroCitta.or(
                                c -> clubService.getCittaFromClubDelLibro(c)
                                                .equals(cittaSingola)
                        );
                }
        }

        List<ClubDelLibro> listaClubs = clubService.visualizzaClubsDelLibro(
                filtroCitta.and(filtroGenere)
        );

        response.setListaClubs(listaClubs);
        // Necessito di un oggetto anonimo per evitare problemi con JS


        response.setGeneri(this.clubService.getTuttiGeneri());
        response.setGeneri(this.clubService.getCitta());

        return response;
    }

    /**
     * Implementa la funzionalità di visualizzare la pagina di creazione di
     * un club del libro.
     * @param club Il form in cui inserire i dati del club
     * @return La pagina del Club
     */
    @PostMapping("crea")
    public VisualizzaCreaClubLibroResponse visualizzaCreaClubDelLibro(@RequestBody  ClubForm club) {
        //TODO:GESTIRE UTENZA E CLUBFORM DEVE ESSERE INVIATO
        //var utente = (UtenteRegistrato) model.getAttribute("loggedUser");
//        if (utente == null || !utente.getTipo().equals("Esperto")) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
        VisualizzaCreaClubLibroResponse response= new VisualizzaCreaClubLibroResponse();
        response.setGeneri(this.clubService.getTuttiGeneri());
        response.setClub(club);
        return response;
    }

    /**
     * Implementa la funzionalità di creazione di un club del libro.
     * @param club Il club che si vuole creare
     * @return la pagina del Club
     */
    @PostMapping("/create")
    public Boolean creaClubDelLibro(@RequestBody ClubForm club) {

        //TODO:GESTIRE UTENZA
//        UtenteRegistrato utente =
//                (UtenteRegistrato) model.getAttribute("loggedUser");
//        if (utente == null || !utente.getTipo().equals("Esperto")) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//        }
        //var esperto = (Esperto) utente;
        ClubDelLibro cdl = new ClubDelLibro();
        cdl.setNome(club.getNome());
        cdl.setDescrizione(club.getDescrizione());
        //cdl.setEsperto(esperto);
        if (club.getCopertina() != null && !club.getCopertina().isEmpty()) {
                try {
                        byte[] imageBytes = club.getCopertina().getBytes();
                        String base64Image =
                                Base64.getEncoder().encodeToString(imageBytes);
                        cdl.setImmagineCopertina(base64Image);
                        } catch (IOException e) {
                        e.printStackTrace();
                        }
        }

        cdl.setGeneri(Arrays.asList(new Genere[] {}));
        if (club.getGeneri() != null) {
            cdl.setGeneri(
                    this.clubService.getGeneri(
                            club.getGeneri()
                )
            );
        }
        if(this.clubService.creaClubDelLibro(cdl) !=null)
            return true;
        else
            return false;
    }

    /**
     * Implementa la funzionalità che permette
     * di re-indirizzare alla pagina di modifica
     * dei dati di un Club del Libro.
     * @param id l'ID del Club da modificare
     * @param club Il club che si vuole creare
     * @return La view che visualizza il form di modifica dati
     */
    @RequestMapping(value = "/{id}/modifica", method = RequestMethod.POST)
    public VisualizzaModificaDatiClub visualizzaModificaDatiClub(final @PathVariable int id,
                                            @RequestBody ClubForm club
                                             ) {
        //GESTIRE UTENZA
        //var esperto = (UtenteRegistrato) model.getAttribute("loggedUser");
        var cdl = this.clubService.getClubByID(id);
        if (cdl == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
//        if (esperto == null
//                || !cdl.getEsperto().getEmail().equals(esperto.getEmail())) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        club.setNome(cdl.getNome());
        club.setDescrizione(cdl.getDescrizione());
        club.setGeneri(cdl.getGeneri().stream().map(Genere::getNome).
                collect(Collectors.toList()));

        VisualizzaModificaDatiClub vm =new VisualizzaModificaDatiClub();
        vm.setClub(club);
        vm.setId(id);
        vm.setGeneri(this.clubService.getTuttiGeneri());
        return vm;
    }

    /**
     * Implementa la funzionalità per la modifica dei dati di un Club.
     * @param id Lo Id del Club
     * @param club Il form dove inserire i nuovi dati
     * @return La schermata del club
     */
    @RequestMapping(value = "/{id}/modifica-dati",
            method = RequestMethod.POST)
    public Boolean modificaDatiClub(final @PathVariable int id,
                                   final @RequestBody ClubForm club) {

        ClubDelLibro clubPers = this.clubService.getClubByID(id);
        if (!club.getCopertina().isEmpty()) {
            try {
                byte[] imageBytes = club.getCopertina().getBytes();
                String base64Image = Base64.getEncoder()
                        .encodeToString(imageBytes);
                clubPers.setImmagineCopertina(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (club.getGeneri() != null) {
            List<Genere> gList = clubService.getGeneri(club.getGeneri());
            clubPers.setGeneri(gList);
        }
        clubPers.setNome(club.getNome());
        clubPers.setDescrizione(club.getDescrizione());
        if(this.clubService.modificaDatiClub(clubPers) !=null)
            return true;
        else
            return false;
    }

    /**
     * Implementa la funzionalità che permette
     * l'iscrizione di un lettore ad un
     * Club del Libro.
     * @param id l'ID del Club a cui iscriversi
     * @param model Il model da passare alla view
     * @return La view che visualizza la lista dei club
     */
    @RequestMapping(value = "/{id}/iscrizione", method = RequestMethod.POST)
    public String partecipaClub(final @PathVariable int id,
                                final Model model) {

        //TODO:GESTIRE UTENZA PER SBLOCCARE SERVIZIO
//        UtenteRegistrato lettore =
//                (UtenteRegistrato) model.getAttribute("loggedUser");
//        if (lettore == null || !lettore.getTipo().equals("Lettore")) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//        ClubDelLibro clubDelLibro = this.clubService.getClubByID(id);
//        if (clubDelLibro.getLettori().contains(lettore)) {
//            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
//        }
//        this.clubService.partecipaClub(
//                clubDelLibro,
//                (Lettore) lettore);
        return "redirect:/club-del-libro/";
    }

    /**
     * Implementa la funzionalità che permette
     * la visualizzazione della modifica dei dati di
     * un evento di un Club del Libro.
     * @param idClub l'ID del Club
     * @param idEvento l'ID dell'evento
     * @param evento il form dell'evento
     * @return La view che visualizza la lista dei club
    @RequestMapping(
        value = "/{idClub}/eventi/{idEvento}/modifica",
        method = RequestMethod.GET
    )
    public VisualizzaModificaEventoResponse visualizzaModificaEvento(final @PathVariable int idClub,
                                           final @PathVariable int idEvento,
                                           @RequestBody
                                                       EventoForm evento) {
        var eventoBaseOpt =
                this.eventiService.getEventoById(idEvento);
        //var esperto = (UtenteRegistrato) model.getAttribute("loggedUser");

        if (eventoBaseOpt.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Evento Inesistente"
            );
        }

//        if (esperto != null && !eventoBaseOpt.get().getClub().getEsperto()
//                .getEmail().equals(esperto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }

        var eventoBase = eventoBaseOpt.get();

        if (eventoBase.getClub().getIdClub() != idClub) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "L'evento con id " + idEvento
                + "non è associato al club con id "
                + idClub + "."
            );
        }

        evento.setNome(eventoBase.getNomeEvento());
        evento.setData(eventoBase.getDataOra().toLocalDate());
        evento.setOra(eventoBase.getDataOra().toLocalTime());
        evento.setDescrizione(eventoBase.getDescrizione());
        if (eventoBase.getLibro() != null) {
            evento.setLibro(eventoBase.getLibro().getIdLibro());
        }

        VisualizzaModificaEventoResponse vr= new VisualizzaModificaEventoResponse();
        vr.setClub(eventoBase.getClub());
        vr.setId(eventoBase.getIdEvento());

        return vr;
    }*/

    /**
     * Implementa la funzionalità che permette
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     * @param id l'id dell'evento
     * @param eventoForm il form dell'evento
     * @return la view della lista degli eventi

    @RequestMapping(value = "/{id}/eventi/crea", method = RequestMethod.POST)
    public String creaEvento(final @PathVariable int id,
                             final @ModelAttribute EventoForm eventoForm) {
        return this.modificaCreaEvento(
            eventoForm,
            "redirect:/club-del-libro/" + id,
            id,
            Optional.empty(),
            this.eventiService::creaEvento

        );
    }*/

    /**
     * Implementa la funzionalità che permette la modifica di un evento.
     * @param idClub l'ID del club
     * @param idEvento l'ID dell'evento
     * @param eventoForm il form dell'evento
     * @return la view che visualizza la lista degli eventi

    @RequestMapping(value = "/{idClub}/eventi/{idEvento}/modifica",
            method = RequestMethod.POST)
    public String modificaEvento(final @PathVariable int idClub,
                                 final @PathVariable int idEvento,
                                 final @ModelAttribute EventoForm eventoForm) {
        return this.modificaCreaEvento(
            eventoForm,
            "redirect:/club-del-libro/" + idClub,
            idClub,
            Optional.of(idEvento),
            evento -> {
                var statusModifica =
                        this.eventiService.modificaEvento(evento);
                if (statusModifica.isEmpty()) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "L'evento con id " + idEvento
                        + "non è associato al club con id "
                        + idClub + "."
                    );
                }
            }
        );
    }*/

    /**
     * Implementa la funzionalità che permette
     * la creazione da parte di un Esperto
     * di un Evento.
     * @param id l'ID dell'evento
     * @param evento il form dell'evento
     * @param model il model da passare alla view
     * @return La view che visualizza il form di creazione Evento
     */
    @GetMapping("/{id}/eventi/crea")
    public String visualizzaCreaEvento(final @PathVariable int id,
                                       final @ModelAttribute EventoForm evento,
                                       final Model model) {
        var club = this.clubService.getClubByID(id);

        if (club == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Club del Libro Inesistente"
            );
        }

        model.addAttribute("club", club);
        model.addAttribute("evento", evento);

        return "club-del-libro/aggiungi-evento";
    }

    /**
     * Implementa la funzionalità che permette di gestire
     * la visualizzazione dei dati di un Club del Libro.
     * @param id l'ID del Club di cui visualizzare i dati
     * @param model il model per il passaggio dei dati
     * @return La view che visualizza i dati
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    public String visualizzaDatiClub(final @PathVariable int id,
                                     final Model model) {
        model.addAttribute("club", clubService.getClubByID(id));
        return "club-del-libro/visualizza-singolo-club";
    }

    /**
     * Implementa la funzionalità che permette di eliminare
     * un evento.
     * @param club L'identificativo del Club dell'evento
     * @param id L'identificativo dell'evento da eliminare
     * @return La view della lista degli eventi
     */
  /*  @RequestMapping(value = "/{club}/eventi/{id}",
            method = RequestMethod.GET)
    public String eliminaEvento(final @PathVariable int club,
                                final @PathVariable int id) {
        Optional<Evento> eventoEliminato =
                this.eventiService.eliminaEvento(id);

        System.out.println(eventoEliminato);


        if (eventoEliminato.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Evento Inesistente"
            );
        }

        return "redirect:/club-del-libro/" + club;
    }*/

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli iscritti a un club.
     * @param id L'identificativo del club
     * @param model il model la passare alla view
     * @return La view della lista degli iscritti
     */
    @RequestMapping(value = "/{id}/iscritti",
            method = RequestMethod.GET)
    public String visualizzaIscrittiClub(final @PathVariable int id,
                                         final Model model) {
        model.addAttribute("club", clubService.getClubByID(id));
        return "club-del-libro/visualizza-iscritti";
    }

    /**
     * Implementa la funzionalità che permette di visualizzare
     * la lista degli eventi di un club.
     * @param id l'ID del club
     * @param model il mdoel da passare alla view
     * @return la view che visualizza gli eventi
     */
    @RequestMapping(value = "/{id}/eventi",
            method = RequestMethod.GET)
    public String visualizzaListaEventiClub(final @PathVariable int id,
                                            final Model model) {
        if (clubService.getClubByID(id) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UtenteRegistrato utente =
                (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Lettore l = (Lettore) utente;
        List<Evento> tutti = clubService.getClubByID(id).getEventi();
        List<Evento> mieiEventi = l.getEventi();
        List<Evento> mieiEventiClub = new ArrayList<>();
        for (Evento e : mieiEventi) {
            if (e.getClub().getIdClub() == id) {
                mieiEventiClub.add(e);
            }
        }
        for (Evento e : mieiEventiClub) {
            if (tutti.contains(e)) {
                tutti.remove(e);
            }
        }
        model.addAttribute("club", clubService.getClubByID(id));
        model.addAttribute("eventi", tutti);
        model.addAttribute("mieiEventi", mieiEventiClub);

        return "club-del-libro/visualizza-eventi";
    }

    /**
     * Implementa la funzionalità che permette di iscriversi
     * ad uno degli eventi presenti nella lista relativa ad
     * un Club del Libro.
     * @param idEvento l'evento a cui partecipare
     * @param idClub il club dell'evento
     * @param model l'oggetto Model da cui ottenere il lettore autenticato
     * @return la view che visualizza la lista degli eventi
    @RequestMapping(value = "/{idClub}/eventi/{idEvento}/iscrizione",
            method = RequestMethod.GET)
    public String partecipaEvento(final @PathVariable int idEvento,
                                  final @PathVariable int idClub,
                                  final Model model) {
        UtenteRegistrato utente =
                (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        model.addAttribute("loggedUser",
                eventiService.partecipaEvento(utente.getEmail(), idEvento));
        return "redirect:/club-del-libro/" + idClub + "/eventi";
    }*/

    /**
     * Implementa la funzionalità che permette di disiscriversi
     * ad uno degli eventi presenti nella lista relativa ad
     * un Club del Libro a cui ci si era precedentemente iscritti.
     * @param idEvento l'evento a cui disiscriversi
     * @param idClub il club dell'evento
     * @param model l'oggetto Model da cui ottenere il lettore autenticato
     * @return la view che visualizza la lista degli eventi
    @RequestMapping(value = "/{idClub}/eventi/{idEvento}/abbandono",
            method = RequestMethod.GET)
    public String abbandonaEvento(final @PathVariable int idEvento,
                                  final @PathVariable int idClub,
                                  final Model model) {
        UtenteRegistrato utente =
                (UtenteRegistrato) model.getAttribute("loggedUser");
        if (utente == null || !utente.getTipo().equals("Lettore")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        model.addAttribute("loggedUser",
                eventiService.abbandonaEvento(utente.getEmail(), idEvento));
        return "redirect:/club-del-libro/" + idClub + "/eventi";
    }*/
}