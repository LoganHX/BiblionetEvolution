package it.unisa.c07.biblionet.gestionebiblioteca.controller;


import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Assertions;
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

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implementa il testing di unità per la classe
 * BibliotecaController.
 *
 * @author Viviana Pentangelo, Gianmario Voria
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@AutoConfigureMockMvc
public class BibliotecaControllerTest {

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private PrenotazioneLibriService prenotazioneService;

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
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
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da isbn
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoIsbnUserNull() throws Exception {

        //todo : Nel nostro caso l'utente non può essere null trovare una soluzione
        String[] generi = {""};

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                .param("isbn", "a")
                .param("generi", generi)
                .param("numCopie", "1")   )  .andExpect(result ->
                Assertions.assertTrue(result.getResolvedException() instanceof ServletException));    // Verifica la classe del lancio della eccezione

    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da isbn
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoIsbnUserNotValid() throws Exception {


        String token="";
        String[] generi = {""};

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .param("isbn", "a")
                .param("generi", generi)
                .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da isbn
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoIsbnLibroNull() throws Exception {


        String token = "";
        String[] generi = {""};
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoPerIsbn(
                "a", "a",1, stlist))
                .thenReturn(null);

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", "a")
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value("Libro non creato"));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da isbn
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoIsbn() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "1234567890123";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        Libro l =  new Libro(new LibroDTO(
                1,
                "BiblioNet",
                "Stefano Lambiase",
                "1234567890123",
                "1995",
                "Aooo",
                "Mondadori",
                "Biblioteche 2.0",
                new HashSet<>()

        ));
        l.setIdLibro(3);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(l);

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da archivio
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoDatabaseUserNull() throws Exception {
        //todo : Nel nostro caso l'utente non può essere null trovare una soluzione

        //JwtFilter filtro = Mockito.mock(JwtFilter.class);
//
//            Mockito.doNothing().when(filtro).doFilter(Mockito.any(), Mockito.any(), Mockito.any());

        //doNothing().when(filtro).doFilter(Mockito.any(),Mockito.any(), Mockito.any());


        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                .param("idLibro", "1")
                .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da archivio
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoDatabaseUserNotValid() throws Exception {

        String token = "";
        when(utils.isUtenteBiblioteca(token)).thenReturn(false);

        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .param("idLibro", "1")
                .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));

    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da archivio
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoDatabase() throws Exception {

        String token="";
        String isbn = "1234567890123";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");
        Libro l =  new Libro(new LibroDTO(
                1,
                "BiblioNet",
                "Stefano Lambiase",
                "1234567890123",
                "1995",
                "Aooo",
                "Mondadori",
                "Biblioteche 2.0",
                new HashSet<String>()

        ));
        l.setIdLibro(3);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(
                3, "a", 1))
                .thenReturn(l);
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", "3")
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }





    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManualeOk(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TokenNonValido(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TokenNonDellaGiustaTipologia(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));}

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TokenNonFornito(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        )
                .andExpect(status().is(400));
    }




    /**
         * Implementa il test della funzionalità gestita dal
         * controller per l'inserimento di un libro con form manuale
         * simulando la richiesta http.
         *
         * @throws Exception Eccezione per MockMvc
         */
    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_CasaEditriceTroppoCorta(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", "aa")
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_CasaEditriceTroppoLunga(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", "Il conta caratteri è semplice d")
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }


    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_DescrizioneTroppoLunga(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", "Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incolla un testo e sulla destra appariranno automaticamentee")
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_DescrizioneTroppoCorta(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", "aa")
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_AnnoDiPubblicazioneNonFornito(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_DescrizioneNonFornita(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_AnnoDiPubblicazioneNonValido(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(202))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_CasaEditriceNonFornita(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_AutoreTroppoLungo(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore","Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incoll")
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_AutoreNonFornito(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_AutoreTroppoCorto(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore","aa")
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_FormatoCopertinaNonValido(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(false);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_CopertinaNonFornita(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(false);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        //.file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_CopertinaTroppoPesante(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[16 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TitoloNonFornito(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TitoloTroppoCorto(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo","AA")
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_TitoloTroppoLungo(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo","Il conta caratteri è semplice da usare, scrivi nella casella di input oppure copia e incoll")
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("isbn", l.getIsbn())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_ISBNNonFornito(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("descrizione", l.getDescrizione())
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_ISBNNonValido(LibroDTO l) throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB

        MockMultipartFile copertina =
                new MockMultipartFile("copertina",
                        "filename.png",
                        "image/png",
                        imageData);

        Set<String> generi = new HashSet<>();
        generi.add("Giallo"); //todo check esistenza generi

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(utils.immagineOk(Mockito.any())).thenReturn(true);
        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);


        this.mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/biblioteca/inserimento-manuale")
                        .file(copertina)
                        .param("titolo",l.getTitolo())
                        .param("autore",l.getAutore())
                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
                        .param("casaEditrice", l.getCasaEditrice())
                        .param("descrizione", l.getDescrizione())
                        .param("isbn", "123456789")
                        .param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));    }

    /**
     * Implementa il test della funzionalità che permette di
     * visualizzare le biblioteche filtrate.     *
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaListaFiltrata() throws Exception {
        List<Biblioteca> list = new ArrayList<>();

        when(bibliotecaService.findBibliotecaByNome("a")).thenReturn(list);
        when(bibliotecaService.findBibliotecaByCitta("a")).thenReturn(list);
        when(bibliotecaService.findAllBiblioteche()).thenReturn(list);

        this.mockMvc.perform(get("/biblioteca/ricerca")
                        .param("filtro", "nome")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));
        this.mockMvc.perform(get("/biblioteca/ricerca")
                        .param("filtro", "citta")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));
        this.mockMvc.perform(get("/biblioteca/ricerca")
                        .param("filtro", "default")
                        .param("stringa", "a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));

    }

    /**
     * Implementa il test della funzionalitá di visualizzazione
     * della lista di tutte le biblioteche.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaListaBiblioteche() throws Exception {
        List<Biblioteca> list = new ArrayList<>();

        when(bibliotecaService.findAllBiblioteche()).thenReturn(list);
        this.mockMvc.perform(get("/biblioteca/visualizza-biblioteche"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(list));
    }

    /**
     * Implementa il test dla funzionalitá di visualizzazione
     * del profilo di una singola biblioteca.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaDatiBiblioteca() throws Exception {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setEmail("a");
        when(bibliotecaService
                .findBibliotecaByEmail("a")).thenReturn(biblioteca);
        this.mockMvc.perform(get("/biblioteca/a"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(biblioteca.getEmail()));
    }

    /**
     * Utilizzato per fornire il libro ai test
     * @return il libro
     */
    private static Stream<Arguments> provideLibroDTO() {
        return Stream.of(
                Arguments.of(
                        new LibroDTO(
                                1,
                                "Fru",
                                "9597845613497",
                                "1234567891234",
                                "2010",
                                "Mondadori",
                                "Casa",
                                "@@@@@",
                                null
                        )
                )
        );
    }
}
