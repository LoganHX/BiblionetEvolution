package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.UtenteRegistrato;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.GestioneEventiService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Evento;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Implementa il testing di unità per la classe
 * ClubDelLibroController.
 *
 * @author Viviana Pentangelo
 * @author Gianmario Voria
 * @author Nicola Pagliara
 * @author Luca Topo
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EventoControllerTest {

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private ClubDelLibroService clubService;

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private LettoreService lettoreService;
    @MockBean
    private GestioneEventiService eventiService;

    @MockBean
    private Utils utils;

    /**
     * Inject di MockMvc per simulare
     * le richieste http.
     */
    @Autowired
    private MockMvc mockMvc;

    private static Stream<Arguments> provideClubDelLibro() {
        return Stream.of(
                Arguments.of(new ClubDelLibro("Club1",
                        "descrizione1",
                        new Esperto("eliaviviani@gmail.com",
                                "ALotOfBeerInMyLife",
                                "Salerno",
                                "Salerno",
                                "Via vicino casa di Stefano 2",
                                "3694578963",
                                "mrDuff",
                                "Nicola",
                                "Pagliara",
                                new Biblioteca("gmail@gmail.com",
                                        "Ueuagliobellstuorolog69",
                                        "Napoli",
                                        "Scampia",
                                        "Via Portici 47",
                                        "3341278415",
                                        "Vieni che non ti faccio niente"))))
        );
    }




    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     * @return Lo stream di dati.
     */
    private static Stream<Arguments> provideLettore() {
        return Stream.of(Arguments.of(new Lettore("giuliociccione@gmail.com",
                "LettorePassword",
                "Salerno",
                "Baronissi",
                "Via Barone 11",
                "3456789012",
                "SuperLettore",
                "Giulio",
                "Ciccione"
        )));
    }
    /*************************************+ Tests for eliminaEvento *********************************/
    @Test
    public void eliminaEvento() throws Exception {
        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(eventiService.eliminaEvento(1)).thenReturn(new Evento());
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);


        when(
                eventiService.eliminaEvento(1)
        ).thenReturn(new Evento());

        this.mockMvc
                .perform(post("/gestione-eventi/elimina-evento")
                        .param("idClub", "1")
                        .param("idEvento", "1"
                )                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "aa"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Evento eliminato"));
    }


/******************************************** Tests for partecipaEvento *******************************/


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la partecipazione ad un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void partecipaEventoUserNotOk() throws Exception {
        String token="";
        String idClub = "1";
        String idEvento = "1";

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/gestione-eventi/{idClub}/eventi/{idEvento}/iscrizione", idClub, idEvento)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    /**************************************** Tests for abbandonaEvento ***************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la partecipazione ad un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void partecipaEventoUserOk() throws Exception {
        String token="";
        Lettore lettore = new Lettore(
                "antoniorenatomontefusco@gmail.com",
                "LettorePassword",
                "Napoli",
                "Somma Vesuviana",
                "Via Vesuvio 33",
                "3456789012",
                "antoniomontefusco",
                "Antonio",
                "Montefusco"
        );


        String idEvento = "1";

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(lettoreService.findLettoreByEmail("a")).thenReturn(lettore);
        when(eventiService.partecipaEvento(Mockito.any(), Mockito.anyInt())).thenReturn(lettore);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/gestione-eventi/eventi/{idEvento}/iscrizione", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                ).andExpect(MockMvcResultMatchers.jsonPath("$.email").value(lettore.getEmail()));
    }
    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'abbandono di un un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void abbandonaEvento() throws Exception {

        int idEvento = 1;
        int idClub = 1;

        String token="";
        Lettore lettore = new Lettore();
        lettore.setEmail("paulo@dybala.it");

        when(eventiService.isLettoreIscrittoEvento(Mockito.anyInt(), Mockito.anyString())).thenReturn(new Evento(
                "Evento fantastyco",
                "Evento fantastyco per gente fantastyca",
                LocalDateTime.now(),
                new ClubDelLibro()
        ));
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        when(eventiService.abbandonaEvento(Mockito.anyString(), Mockito.anyInt())).thenReturn(lettore);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/gestione-eventi/eventi/{idEvento}/abbandono", idEvento)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'abbandono di un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void abbandonaEventoUtenteNonIscritto() throws Exception {

        String token="";
        Evento e =new Evento();

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(eventiService.isLettoreIscrittoEvento(Mockito.anyInt(), Mockito.anyString())).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/gestione-eventi/eventi/15/abbandono")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }



    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'abbandono di un un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void abbandonaEventoUtenteInesistente() throws Exception {

        String token="";
        Lettore lettore = new Lettore();
        lettore.setEmail("paulo@dybala.it");

        when(eventiService.isLettoreIscrittoEvento(Mockito.anyInt(), Mockito.anyString())).thenReturn(new Evento(
                "Evento fantastyco",
                "Evento fantastyco per gente fantastyca",
                LocalDateTime.now(),
                new ClubDelLibro()
        ));
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(null);
        when(eventiService.abbandonaEvento(Mockito.anyString(), Mockito.anyInt())).thenReturn(lettore);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/gestione-eventi/eventi/15/abbandono")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'abbandono di un un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void abbandonaEventoUtenteNonAutorizzato() throws Exception {

        String token="";

        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/gestione-eventi/eventi/15/abbandono")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }


    /*************************** Tests for Exception ******************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la creazione di un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */

    @Test
    public void modificaEventoClubOk() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void modificaEventoClub_TokenNonValido() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void modificaEventoClub_TokenNonAssociatoAEspertoClub() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void modificaEventoClub_TokenNonDellaGiustaTipologia() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);

        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void modificaEventoClub_TokenNonFornito() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento)))
                .andExpect(status().is(400));
    }

    @Test
    public void modificaEventoClub_IDClubNonFornito() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }

    @Test
    public void modificaEventoClub_IDClubNonPresenteNelDB() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(null);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void modificaEventoClub_IDClubNonAssociatoAEvento() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(2);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }

    @Test
    public void modificaEventoClub_IDLibroNonFornito() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void modificaEventoClub_IDLibroNonPresenteNelDB() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.empty());
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Libro inserito non valido"));
    }


    @Test
    public void modificaEventoClub_DescrizioneTroppoLunga() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "It’s also not even remotely new. Although Gurnee and Tegmark seem impressed with their results (“we atempt to extract an actual map of the world!”) the fact at geography can be weakly but imperfectly inferred from language corpora— is actually already well")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DescrizioneTroppoCorta() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "aa")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DescrizioneNonFornita() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeTroppoLungo() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeTroppoCorto() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "aa")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_NomeNonFornito() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void modificaEventoClub_DataNonValida() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2020-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Data non valida"));
    }

    @Test
    public void modificaEventoClub_DataNonFornita() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }


    @Test
    public void modificaEventoClub_OraNonFornita() throws Exception {

        String token = "";

        int idEvento = 1;

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Evento e = Mockito.mock(Evento.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
        when(eventiService.getEventoById(Mockito.anyInt())).thenReturn(Optional.of(e));
        when(e.getClub()).thenReturn(clubDelLibro);
        when(clubDelLibro.getIdClub()).thenReturn(idEvento);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/modifica")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2020-12-12")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .param("idEvento", String.valueOf(idEvento))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClubOk() throws Exception {

    String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void creaEventoClub_IDClubNonPresenteNelDB() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(null);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    @Test
    public void creaEventoClub_IDClubNonFornito() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClub_TokenNonFornito() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1"))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClub_TokenNonAssociatoAEspertoClub() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

//    @Test
//    public void creaEventoClub_TokenNonValido() throws Exception {
//
//        String token = "";
//
//        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
//        Esperto esperto = Mockito.mock(Esperto.class);
//        when(clubDelLibro.getEsperto()).thenReturn(esperto);
//        when(esperto.getEmail()).thenReturn("a");
//        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
//        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
//        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
//        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
//        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
//        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());
//
//        this.mockMvc
//                .perform(MockMvcRequestBuilders
//                        .post("/gestione-eventi/crea")
//                        .param("nome", "Prova")
//                        .param("descrizione", "Prova")
//                        .param("dateString", "2024-12-12")
//                        .param("timeString", "11:24")
//                        .param("idClub", "1")
//                        .param("idLibro", "1")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
//    }

    @Test
    public void creaEventoClub_TokenNonDellaGiustaTipologia() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }



    @Test
    public void creaEventoClub_NomeNonFornito() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }


    @Test
    public void creaEventoClub_DataNonValida() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2021-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Data non valida"));
    }


    @Test
    public void creaEventoClub_DataNonFornita() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }



    @Test
    public void creaEventoClub_OraNonFornita() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("idClub", "1")
                        .param("timeString", "11:24")

                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));
    }

    @Test
    public void creaEventoClub_DescrizioneTroppoCorta() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "a")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_DescrizioneTroppoLunga() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "It’s also not even remotely new. Although Gurnee and Tegmark seem impressed with their results (“we atempt to extract an actual map of the world!”) the fact at geography can be weakly but imperfectly inferred from language corpora— is actually already well")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_DescrizioneNonFornita() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_IDLibroNonPresenteNelDB() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.empty());
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Libro inserito non valido"));
    }


    @Test
    public void creaEventoClub_NomeTroppoLungo() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-aaa")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_NomeTroppoCorto() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.getLibroById(Mockito.anyInt())).thenReturn(Optional.of(new Libro()));
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "a")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .param("idLibro", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void creaEventoClub_IDLibroNonFornito() throws Exception {

        String token = "";

        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(eventiService.modificaEvento(Mockito.any())).thenReturn(new Evento());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/gestione-eventi/crea")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("dateString", "2024-12-12")
                        .param("timeString", "11:24")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la eliminazione di un evento
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MovkMvc
     */
    @Test
    public void eliminaEventoInesistente() throws Exception {
        ClubDelLibro clubDelLibro = Mockito.mock(ClubDelLibro.class);
        Esperto esperto = Mockito.mock(Esperto.class);
        when(clubDelLibro.getEsperto()).thenReturn(esperto);
        when(esperto.getEmail()).thenReturn("a");
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(eventiService.eliminaEvento(1)).thenReturn(null);
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/gestione-eventi/elimina-evento") .header(HttpHeaders.AUTHORIZATION, "Bearer " + "aaa")
                        .param("idClub", String.valueOf(1))
                        .param("idEvento", String.valueOf(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Evento inesistente"));
    }







}
