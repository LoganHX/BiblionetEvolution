package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/gestione-eventi")
public class EventoController {

    private final GestioneEventiService eventiService;
    private final ClubDelLibroService clubService;
    private final LettoreService lettoreService;
    private final EspertoService espertoService;
    private final Utils utils;


    /**
     * Implementa la funzionalità che permette di disiscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro a cui ci si era precedentemente iscritti.
     *
     * @param idEvento l'evento a cui disiscriversi
     * @param idClub   il club dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/{idClub}/eventi/{idEvento}/abbandono")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse abbandonaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, @RequestHeader(name = "Authorization") final String token) {
        //Lettore lettore = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));
        if (!utils.isUtenteLettore(token)) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
//        boolean eventoOK = false;
//
//        for(Evento e: lettore.getEventi()){
//            if(e.getIdEvento() == idEvento){
//                eventoOK = true;
//                break;
//            }
//        }
//
//        if(!eventoOK) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        if(eventiService.isLettoreIscrittoEvento(idEvento, utils.getSubjectFromToken(token)) != null) return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if (l == null) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        if(eventiService.abbandonaEvento(l.getEmail(), idEvento) != null) return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);;
        return new BiblionetResponse(BiblionetResponse.ERRORE, false);
    }


    /**
     * Implementa la funzionalità che permette d'iscriversi
     * a uno degli eventi presenti nella lista relativa a
     * un Club del Libro.
     *
     * @param idEvento l'evento a cui partecipare
     * @param idClub   il club dell'evento
     * @return la view che visualizza la lista degli eventi
     */
    @GetMapping(value = "/{idClub}/eventi/{idEvento}/iscrizione")
    public LettoreDTO partecipaEvento(final @PathVariable int idEvento, final @PathVariable int idClub, @RequestHeader(name = "Authorization") final String token) {


        if (!utils.isUtenteLettore(token)) return null;
        Lettore l = lettoreService.findLettoreByEmail(utils.getSubjectFromToken(token));

        if (l == null) return null;
        return new LettoreDTO(eventiService.partecipaEvento(l.getEmail(), idEvento));
    }



    /**
     * Implementa la funzionalità che permette di eliminare
     * un evento.
     *
     * @param club L'identificativo del Club dell'evento
     * @param id   L'identificativo dell'evento da eliminare
     * @return La view della lista degli eventi
     */
    @PostMapping(value = "/elimina-evento")
    public BiblionetResponse eliminaEvento(final @RequestParam int club, final @RequestParam int id) {
        //todo possibile non ci sia un check sul token?
        Optional<Evento> eventoEliminato = this.eventiService.eliminaEvento(id);

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
    @PostMapping(value = "/eventi/modifica")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse modificaEvento(final @RequestParam int idClub,
                                            final @RequestParam int idEvento,
                                            final @Valid @ModelAttribute EventoDTO eventoDTO, BindingResult bindingResult,
                                            @RequestHeader(name = "Authorization") final String token) {


        Optional<Evento> e = eventiService.getEventoById(idEvento);
        if(e.isEmpty()) return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        //System.out.println(e.get().getClub().getIdClub() + " <> " + idClub);
        if(e.get().getClub().getIdClub() != idClub) return new BiblionetResponse(BiblionetResponse.ERRORE, false);

        //System.out.println(e.get().getClub().getEsperto().getEmail());

        if(!utils.isUtenteEsperto(token) || !utils.getSubjectFromToken(token).equals(e.get().getClub().getEsperto().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        return this.modificaCreaEvento(
                eventoDTO,
                bindingResult,
                idClub,
                Optional.of(idEvento)


        );
    }

    /**
     * Implementa la funzionalità che permette
     * di gestire la chiamata POST
     * per creare un evento un club del libro.
     *
     * @param id         l'id dell'evento
     * @param eventoDTO il form dell'evento
     * @return la view della lista degli eventi
     */
    @PostMapping(value = "/eventi/crea")
    @CrossOrigin
    @ResponseBody
    public BiblionetResponse creaEvento(final @RequestParam int id,
                                        final @Valid @ModelAttribute EventoDTO eventoDTO,
                                        BindingResult bindingResult) {
        return this.modificaCreaEvento(
                eventoDTO,
                bindingResult,
                id,
                Optional.empty()
        );
    }

    /**
     * Metodo di utilità che modifica o crea un evento, validando
     * i dati.
     *
     * @param eventoDTO Il form con i dati da modificare
     * @param idClub    L'id del club del libro in cui inserire l'evento.
     * @param idEvento  L'id dell'evento, che può essere vuoto per ottenere
     *                  l'autoassegnazione.
     * @return La view inserita.
     */
    private BiblionetResponse modificaCreaEvento(final EventoDTO eventoDTO, BindingResult bindingResult,
                                                 //@RequestParam final String view,
                                                 final int idClub, final Optional<Integer> idEvento) {

        if (bindingResult.hasErrors())
            return new BiblionetResponse(BiblionetResponse.RICHIESTA_NON_VALIDA, false);
        var club = this.clubService.getClubByID(idClub);

        if (club == null) {
            return new BiblionetResponse(BiblionetResponse.OGGETTO_NON_TROVATO, false);
        }

        var evento = new Evento();

        if (idEvento.isPresent()) {
            evento.setIdEvento(idEvento.get());
        }

        evento.setClub(club);
        evento.setNomeEvento(eventoDTO.getNome());
        evento.setDescrizione(eventoDTO.getDescrizione());

        var dataOra = LocalDateTime.of(eventoDTO.getData(), eventoDTO.getOra());
        if (dataOra.isBefore(LocalDateTime.now())) {
            return new BiblionetResponse("Data non valida", false);
        }

        evento.setDataOra(dataOra);

        if (eventoDTO.getLibro() != null) {
            var libro = this.eventiService.getLibroById(eventoDTO.getLibro());
            if (libro.isEmpty()) {
                return new BiblionetResponse("Libro inserito non valido", false);
            }
            evento.setLibro(libro.get());
        }

        return new BiblionetResponse("Evento creato/modificato", true);

    }


}
