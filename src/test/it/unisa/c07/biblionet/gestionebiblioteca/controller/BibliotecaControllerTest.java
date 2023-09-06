package it.unisa.c07.biblionet.gestionebiblioteca.controller;


import it.unisa.c07.biblionet.common.Libro;
import it.unisa.c07.biblionet.filter.JwtFilter;
import it.unisa.c07.biblionet.gestionebiblioteca.BibliotecaService;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Genere;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import it.unisa.c07.biblionet.utils.BiblionetResponse;
import it.unisa.c07.biblionet.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

        Libro l = new Libro(
                "BiblioNet",
                "Paulo Dybala",
                isbn,
                LocalDateTime.now(),
                "Biblioteche 2.0",
                "Mondadori"
        );
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
        Libro l = new Libro(
                "BiblioNet",
                "Paulo Dybala",
                isbn,
                LocalDateTime.now(),
                "Biblioteche 2.0",
                "Mondadori"
        );
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

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro con form manuale
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoManualeUserNull() throws Exception {
        //todo : Nel nostro caso l'utente non può essere null trovare una soluzione

        this.mockMvc.perform(post("/biblioteca/inserimento-manuale")
                .param("numCopie", "1"));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro con form manuale
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoManualeUserNotValid() throws Exception {

        String token="";

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(post("/biblioteca/inserimento-manuale")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .param("numCopie", "1"));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro con form manuale
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoManualeIsbnDescrizioneImmagineVuoto() throws Exception {


        String token="";
        //todo Required LibroDTO parameter 'libro' is not present, quindi sostanzialmente è na bad request. Manca qualcosa per i parametri obbligatori di LibroDTO
        //todo manca il test per il rispetto del formato
        Biblioteca b = new Biblioteca();

        b.setEmail("a");
        Libro l = new Libro();
        l.setTitolo("BiblioNet");
        l.setAutore("Stefano Lambiase");
        l.setCasaEditrice("Mondadori");
        l.setAnnoDiPubblicazione(LocalDateTime.of(2010, 1, 1, 1, 1));
        l.setImmagineLibro(null);
        l.setIdLibro(0);
        Set<String> generi = new HashSet<>();
        generi.add("g1");
        generi.add("g2");
        l.setGeneri(generi);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(prenotazioneService.inserimentoManuale(
                Mockito.any(), Mockito.anyString(), Mockito.anyInt(), Mockito.any()))
                .thenReturn(l);
        when(bibliotecaService.findBibliotecaByEmail("a"))
                .thenReturn(b);

        this.mockMvc.perform(post("/biblioteca/inserimento-manuale")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("isbn", "a")
                        .param("descrizione", "a")
                        .param("titolo", "BiblioNet")
                        .param("casaEditrice", "Mondadori")
                        .param("autore", "Stefano Lambiase")
                        .param("annoPubblicazione", "2010")
                        .param("generi", "g1")
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per l'inserimento di un libro con form manuale
     * simulando la richiesta http.
     *
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void inserimentoManuale() throws Exception {

        String token="";

        Biblioteca b = new Biblioteca();
        b.setEmail("a");
        Libro l = new Libro();
        l.setTitolo("BiblioNet");
        l.setAutore("Stefano Lambiase");
        l.setCasaEditrice("Mondadori");
        l.setAnnoDiPubblicazione(LocalDateTime.of(2010, 1, 1, 1, 1));
        l.setImmagineLibro(null);
        l.setIdLibro(0);
        l.setIsbn("1234");
        l.setDescrizione("descrizione");
        Set<String> generi = new HashSet<>();
        generi.add("g1");
        generi.add("g2");
        Genere g1 = new Genere();
        Genere g2 = new Genere();
        List<Genere> glist = new ArrayList<>();
        glist.add(g1);
        glist.add(g2);

        when(utils.isUtenteBiblioteca(Mockito.anyString())).thenReturn(true);
        when(utils.getSubjectFromToken(Mockito.anyString())).thenReturn("a");
        when(bibliotecaService.findBibliotecaByEmail("a"))
                .thenReturn(b);

        when(prenotazioneService.inserimentoManuale(
                l, "a", 1, generi))
                .thenReturn(l);
        this.mockMvc.perform(post("/biblioteca/inserimento-manuale")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("titolo", "BiblioNet")
                        .param("casaEditrice", "Mondadori")
                        .param("autore", "Stefano Lambiase")
                        .param("annoPubblicazione", "2010")
                        .param("generi", "g1")
                        .param("generi", "g2")
                        .param("isbn", "1234")
                        .param("descrizione", "descrizione")
                        .param("numCopie", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.descrizione").value(BiblionetResponse.OPERAZIONE_OK));
    }

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

}
