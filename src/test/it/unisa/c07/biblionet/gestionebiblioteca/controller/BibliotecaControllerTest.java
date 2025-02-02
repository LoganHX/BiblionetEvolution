package it.unisa.c07.biblionet.gestionebiblioteca.controller;


import io.jsonwebtoken.MalformedJwtException;
import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.common.LibroDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaDTO;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.GenereService;
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

    /**
     * Mock del service per simulare
     * le operazioni dei metodi.
     */
    @MockBean
    private GenereService genereService;

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da isbn
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoIsbn_GenereSingoloEsistente_OK() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }
    @Test
    public void inserimentoIsbn_GeneriMultipliEsistenti_OK() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    @Test
    public void inserimentoIsbn_GenereSingoloNonEsistente() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void inserimentoIsbn_GeneriMultipliNonEsistenti() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }

    @Test
    public void inserimentoIsbn_GeneriMultipliEsistentiENonEsistenti() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));
    }


//    @Test
//    public void inserimentoIsbn_TokenNonValido() throws Exception {
//
//
//        String token="aaaa";
//        String[] generi = {""};
//        String isbn = "9780735611313";
//        Biblioteca b = new Biblioteca();
//        b.setEmail("a");
//
//        Set<String> stlist = new HashSet<>();
//
//        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
//        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
//        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
//        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
//
//        when(prenotazioneService.inserimentoPerIsbn(
//                isbn, "a", 1, stlist))
//                .thenReturn(new Libro());
//
//        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                        .param("isbn", isbn)
//                        .param("generi", generi)
//                        .param("numCopie", "1")).
//                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MalformedJwtException));
//    }

    @Test
    public void inserimentoIsbn_TokenNonDellaGiustaTipologia() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));
    }

    @Test
    public void inserimentoIsbn_TokenNonFornito() throws Exception {

        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(status().is(400));
    }



    @Test
    public void inserimentoIsbn_NumeroCopieNonValido() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "-1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));
    }

    @Test
    public void inserimentoIsbn_NumeroCopieNonFornito() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi))
                        //.param("numCopie", "1"))
                .andExpect(status().is(400));
    }


    @Test
    public void inserimentoIsbn_ISBNNonFornito() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        //.param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(status().is(400));
    }

    @Test
    public void inserimentoIsbn_ISBNNonValido() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(new Libro());

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", "123456789")
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.FORMATO_NON_VALIDO));
    }

    @Test
    public void inserimentoIsbn_ISBNLibroNonPresenteNelCatalogoRemoto() throws Exception {


        String token="";
        String[] generi = {""};
        String isbn = "9780735611313";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");

        Set<String> stlist = new HashSet<>();

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);

        when(prenotazioneService.inserimentoPerIsbn(
                isbn, "a", 1, stlist))
                .thenReturn(null);

        this.mockMvc.perform(post("/biblioteca/inserimento-isbn")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", isbn)
                        .param("generi", generi)
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
    }




    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro da archivio
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoDatabaseOK() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(prenotazioneService.getLibroByID(Mockito.anyInt())).thenReturn(new Libro());



        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));

    }

    @Test
    public void inserimentoDatabase_IDLibroNonPresenteNelDB() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(prenotazioneService.getLibroByID(Mockito.anyInt())).thenReturn(null);



        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));

    }



    @Test
    public void inserimentoDatabase_TokenNonFornito() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(status().is(400));

    }

    @Test
    public void inserimentoDatabase_TokenNonDellaGiustaTipologia() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.NON_AUTORIZZATO));

    }
