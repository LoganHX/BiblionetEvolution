package it.unisa.c07.biblionet.gestionebiblioteca.controller;

import it.unisa.c07.biblionet.BiblionetApplication;
import it.unisa.c07.biblionet.common.LibroDAO;
import it.unisa.c07.biblionet.gestionebiblioteca.PrenotazioneLibriService;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.Biblioteca;
import it.unisa.c07.biblionet.gestionebiblioteca.repository.BibliotecaDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.GenereDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.LettoreDAO;
import it.unisa.c07.biblionet.gestioneclubdellibro.repository.Lettore;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Antonio Della Porta
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiblionetApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrenotazioneLibriControllerIntegrationTest {

    @Autowired
    @Setter
    @Getter
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrenotazioneLibriService prenotazioneLibriService;

    @Autowired
    private BibliotecaDAO bibliotecaDAO;

    @Autowired
    private LibroDAO libroDAO;

    @Autowired
    private LettoreDAO lettoreDAO;

    @Before
    public void init() {
        BiblionetApplication.init((ConfigurableApplicationContext) applicationContext);
    }

    /**
     * Implementa il test della funzionalità gestita dal
     * controller per la visualizzazione di
     * tutti i libri prenotabili
     * simulando la richiesta http.
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void visualizzaListaLibri() throws Exception {

        this.mockMvc.perform(get("/prenotazione-libri/"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").exists());
    }

    @Test
    public void ricercaFiltrata() throws Exception {
        var libri=libroDAO.findAll();
        var libro =libri.get(0);
        String titolo=libro.getTitolo();
        var id=libro.getIdLibro();
        this.mockMvc.perform(get("/prenotazione-libri/ricerca")
                .param("filtro", "titolo")
                .param("stringa", titolo))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(id));
    }

    /**
     * Implementa il test della funzionalità che permette di
     * richiedere il prestito di un libro
     * simulando la richiesta http.
     * @throws Exception Eccezione per MockMvc
     */
    @Test
    public void confermaPrenotazione() throws Exception {

        String tokenLettore="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbnRvbmlvcmVuYXRvbW9udGVmdXNjb0BnbWFpbC5jb20iLCJyb2xlIjoiTGV0dG9yZSIsImlhdCI6MTY5NzQ0NjY1N30.nxifMlaYv4VQML6YLtmKNM-_AfkyjLQ4GQ3B9_mTmF4";
        Biblioteca biblioteca = bibliotecaDAO.findByID("bibliotecacarrisi@gmail.com");


        this.mockMvc.perform(
                post("/prenotazione-libri/conferma-prenotazione")
                        .param("emailBiblioteca", biblioteca.getEmail())
                        .param("idLibro", "2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenLettore))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusOk").value(true));
    }

}
