package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.ILibroIdAndName;
import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.TicketPrestitoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementa il controller per il sottosistema
 * PrenotazioneLibri.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/prenotazione-libri")
public class PrenotazioneLibriController {

    /**
     * Il service per effettuare le operazioni di
     * persistenza.
     */
    private final PrenotazioneLibriService prenotazioneService;
    private final BibliotecaService bibliotecaService;
    private final Utils utils;

    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param idLibro      L'ID del libro di cui effettuare la prenotazione
     * @return La view che visualizza la lista dei libri prenotabili
     */
    @PostMapping(value = "/conferma-prenotazione")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse confermaPrenotazione(@RequestParam final String emailBiblioteca,
                                                  @RequestParam final String idLibro,
                                                  @RequestHeader(name = "Authorization") final String token) {

        if (!utils.isUtenteLettore(token)) {
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);
        }
        String subject = utils.getSubjectFromToken(token);
        prenotazioneService.richiediPrestito(subject,
                emailBiblioteca,
                Integer.parseInt(idLibro));
        return new BiblionetResponse(BiblionetResponse.OPERAZIONE_OK, true);
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutti i libri prenotabili.
     *
     * @return La view per visualizzare i libri
     */
    @GetMapping(value = "")
    @ResponseBody
    @CrossOrigin
    public List<LibroDTO> visualizzaListaLibri() {
        return prenotazioneService.getInformazioniLibri(prenotazioneService.visualizzaListaLibriCompleta());
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare tutti i libri filtrati.
     *
     * @param stringa La stringa di ricerca
     * @param filtro  L'informazione su cui filtrare
     * @return La view che visualizza la lista
     */
    @GetMapping(value = "/ricerca")
    @ResponseBody
    @CrossOrigin
    public List<LibroDTO> visualizzaListaFiltrata(
            @RequestParam("stringa") final String stringa,
            @RequestParam("filtro") final String filtro) {

        return switch (filtro) {
            case "titolo" ->
                    prenotazioneService.getInformazioniLibri(prenotazioneService.visualizzaListaLibriPerTitolo(stringa));
            case "genere" ->
                    prenotazioneService.getInformazioniLibri(prenotazioneService.visualizzaListaLibriPerGenere(stringa));
            case "biblioteca" ->
                    prenotazioneService.getInformazioniLibri(prenotazioneService.visualizzaListaLibriPerBiblioteca(stringa));
            default -> prenotazioneService.getInformazioniLibri(prenotazioneService.visualizzaListaLibriCompleta());
        };
    }

    /**
     * Implementa la funzionalità che permette di
     * visualizzare le biblioteche presso cui è
     * possibile prenotare il libro.
     *
     * @param id L'ID del libro di cui effettuare la prenotazione
     * @return La view che visualizza la lista delle biblioteche
     */
    @GetMapping(value = "/{id}/visualizza-libro")
    @ResponseBody
    @CrossOrigin
    public List<BibliotecaDTO> prenotaLibro(@PathVariable final int id) {
        return bibliotecaService.getInformazioniBiblioteche(prenotazioneService.getBibliotecheLibro(prenotazioneService.getLibroByID(id)));
    }

    @PostMapping(value = "/ottieni-libro")
    @ResponseBody
    @CrossOrigin
    public LibroDTO ottieniLibro(@RequestParam final int id, @RequestHeader(name = "Authorization") final String token
    ) {
        if (!utils.isUtenteLettore(token)) return null;
        return new LibroDTO( prenotazioneService.getLibroByID(id));
    }


    /**
     * Implementa la funzionalità che permette di
     * a una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @return La view che visualizza la lista delle richieste
     */
    @PostMapping(value = "/visualizza-richieste")
    @ResponseBody
    @CrossOrigin
    public Map<String, List<TicketPrestitoDTO>> visualizzaRichieste(
            @RequestHeader(name = "Authorization") final String token
    ) {
        if (!utils.isUtenteBiblioteca(token)) {
            return null;
        }
        Biblioteca biblioteca = bibliotecaService.findBibliotecaByEmail(utils.getSubjectFromToken(token));

        Map<String, List<TicketPrestitoDTO>> map = new HashMap<>();
        map.put(TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA.name(), prenotazioneService.getInformazioniTickets(prenotazioneService.getTicketsByBibliotecaAndStato(biblioteca, TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA)));
        map.put(TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE.name(), prenotazioneService.getInformazioniTickets(prenotazioneService.getTicketsByBibliotecaAndStato(biblioteca, TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE)));
        map.put(TicketPrestito.Stati.CHIUSO.name(), prenotazioneService.getInformazioniTickets(prenotazioneService.getTicketsByBibliotecaAndStato(biblioteca, TicketPrestito.Stati.CHIUSO)));

        return map;
    }

    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param id     l'ID del ticket da accettare
     * @param giorni il tempo di concessione del prestito
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/accetta")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse accettaPrenotazione(final @RequestParam int id,
                                                 @RequestHeader(name = "Authorization") final String token,
                                                 final @RequestParam(value = "giorni") int giorni) {

        TicketPrestito ticket = prenotazioneService.getTicketByID(id);

        if (!utils.isUtenteBiblioteca(token) || !utils.getSubjectFromToken(token).equals(ticket.getBiblioteca().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        ticket = prenotazioneService.accettaRichiesta(ticket, giorni);
        if (ticket != null) return new BiblionetResponse("Richiesta accettata", true);
        return new BiblionetResponse(BiblionetResponse.ERRORE, false);
    }

    /**
     * Implementa la funzionalità che permette di
     * richiedere il prestito di un libro.
     *
     * @param id l'ID del ticket da rifiutare
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/rifiuta")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse rifiutaPrenotazione(final @RequestParam int id, @RequestHeader(name = "Authorization") final String token) {
        TicketPrestito ticket = prenotazioneService.getTicketByID(id);

        if (!utils.isUtenteBiblioteca(token) || !utils.getSubjectFromToken(token).equals(ticket.getBiblioteca().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);


        ticket = prenotazioneService.rifiutaRichiesta(ticket);
        if (ticket != null) return new BiblionetResponse("Richiesta rifiutata", true);
        return new BiblionetResponse("Errore", false);
    }

    /**
     * Implementa la funzionalità che permette di
     * chiudere una prenotazione di un libro quando
     * questo viene riconsegnato.
     *
     * @param id l'ID del ticket da chiudere
     * @return La view che visualizza la lista delle prenotazioni
     */
    @PostMapping(value = "/ticket/chiudi")
    @ResponseBody
    @CrossOrigin
    public BiblionetResponse chiudiPrenotazione(final @RequestParam int id, @RequestHeader(name = "Authorization") final String token) {
        TicketPrestito ticket = prenotazioneService.getTicketByID(id);
        if (!utils.isUtenteBiblioteca(token) || !utils.getSubjectFromToken(token).equals(ticket.getBiblioteca().getEmail()))
            return new BiblionetResponse(BiblionetResponse.NON_AUTORIZZATO, false);

        ticket = prenotazioneService.chiudiTicket(ticket);
        if (ticket != null) return new BiblionetResponse("Prenotazione chiusa", true);
        return new BiblionetResponse("Errore", false);
    }

    /**
     * Implementa la funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @return La view che visualizza la lista delle prenotazioni del lettore
     */
    @GetMapping(value = "/visualizza-prenotazioni")
    @ResponseBody
    @CrossOrigin
    public List<TicketPrestitoDTO> visualizzaPrenotazioniLettore(@RequestHeader(name = "Authorization") final String token) {

        if (!utils.isUtenteLettore(token)) return null;

        return prenotazioneService.getInformazioniTickets(prenotazioneService.getTicketsByEmailLettore(utils.getSubjectFromToken(token)));

    }


    /**
     * Implementa la funzionalità che permette di
     * ottenere una lista di id e titoli di libri
     * sulla base di un titolo dato
     * ! Controllare prima di consegnare
     *
     * @param titolo il titolo che deve fare match
     * @return la lista di informazioni
     */
    @RequestMapping(value = "/find-libri-by-titolo-contains")
    @CrossOrigin
    public @ResponseBody List<ILibroIdAndName> findLibriByTitoloContains(
            @RequestParam("q") final String titolo
    ) {
        return prenotazioneService.findByTitoloContains(titolo);
    }

}
