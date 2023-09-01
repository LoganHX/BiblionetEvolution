package it.unisa.c07.biblionet.gestioneclubdellibro.controller;


import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.*;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.*;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
@AutoConfigureMockMvc
public class ClubDelLibroControllerTest {

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
    private EspertoService espertoService;

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
    private static Stream<Arguments> provideClubDTO() {
        Set<String> generi = new HashSet<>();
        generi.add("Saggistica");
        return Stream.of(
                Arguments.of(new ClubDTO("Club1",
                        "descrizione1",
                        generi))
        );
    }

    /**
     * Simula i dati inviati da un metodo
     * http attraverso uno stream.
     *
     * @return Lo stream di dati.
     */
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

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(new ClubDelLibro("Club1",
                                "descrizione1",
                                new Esperto("drink@home.com",
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
                                                "Vieni che non ti faccio niente"))),
                        new Lettore("giuliociccione@gmail.com",
                                "LettorePassword",
                                "Salerno",
                                "Baronissi",
                                "Via Barone 11",
                                "3456789012",
                                "SuperLettore",
                                "Giulio",
                                "Ciccione"
                        ))

        );
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la creazione di un club
     * simulando la richiesta http.
     *
     * @param clubDTO Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */
    @ParameterizedTest
    @MethodSource("provideClubDTO")
    public void creaClubDelLibro(final ClubDTO clubDTO) throws Exception {
        String[] list = {"A", "B"};
        // TODO: static
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        Esperto esperto = Mockito.mock(Esperto.class);
        List<ClubDelLibro> listaClub = new ArrayList<>();
        esperto.setClubs(listaClub);

        ClubDelLibro cdl = new ClubDelLibro();

        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(esperto);
        when(clubService.creaClubDelLibro(Mockito.any(), Mockito.any())).thenReturn(cdl);
        when(espertoService.aggiornaEsperto(esperto)).thenReturn(esperto);
        // Assert del test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/crea")
                        //.file(copertina)
                        .param("nome", clubDTO.getNome())
                        .param("descrizione", clubDTO.getDescrizione())
                        .param("generi", list)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Club del Libro creato"));
    }


    /********************************** Tests fot visualizzaModificaDatiClub **********************************/

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void visualizzaListaEventiClubTest(final ClubDelLibro club) throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4NzI3NDk4OH0.5oCy7B9dBs97F7XGr1uVa-x1ofyjodrrthsd_xEu3_s";


        List<Evento> eventi_club = new ArrayList<>();
        List<Evento> eventi_lettore = new ArrayList<>();
        club.setIdClub(1);
        Evento evento = new Evento();
        Evento evento1 = new Evento();
        evento.setClub(club);
        evento1.setClub(club);
        evento.setIdEvento(1);
        evento1.setIdEvento(2);
        eventi_club.add(evento);
        eventi_club.add(evento1);
        eventi_lettore.add(evento);
        club.setEventi(eventi_club);
        Lettore lettore = new Lettore();
        lettore.setEventi(eventi_lettore);
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        when(clubService.getClubByID(1)).thenReturn(club);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/club-del-libro/1/eventi")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore)
                        .param("idClub", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));


    }

    /******************************************* Tests for partecipaClub ***************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per il reinderizzamento alla modifica
     * dei dati di un club del libro
     * simulando la richiesta http.
     *
     * @param club Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void modificaDatiClub(final ClubDelLibro club)
            throws Exception {
        // TODO: static
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        when(clubService.getClubByID(1)).thenReturn(club);
        when(clubService.salvaClub(club)).thenReturn(club);

        //when(clubService.getTuttiGeneri()).thenReturn(set); todo totalmente cambiato
        // Test
        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/club-del-libro/modifica")
                        .param("id", "1")
                        .param("nome",club.getNome())
                        .param("descrizione",club.getDescrizione())
                        .param("generi", String.valueOf(club.getGeneri()))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Modifiche apportate"));
    }

    /************************************** Tests for visualizzaCreaEvento ****************************/

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'iscrizione di un lettore ad un club
     * simulando la richiesta http.
     *
     * @param club Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void partecipaClub(final ClubDelLibro club) throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4NzI3NDk4OH0.5oCy7B9dBs97F7XGr1uVa-x1ofyjodrrthsd_xEu3_s";

        Lettore lettore = Mockito.mock(Lettore.class);
        List<Lettore> lettori = new ArrayList<>();
        club.setLettori(lettori);

        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(club);
        when(lettoreService.partecipaClub(club, lettore)).thenReturn(true);


        this.mockMvc
                .perform(post("/club-del-libro/iscrizione")
                        .param("id", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore)

                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_OK));
    }

    /*
    * TODO: rifare da zero
    *
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaEvento(final ClubDelLibro club) throws Exception {
        when(clubService.getClubByID(1)).thenReturn(club);
        when(eventiService.getLibroById(1)).thenReturn(Optional.of(
                new Libro(
                        "Bibbia ebraica. Pentateuco e Haftarot.",
                        "Dario Disegni",
                        "9788880578529",
                        LocalDateTime.of(2020, 1, 1, 0, 0),
                        "La Torah, a cura di Dario Disegni",
                        "Giuntina"
                )
        ));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/club-del-libro/1/crea-evento")
                        .param("nome", "Prova")
                        .param("descrizione", "Prova")
                        .param("data", "2024-12-12")
                        .param("ora", "11:24")
                        .param("libro", "1"))
                .andExpect(view().name(
                        "redirect:/club-del-libro/1/eventi"
                ));
    }

     */

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione dei dati di un club
     * simulando la richiesta http.
     *
     * @param club Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */
    @ParameterizedTest
    @MethodSource({"provideParameters"})
    public void visualizzaDatiClub(final ClubDelLibro club, final Lettore lettore) throws Exception {

        String id = "1";
        List<Lettore> lettori = new ArrayList<>();
        lettori.add(lettore);

        club.setLettori(lettori);

        List<LettoreDTO> lettoriDTO = new ArrayList<>();
        lettoriDTO.add(new LettoreDTO(lettore));
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(club);


        //todo problema dto
        this.mockMvc
                .perform(get("/club-del-libro/lettori-club/{id}", id))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(lettore.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value(lettore.getNome()));
    }


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione di tutti i club
     * presenti, simulando la richiesta http.
     *
     * @param club Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void visualizzaListaClubsFilterGenre(final ClubDelLibro club, final Lettore lettore) throws Exception {
        List<ClubDelLibro> list = new ArrayList<>();
        list.add(club);
        Set<String> list_club_genre = new HashSet<>();

        List<Lettore> lettori = new ArrayList<>();
        lettori.add(lettore);
        club.setLettori(lettori);

        list_club_genre.add("Fantasy");
        list_club_genre.add("Saggistica");
        club.setGeneri(list_club_genre);
        List<String> generi = new ArrayList<>();
        generi.add("Fantasy");

        when(clubService.visualizzaClubsDelLibro(Mockito.any())).thenReturn(list);

        //todo problema dto
        this.mockMvc.perform(get("/club-del-libro/")
                        .param("generi", String.valueOf(generi)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idClub").value(club.getIdClub()));

    }


    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione di tutti i club
     * presenti, simulando la richiesta http.
     *
     * @param club Un club per la simulazione
     * @throws Exception Eccezione per MovkMvc
     */

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void visualizzaListaClubsFilterCity(final ClubDelLibro club) throws Exception {

        List<ClubDelLibro> list = new ArrayList<>();
        List<String> città = new ArrayList<>();
        città.add("Salerno");

        System.out.println(città);
        System.out.println(città.get(0));


        list.add(club);
        //when(clubService.getCittaFromClubDelLibro(club)).thenReturn(list);
        //System.out.println(String.valueOf(list));
        this.mockMvc.perform(get("/club-del-libro/")
                        .param("città", String.valueOf(città)))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

    }


    @Test
    public void partecipaClubNonAutorizzato() throws Exception {
        //todo token
        String tokenBiblio = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiaWJsaW90ZWNhY2FycmlzaUBnbWFpbC5jb20iLCJyb2xlIjoiQmlibGlvdGVjYSIsImlhdCI6MTY4ODIwMjg2Mn0.u4Ej7gh1AswIUFSHnLvQZY3vS0VpHuhNhDIkbEd2H_o";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/club-del-libro/iscrizione")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBiblio)
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void partecipaClubLettoreIscritto(final ClubDelLibro club, final Lettore lettore) throws Exception {

        String tokenLettore = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY4NzI3NDk4OH0.5oCy7B9dBs97F7XGr1uVa-x1ofyjodrrthsd_xEu3_s";

        List<Lettore> list_lett = new ArrayList<>();
        list_lett.add(lettore);
        club.setLettori(list_lett);

        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(club);
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(lettore);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/club-del-libro/iscrizione")
                        .param("id", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ISCRIZIONE_FALLITA));

    }

    @Test
    public void visualizzaModificaDatiClubNonEsistente() throws Exception {
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        String id = "1";

        when(clubService.getClubByID(Integer.parseInt(id))).thenReturn(null);
        this.mockMvc.perform(post("/club-del-libro/modifica")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                        .param("id", id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void visualizzaModificaDatiClubNonAutorizzato() throws Exception {

        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        String id = "1";
        ClubDelLibro club = new ClubDelLibro();
        Esperto esperto = new Esperto();
        Esperto esperto1 = new Esperto();
        esperto.setEmail("patriarca57@outlook.com");
        esperto1.setEmail("liberale@gmail.com");
        club.setEsperto(esperto);

        when(clubService.getClubByID(Integer.parseInt(id))).thenReturn(club);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/club-del-libro/modifica")
                        .param("id", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void visualizzaListaEventiClubNonTrovato() throws Exception {

        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";

        String id = "1";

        when(clubService.getClubByID(Integer.parseInt(id))).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/club-del-libro/1/eventi")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)
                        .param("id", id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void visualizzaListaEventiClubNonAutorizzato() throws Exception {
        String tokenEsperto = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlhdml2aWFuaUBnbWFpbC5jb20iLCJyb2xlIjoiRXNwZXJ0byIsImlhdCI6MTY4NzUxMTUxNn0.T57rj7tmsAKJKLYvMATNd71sO6YRHjLlECYyhJ2CLzs";
        String id = "1";

        ClubDelLibro club = new ClubDelLibro();
        club.setIdClub(Integer.parseInt(id));
        Esperto esperto = new Esperto();
        when(clubService.getClubByID(Integer.parseInt(id))).thenReturn(club);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/club-del-libro/1/eventi")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenEsperto)

                        .param("id", id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));

    }


}
