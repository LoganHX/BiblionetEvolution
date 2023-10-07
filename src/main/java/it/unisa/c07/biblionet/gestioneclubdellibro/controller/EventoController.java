package it.unisa.c07.biblionet.gestioneclubdellibro.controller;

import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/gestione-eventi")
public class EventoController {

    private final GestioneEventiService eventiService;
    private final ClubDelLibroService clubService;
    private final LettoreService lettoreService;
    private final PrenotazioneLibriService prenotazioneLibriService;
    private final Utils utils;

    /**
     * Implementa la funzionalità che permette di disiscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro a cui ci si era precedentemente iscritti.
     *
     * @param idEvento l'evento a cui disiscriversi
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/eventi/{idEvento}/abbandono")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse abbandonaEvento(final @PathVariable int idEvento, @RequestHeader(name = "Authorization") final String token) {
        //Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        if (!utils.isUtenteLettore(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(eventiService.isLettoreIscrittoEvento(idEvento, utils.getSubjectFromToken(token)) != null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if (l == null) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        if(eventiService.abbandonaEvento(l.getEmail(), idEvento) != null) return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
        return new BiblionetResponse(BiblionetResponse.ERRORE, false);
    }


    /**
     * Implementa la funzionalità che permette d'iscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro.
     *
     * @param idEvento l'evento a cui partecipare
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/eventi/{idEvento}/iscrizione")
    public LettoreDTO partecipaEvento(final @PathVariable int idEvento, @RequestHeader(name = "Authorization") final String token) {


        if (!utils.isUtenteLettore(token)) return null;
        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if (l == null) return null;
        return new LettoreDTO(eventiService.partecipaEvento(l.getEmail(), idEvento));
    }



    /**
     * Implementa la funzionalità che permette di eliminare
     * un evento.
     *
     * @param idClub L'identificativo del Club dell'evento
     * @param idEvento   L'identificativo dell'evento da eliminare
     * @return La view della lista degli eventi
     */
    @PostMapping(value = "/elimina-evento")
    public BiblionetResponse eliminaEvento(final @RequestParam int idClub,
                                           final @RequestParam int idEvento,
                                           @RequestHeader(name = "Authorization") final String token) {

        ClubDelLibro clubDelLibro = clubService.getClubByID(idClub);
        BiblionetResponse br = checkPermessi(clubDelLibro, token);
        if(br != null) return br;

        Optional<Evento> eventoEliminato = this.eventiService.eliminaEvento(idEvento);

        if (eventoEliminato.isEmpty()) {
            return new BiblionetResponse("Evento inesistente", false);
        }

        return new BiblionetResponse("Evento eliminato", true);
    }

    /**
     * Implementa la funzionalità che permette la modifica di un evento.
     * @param idClub l'ID del club
     * @param idEvento l'ID dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @PostMapping(value = "/modifica")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse modificaEvento(final @RequestParam Integer idClub,
                                            final @RequestParam Integer idEvento,
                                            final @RequestParam (required = false) Integer idLibro,
                                            final @Valid @ModelAttribute EventoDTO eventoDTO, BindingResult bindingResult,
                                            @RequestHeader(name = "Authorization") final String token ,final @RequestParam String timeString,
                                            final @RequestParam String dateString) {
       System.err.println(token);
        eventoDTO.setData(LocalDate.parse(dateString));
        eventoDTO.setOra(LocalTime.parse(timeString));

        ClubDelLibro clubDelLibro = clubService.getClubByID(idClub);
        BiblionetResponse br = checkPermessi(clubDelLibro, token);
        if(br != null) return br;


        Optional<Evento> e = eventiService.getEventoById(idEvento);
        if(e.isEmpty()) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        if(e.get().getClub().getIdClub() != idClub) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        //if(!utils.isUtenteEsperto(token) || !utils.getSubjectFromToken(token).equals(e.get().getClub().getEsperto().getEmail()))
        //    return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        return this.modificaCreaEvento(
                eventoDTO,
                bindingResult,
                clubDelLibro,
                Optional.of(idEvento),
                Optional.ofNullable(idLibro)

        );
    }

    /**
     * Implementa la funzionalità che permette
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     *
     * @param idClub         l'id dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view della lista degli eventi
     */
    @PostMapping(value = "/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaEvento(final @RequestParam Integer idClub,
                                        final @RequestParam (required = false) Integer idLibro,
                                        final @Valid @ModelAttribute EventoDTO eventoDTO,
                                        BindingResult bindingResult,
                                        final @RequestParam @NonNull String timeString,
                                        final @RequestParam @NonNull String dateString, @RequestHeader(name = "Authorization") final String token) {
        eventoDTO.setData(LocalDate.parse(dateString));
        eventoDTO.setOra(LocalTime.parse(timeString));


        ClubDelLibro clubDelLibro = clubService.getClubByID(idClub);
        BiblionetResponse br = checkPermessi(clubDelLibro, token);
        if(br != null) return br;

        return this.modificaCreaEvento(
                        eventoDTO,
                        bindingResult,
                        clubDelLibro,
                        Optional.empty(),
                Optional.ofNullable(idLibro)
        );
    }

    /**
     * Metodo di utilità che modifica o crea un evento, validando
     * i dati.
     *
     * @param eventoDTO Il form con i dati da modificare
     * @param club    club del libro in cui inserire l'evento.
     * @param idEvento  L'id dell'evento, che può essere vuoto per ottenere
     *                  l'autoassegnazione.
     * @return La view inserita.
     */
    private BiblionetResponse modificaCreaEvento(final EventoDTO eventoDTO, BindingResult bindingResult,
                                                 final ClubDelLibro club,
                                                 final Optional<Integer> idEvento, final Optional<Integer> idLibro ) {

        if (bindingResult.hasErrors())
            return new BiblionetResponse(BiblionetResponse.FORMATO_NON_VALIDO, false);

        var evento = new Evento();

        idEvento.ifPresent(evento::setIdEvento);//if evento is present

        evento.setClub(club);
        evento.setNomeEvento(eventoDTO.getNome());
        evento.setDescrizione(eventoDTO.getDescrizione());

        var dataOra = LocalDateTime.of(eventoDTO.getData(), eventoDTO.getOra());
        if (dataOra.isBefore(LocalDateTime.now())) {
            return new BiblionetResponse("Data non valida", false);
        }

        evento.setDataOra(dataOra);

        if (idLibro.isPresent()) {
            var libro = this.eventiService.getLibroById(idLibro.get());
            if (libro.isEmpty()) {
                return new BiblionetResponse("Libro inserito non valido", false);
            }
            evento.setLibro(libro.get());
        }
        eventiService.modificaEvento(evento);

        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);

    }

    private BiblionetResponse checkPermessi(ClubDelLibro club, String token){
        if(club == null)
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        if (!utils.isUtenteEsperto(token) || !utils.match(utils.getSubjectFromToken(token), club.getEsperto().getEmail())) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }

        return null;
    }

}
