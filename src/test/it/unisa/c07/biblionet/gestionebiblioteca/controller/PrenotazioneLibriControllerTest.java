package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.TicketPrestitoDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.TicketPrestito;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Implementa il testing di unità per la classe
 * PrenotazioneLibriController.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PrenotazioneLibriControllerTest {

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private PrenotazioneLibriService prenotazioneService;
    @MockBean
    private BibliotecaService bibliotecaService;
    @MockBean
    private Utils utils;

    /**
     * Inject di MockMvc per simulare
     * le richieste http.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideTicketInAttesa() {
        return Stream.of(Arguments.of(new TicketPrestito(
                                TicketPrestito.Stati.IN_ATTESA_DI_CONFERMA,
                                LocalDateTime.now(),
                                new Libro(new LibroDTO(
                                        1,
                                        "BiblioNet",
                                        "Stefano Lambiase",
                                        "1234567890123",
                                        "1995",
                                        "Aooo",
                                        "Mondadori",
                                        "Biblioteche 2.0",
                                        new HashSet<String>()

                                )),
                                new Biblioteca(
                                        "b4@gmail.com",
                                        "aaaaa",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Naboli"
                                ),
                                new Lettore(
                                        "giuliociccione@gmail.com",
                                        "LettorePassword",
                                        "Salerno",
                                        "Baronissi",
                                        "Via Barone 11",
                                        "3456789012",
                                        "SuperLettore",
                                        "Giulio",
                                        "Ciccione"
                                )
                        )
                )
        );
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideTicketAccettato() {
        return Stream.of(Arguments.of(new TicketPrestito(
                                TicketPrestito.Stati.IN_ATTESA_DI_RESTITUZIONE,
                                LocalDateTime.now(),
                        new Libro(new LibroDTO(2,
                                "BiblioNet",
                                "Stefano Lambiase",
                                "1234567890123",
                                "1995",
                                "Aooo",
                                "Mondadori",
                                "Biblioteche 2.0",
                                new HashSet<String>()

                        )),
                                new Biblioteca(
                                        "b4@gmail.com",
                                        "aaaaa",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Naboli"
                                ),
                                new Lettore(
                                        "giuliociccione@gmail.com",
                                        "LettorePassword",
                                        "Salerno",
                                        "Baronissi",
                                        "Via Barone 11",
                                        "3456789012",
                                        "SuperLettore",
                                        "Giulio",
                                        "Ciccione"
                                )
                        )
                )
        );
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideTicketChiuso() {
        return Stream.of(Arguments.of(new TicketPrestito(
                                TicketPrestito.Stati.CHIUSO,
                                LocalDateTime.now(),
                        new Libro(new LibroDTO(3,
                                "BiblioNet",
                                "Stefano Lambiase",
                                "1234567890123",
                                "1995",
                                "Aooo",
                                "Mondadori",
                                "Biblioteche 2.0",
                                new HashSet<String>()

                        )),
                                new Biblioteca(
                                        "b4@gmail.com",
                                        "aaaaa",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Naboli"
                                ),
                                new Lettore(
                                        "giuliociccione@gmail.com",
                                        "LettorePassword",
                                        "Salerno",
                                        "Baronissi",
                                        "Via Barone 11",
                                        "3456789012",
                                        "SuperLettore",
                                        "Giulio",
                                        "Ciccione"
                                )
                        )
                )
        );
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideTicketRifiutato() {
        return Stream.of(Arguments.of(new TicketPrestito(
                                TicketPrestito.Stati.RIFIUTATO,
                                LocalDateTime.now(),
                        new Libro(new LibroDTO(3,
                                "BiblioNet",
                                "Stefano Lambiase",
                                "1234567890123",
                                "1995",
                                "Aooo",
                                "Mondadori",
                                "Biblioteche 2.0",
                                new HashSet<String>()

                        )),
                                new Biblioteca(
                                        "b4@gmail.com",
                                        "aaaaa",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Naboli"
                                ),
                                new Lettore(
                                        "giuliociccione@gmail.com",
                                        "LettorePassword",
                                        "Salerno",
                                        "Baronissi",
                                        "Via Barone 11",
                                        "3456789012",
                                        "SuperLettore",
                                        "Giulio",
                                        "Ciccione"
                                )
                        )
                )
        );
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione di
     * tutti i libri prenotabili
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaListaLibri() throws Exception {
        //todo la lista doveva essere piena (list.add(new Libro())
        List<Libro> list = new ArrayList<>();
        when(prenotazioneService.visualizzaListaLibriCompleta())
                .thenReturn(list);
        this.mockMvc.perform(get("/prenotazione-libri/"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione di
     * tutti i libri prenotabili
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaListaFiltrata() throws Exception {
        List<Libro> list = new ArrayList<>();
        when(prenotazioneService.visualizzaListaLibriPerTitolo("titolo"))
                .thenReturn(list);

        when(prenotazioneService.
                visualizzaListaLibriPerBiblioteca("biblioteca"))
                .thenReturn(list);
        when(prenotazioneService.
                visualizzaListaLibriCompleta()).thenReturn(list);

        this.mockMvc.perform(get("/prenotazione-libri/ricerca")
                        .param("filtro", "titolo")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

        this.mockMvc.perform(get("/prenotazione-libri/ricerca")
                        .param("filtro", "genere")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

        this.mockMvc.perform(get("/prenotazione-libri/ricerca")
                        .param("filtro", "biblioteca")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

        this.mockMvc.perform(get("/prenotazione-libri/ricerca")
                        .param("filtro", "sbagliato")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

    }

    /**
     * Implementa il test della funzionalità che permette di
     * richiedere il prestito di un libro
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void confermaPrenotazione() throws Exception {

        String token = "";

        TicketPrestito t = new TicketPrestito();

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("lettore@gmail.com");
        when(prenotazioneService.richiediPrestito("lettore@gmail.com",
                "id",
                1)).thenReturn(t);

        this.mockMvc.perform(
                        post("/prenotazione-libri/conferma-prenotazione")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("idBiblioteca", "id")
                                .param("idLibro", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

    @Test
    public void confermaPrenotazioneUserNotValid() throws Exception {

        String token = "";


        TicketPrestito t = new TicketPrestito();
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(
                        post("/prenotazione-libri/conferma-prenotazione")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("idBiblioteca", "id")
                                .param("idLibro", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ad una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketInAttesa")
    public void visualizzaRichiesteUserNotValid(final TicketPrestito t)
            throws Exception {

        String token="";

        List<TicketPrestito> list = new ArrayList<>();
        list.add(t);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-richieste")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ad una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */

    @ParameterizedTest
    @MethodSource("provideTicketInAttesa")
    public void visualizzaRichiesteTicketInAttesa(final TicketPrestito t) throws Exception {

        String token="";
        List<TicketPrestito> list = new ArrayList<>();
        list.add(t);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(t.getBiblioteca());
        when(prenotazioneService.getTicketsByBiblioteca(t.getBiblioteca()))
                .thenReturn(list);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-richieste")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CHIUSO").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_CONFERMA[0].stato").value(t.getStato().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_CONFERMA[0].idTicket").value(t.getIdTicket()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_RESTITUZIONE").isEmpty());

    }

    /**
     * Implementa il test della funzionalità che permette di
     * ad una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketAccettato")
    public void visualizzaRichiesteTicketAccettato(final TicketPrestito t) throws Exception {
        String token="";
        List<TicketPrestito> list = new ArrayList<>();
        list.add(t);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(t.getBiblioteca());
        when(prenotazioneService.getTicketsByBiblioteca(t.getBiblioteca()))
                .thenReturn(list);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-richieste")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CHIUSO").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_RESTITUZIONE[0].stato").value(t.getStato().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_RESTITUZIONE[0].idTicket").value(t.getIdTicket()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_CONFERMA").isEmpty());

    }

    /**
     * Implementa il test della funzionalità che permette di
     * ad una biblioteca di visualizzare le richieste di
     * prenotazione ricevute.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketChiuso")
    public void visualizzaRichiesteTicketChiuso(final TicketPrestito t) throws Exception {
        String token="";
        List<TicketPrestito> list = new ArrayList<>();
        list.add(t);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(t.getBiblioteca());
        when(prenotazioneService.getTicketsByBiblioteca(t.getBiblioteca()))
                .thenReturn(list);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-richieste")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_CONFERMA").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.CHIUSO[0].stato").value(t.getStato().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.CHIUSO[0].idTicket").value(t.getIdTicket()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.IN_ATTESA_DI_RESTITUZIONE").isEmpty());

    }

    /**
     * Implementa il test della funzionalità che permette di
     * accettare la richiesta di prestito di un libro.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void accettaPrenotazione() throws Exception {
        TicketPrestito ticket = new TicketPrestito();
        when(prenotazioneService.getTicketByID(1)).thenReturn(ticket);
        when(prenotazioneService.accettaRichiesta(ticket, 1))
                .thenReturn(ticket);
        this.mockMvc.perform(post("/prenotazione-libri/ticket/1/accetta")
                        .param("giorni", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * rifiutare la richiesta di prestito di un libro.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void rifiutaPrenotazione() throws Exception {
        TicketPrestito ticket = new TicketPrestito();
        when(prenotazioneService.getTicketByID(1)).thenReturn(ticket);
        when(prenotazioneService.rifiutaRichiesta(ticket)).thenReturn(ticket);
        this.mockMvc.perform(post("/prenotazione-libri/ticket/1/rifiuta"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

    /**
     * Implementa il test della funzionalità che permette
     * di chiudere una richiesta di prestito di un libro
     * quando questo viene restituito.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void chiudiPrenotazione() throws Exception {
        TicketPrestito ticket = new TicketPrestito();
        when(prenotazioneService.getTicketByID(1)).thenReturn(ticket);
        when(prenotazioneService.chiudiTicket(ticket)).thenReturn(ticket);
        this.mockMvc.perform(post("/prenotazione-libri/ticket/1/chiudi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketInAttesa")
    public void visualizzaPrenotazioniLettoreUserNotValid(final TicketPrestito t)
            throws Exception {

        String token="";

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-prenotazioni")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketInAttesa")
    public void visualizzaPrenotazioniLettoreTicketInAttesa(final TicketPrestito t)
            throws Exception {
        visualizzaPrenotazioniLettoreTicket(t);
    }

    private void visualizzaPrenotazioniLettoreTicket(final TicketPrestito t) throws Exception {
        String token="";
        TicketPrestitoDTO ticketPrestitoDTO = new TicketPrestitoDTO(t);

        List<TicketPrestito> list = new ArrayList<>();
        list.add(t);

        List<TicketPrestitoDTO> listDTO = new ArrayList<>();
        listDTO.add(ticketPrestitoDTO);

    when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
    when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(prenotazioneService.getTicketsByEmailLettore("a"))
                .thenReturn(list);

        this.mockMvc.perform(get("/prenotazione-libri/visualizza-prenotazioni")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idTicket").value(t.getIdTicket()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].stato").value(t.getStato().toString()));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketAccettato")
    public void visualizzaPrenotazioniLettoreTicketAccettato(final TicketPrestito t)
            throws Exception {
        visualizzaPrenotazioniLettoreTicket(t);
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketChiuso")
    public void visualizzaPrenotazioniLettoreTicketChiuso2(final TicketPrestito t)
            throws Exception {
        visualizzaPrenotazioniLettoreTicket(t);
    }

    /**
     * Implementa il test della funzionalità che permette di
     * ottenere la lista di ticket di un lettore.
     *
     * @param t Un ticket per la simulazione
     * @throws Exception Eccezione per MockMvc
     */
    @ParameterizedTest
    @MethodSource("provideTicketRifiutato")
    public void visualizzaPrenotazioniLettoreTicketChiuso(final TicketPrestito t)
            throws Exception {
        visualizzaPrenotazioniLettoreTicket(t);
    }
}