//    @Test
//    public void inserimentoDatabase_TokenNonValido() throws Exception {
//        int idLibro = 1;
//        int numCopie = 5;
//        String token="";
//        Biblioteca b = new Biblioteca();
//        b.setEmail("a");
//
//
//        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
//        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
//        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
//        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
//                .thenReturn(new Libro());
//        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                        .param("idLibro", String.valueOf(idLibro))
//                        .param("numCopie", String.valueOf(numCopie)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));
//
//    }

    @Test
    public void inserimentoDatabase_IDLibroNonFornito() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        //.param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(status().is(400));

    }

    @Test
    public void inserimentoDatabase_NumeroCopieNonFornito() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro)))
                        //.param("numCopie", String.valueOf(numCopie)))
                .andExpect(status().is(400));

    }
    @Test
    public void inserimentoDatabase_LibroConIDFornitoNonPresenteNelDB() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(null);
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        //.param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(numCopie)))
                .andExpect(status().is(400));

    }

    @Test
    public void inserimentoDatabase_NumeroCopieNonValido() throws Exception {
        int idLibro = 1;
        int numCopie = 5;
        String token="";
        Biblioteca b = new Biblioteca();
        b.setEmail("a");


        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a")).thenReturn(b);
        when(prenotazioneService.inserimentoDalDatabase(idLibro, b.getEmail(), numCopie))
                .thenReturn(new Libro());
        this.mockMvc.perform(post("/biblioteca/inserimento-archivio")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("idLibro", String.valueOf(idLibro))
                        .param("numCopie", String.valueOf(-5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));

    }



    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_GenereSingoloEsistente_Ok(LibroDTO l) throws Exception {

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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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
    public void inserimentoManuale_GenereSingoloNonEsistente_Ok(LibroDTO l) throws Exception {

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
        generi.add("Poggibonsi"); //todo check esistenza generi

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_GeneriMultipliEsistenti_Ok(LibroDTO l) throws Exception {

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
        generi.add("Poggibonsi"); //todo check esistenza generi

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_GeneriMultipliNonEsistenti(LibroDTO l) throws Exception {

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
        generi.add("Poggibonsi"); //todo check esistenza generi

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_GeneriMultipliEsistentiENonEsistenti(LibroDTO l) throws Exception {

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
        generi.add("Poggibonsi"); //todo check esistenza generi

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(false);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OGGETTO_NON_TROVATO));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_NumeroCopieNonFornito(LibroDTO l) throws Exception {

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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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
                        //.param("numCopie", String.valueOf(5))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(400));    }

    @ParameterizedTest
    @MethodSource("provideLibroDTO")
    public void inserimentoManuale_NumeroCopieNonValido(LibroDTO l) throws Exception {

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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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
                        .param("numCopie", String.valueOf(-1))
                        .param("generi", String.valueOf(generi))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.RICHIESTA_NON_VALIDA));    }

//    @ParameterizedTest
//    @MethodSource("provideLibroDTO")
//    public void inserimentoManuale_TokenNonValido(LibroDTO l) throws Exception {
//
//        String token="";
//
//        Biblioteca b = new Biblioteca();
//        b.setEmail("a");
//
//        byte[] imageData = new byte[2 * 1024 * 1024]; // Creiamo un'immagine di 2 MB
//
//        MockMultipartFile copertina =
//                new MockMultipartFile("copertina",
//                        "filename.png",
//                        "image/png",
//                        imageData);
//
//        Set<String> generi = new HashSet<>();
//        generi.add("Giallo"); //todo check esistenza generi
//
//        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
//        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
//        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
//        when(utils.immagineOk(Mockito.any())).thenReturn(true);
//        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
//        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);
//
//
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .multipart("/biblioteca/inserimento-manuale")
//                        .file(copertina)
//                        .param("titolo",l.getTitolo())
//                        .param("autore",l.getAutore())
//                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
//                        .param("casaEditrice", l.getCasaEditrice())
//                        .param("isbn", l.getIsbn())
//                        .param("descrizione", l.getDescrizione())
//                        .param("numCopie", String.valueOf(5))
//                        .param("generi", String.valueOf(generi))
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));    }

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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

//    @ParameterizedTest
//    @MethodSource("provideLibroDTO")
//    public void inserimentoManuale_CopertinaTroppoPesante(LibroDTO l) throws Exception {
//
//        String token="";
//
//        Biblioteca b = new Biblioteca();
//        b.setEmail("a");
//
//        byte[] imageData = new byte[16 * 1024 * 1024]; // Creiamo un'immagine di 2 MB
//
//        MockMultipartFile copertina =
//                new MockMultipartFile("copertina",
//                        "filename.png",
//                        "image/png",
//                        imageData);
//
//        Set<String> generi = new HashSet<>();
//        generi.add("Giallo"); //todo check esistenza generi
//
//        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
//        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
//        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
//        when(utils.immagineOk(Mockito.any())).thenReturn(true);
//        when(prenotazioneService.creaLibroDaModel(Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any())).thenReturn(new Libro(l));
//        when(bibliotecaService.findBibliotecaByEmail(Mockito.anyString())).thenReturn(b);
//
//
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .multipart("/biblioteca/inserimento-manuale")
//                        .file(copertina)
//                        .param("titolo",l.getTitolo())
//                        .param("autore",l.getAutore())
//                        .param("annoDiPubblicazione", String.valueOf(l.getAnnoDiPubblicazione()))
//                        .param("casaEditrice", l.getCasaEditrice())
//                        .param("isbn", l.getIsbn())
//                        .param("descrizione", l.getDescrizione())
//                        .param("numCopie", String.valueOf(5))
//                        .param("generi", String.valueOf(generi))
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.ERRORE));    }

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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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

        when(genereService.doGeneriExist(Mockito.any())).thenReturn(true);
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
    public void visualizzaInformazioniBiblioteca() throws Exception {
        Biblioteca biblioteca = new Biblioteca(new BibliotecaDTO(
                "bibliotecacarrisi@gmail.com",
                "BibliotecaPassword",
                "Napoli",
                "Torre del Greco",
                "Via Carrisi 47",
                "1234567890",
                "Biblioteca Carrisi"
        ));

        when(bibliotecaService
                .findBibliotecaByEmail(Mockito.anyString())).thenReturn(biblioteca);
        this.mockMvc.perform(post("/biblioteca/informazioni")
                        .param("email", biblioteca.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(biblioteca.getEmail()));
    }

    /**
     * Utilizzato per fornire il libro ai vari test
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
