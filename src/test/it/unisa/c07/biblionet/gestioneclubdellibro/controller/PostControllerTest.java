package it.unisa.c07.biblionet.gestioneclubdellibro.controller;



import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.ClubDelLibroService;
import it.unisa.c07.biblionet.gestioneclubdellibro.EspertoService;
import it.unisa.c07.biblionet.gestioneclubdellibro.LettoreService;
import it.unisa.c07.biblionet.gestioneclubdellibro.PostService;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.ClubDelLibro;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Esperto;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Post;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
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

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
public class PostControllerTest {


    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private LettoreService lettoreService;
    @MockBean
    private EspertoService espertoService;
    @MockBean
    private ClubDelLibroService clubService;
    @MockBean
    private Utils utils;
    @MockBean
    private PostService postService;

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


    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPostOk(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TitoloNonFornito(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        //.param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_ContenutoNonFornito(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        //.param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_IDClubNonFornito(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        //.param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_IDClubNonMatchaConClubEsperto(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_IDClubInesistente(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(null);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TitoloTroppoLungo(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_ContenutoTroppoLungo(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("content", "Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incolla un testo e sulla destra appariranno automaticamente tutte le statistiche. Il calcolo è dinamico, dall'istante in cui inizi a scrivere, oppure dopo aver incollato a")
                        .param("titolo", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_ContenutoTroppoCorto(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("content", "aa")
                        .param("titolo", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TitoloTroppoCorto(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa-a")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TokenNonValido(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }
    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TokenNonFornito(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1"))
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(status().is(400));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void creaPost_TokenNonDellaGiustaTipologia(ClubDelLibro clubDelLibro) throws Exception {

        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(clubService.getClubByID(Mockito.anyInt())).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.creaPostDaModel(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new Post());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/crea")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idClub", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void aggiungiCommento_ByEspertoOk(ClubDelLibro clubDelLibro) throws Exception {

        Post post = mock(Post.class);


        when(utils.match(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(utils.isUtenteEsperto(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(post.getClubDelLibro()).thenReturn(clubDelLibro);
        when(espertoService.findEspertoByEmail(Mockito.anyString())).thenReturn(clubDelLibro.getEsperto());
        when(postService.getPostByID(Mockito.anyInt())).thenReturn(post);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @ParameterizedTest
    @MethodSource("provideClubDelLibro")
    public void aggiungiCommento_ByLettoreOk(ClubDelLibro clubDelLibro) throws Exception {

        Post post = mock(Post.class);

        clubDelLibro.setIdClub(1);
        when(utils.isUtenteLettore(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(post.getClubDelLibro()).thenReturn(clubDelLibro);
        when(lettoreService.findLettoreByEmail(Mockito.anyString())).thenReturn(new Lettore());
        when(postService.getPostByID(Mockito.anyInt())).thenReturn(post);
        when(clubService.isLettoreIscrittoAClub("a", 1)).thenReturn(true);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/post/aggiungi-commento")
                        .param("titolo", "Prova")
                        .param("content", "Prova")
                        .param("idPost", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + "token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }
}
